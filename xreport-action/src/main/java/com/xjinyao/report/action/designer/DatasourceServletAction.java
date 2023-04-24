package com.xjinyao.report.action.designer;

import com.xjinyao.report.action.RenderPageServletAction;
import com.xjinyao.report.action.exception.ReportDesignException;
import com.xjinyao.report.core.Utils;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.definition.dataset.Field;
import com.xjinyao.report.core.definition.datasource.BuiltinDatasource;
import com.xjinyao.report.core.definition.datasource.DataType;
import com.xjinyao.report.core.definition.datasource.connection.ConnectionInfo;
import com.xjinyao.report.core.definition.datasource.connection.DBType;
import com.xjinyao.report.core.definition.datasource.connection.DatasourceConnection;
import com.xjinyao.report.core.definition.datasource.connection.JdbcUrlPartInfo;
import com.xjinyao.report.core.expression.ExpressionUtils;
import com.xjinyao.report.core.expression.model.Expression;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectExpressionData;
import com.xjinyao.report.core.utils.JdbcUrlUtils;
import com.xjinyao.report.core.utils.ProcedureUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class DatasourceServletAction extends RenderPageServletAction {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = retriveMethod(req);
		if (method != null) {
			invokeMethod(method, req, resp);
		}
	}

	public void loadBuildinDatasources(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<String> datasources = new ArrayList<>();
		for (BuiltinDatasource datasource : Utils.getBuildinDatasources()) {
			datasources.add(datasource.name());
		}
		writeObjectToJson(resp, datasources);
	}

	public void loadMethods(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String beanId = req.getParameter("beanId");
		Object obj = applicationContext.getBean(beanId);
		Class<?> clazz = obj.getClass();
		Method[] methods = clazz.getMethods();
		List<String> result = new ArrayList<>();
		for (Method method : methods) {
			Class<?>[] types = method.getParameterTypes();
			if (types.length != 3) {
				continue;
			}
			Class<?> typeClass1 = types[0];
			Class<?> typeClass2 = types[1];
			Class<?> typeClass3 = types[2];
			if (!String.class.isAssignableFrom(typeClass1)) {
				continue;
			}
			if (!String.class.isAssignableFrom(typeClass2)) {
				continue;
			}
			if (!Map.class.isAssignableFrom(typeClass3)) {
				continue;
			}
			result.add(method.getName());
		}
		writeObjectToJson(resp, result);
	}

	public void buildClass(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String clazz = req.getParameter("clazz");
		List<Field> result = new ArrayList<>();
		try {
			Class<?> targetClass = Class.forName(clazz);
			PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(targetClass);
			for (PropertyDescriptor pd : propertyDescriptors) {
				String name = pd.getName();
				if ("class".equals(name)) {
					continue;
				}
				result.add(new Field(name));
			}
			writeObjectToJson(resp, result);
		} catch (Exception ex) {
			throw new ReportDesignException(ex);
		}
	}

	public void buildDatabaseTables(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = null;
		ResultSet rs = null;
		try {
			DatasourceConnection datasourceConnection = buildConnection(req);
			conn = datasourceConnection.getConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			String catalog = null;
			String schema = null;
			ConnectionInfo connectionInfo = datasourceConnection.getConnectionInfo();
			if (connectionInfo != null && connectionInfo.getUrlPartInfo() != null) {
				JdbcUrlPartInfo urlPartInfo = connectionInfo.getUrlPartInfo();
				catalog = urlPartInfo.getDatabase();
				DBType databaseType = urlPartInfo.getDatabaseType();
				if (databaseType.equals(DBType.ORACLE_SID) || databaseType.equals(DBType.ORACLE_SERVICE_NAME)) {
					schema = metaData.getUserName();
				}
				if (databaseType.equals(DBType.MYSQL) || databaseType.equals(DBType.MARIADB)) {
					catalog = urlPartInfo.getDatabase();
				}
			}
			List<Map<String, String>> tables = new ArrayList<>();
			rs = metaData.getTables(catalog, schema, "%", new String[]{"TABLE", "VIEW"});
			while (rs.next()) {
				Map<String, String> table = new HashMap<>();
				table.put("name", rs.getString("TABLE_NAME"));
				table.put("type", rs.getString("TABLE_TYPE"));
				tables.add(table);
			}
			writeObjectToJson(resp, tables);
		} catch (Exception ex) {
			throw new ServletException(ex);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeConnection(conn);
		}
	}

	public static String getHostFrom(String url) {
		Pattern p = Pattern.compile("jdbc:(?<db>\\w+):.*((//)|@)(?<host>.+):(?<port>\\d+)(/|(;DatabaseName=)|:)(?<dbName>\\w+)\\??.*");
		Matcher m = p.matcher(url);
		if (m.find()) {
//            System.out.println(m.group("db"));
//            System.out.println(m.group("host"));
//            System.out.println(m.group("port"));
//            System.out.println(m.group("dbName"));
		}
		return m.group("dbName");
	}

	public void buildFields(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String sql = req.getParameter("sql");
		String parameters = req.getParameter("parameters");
		Connection conn = null;
		final List<Field> fields = new ArrayList<>();
		try {
			conn = buildConnection(req).getConnection();
			Map<String, Object> map = buildParameters(parameters);
			sql = parseSql(sql, map);
			if (ProcedureUtils.isProcedure(sql)) {
				List<Field> fieldsList = ProcedureUtils.procedureColumnsQuery(sql, map, conn);
				fields.addAll(fieldsList);
			} else {
				DataSource dataSource = new SingleConnectionDataSource(conn, false);
				NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(dataSource);
				PreparedStatementCreator statementCreator = getPreparedStatementCreator(sql, new MapSqlParameterSource(map));
				jdbc.getJdbcOperations().execute(statementCreator, new PreparedStatementCallback<Object>() {
					@Override
					public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
						ResultSet rs = null;
						try {
							rs = ps.executeQuery();
							ResultSetMetaData metadata = rs.getMetaData();
							int columnCount = metadata.getColumnCount();
							for (int i = 0; i < columnCount; i++) {
								String columnName = metadata.getColumnLabel(i + 1);
								fields.add(new Field(columnName));
							}
							return null;
						} finally {
							JdbcUtils.closeResultSet(rs);
						}
					}
				});
			}
			writeObjectToJson(resp, fields);
		} catch (Exception ex) {
			throw new ReportDesignException(ex);
		} finally {
			JdbcUtils.closeConnection(conn);
		}
	}

	protected PreparedStatementCreator getPreparedStatementCreator(String sql, SqlParameterSource paramSource) {
		ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(sql);
		String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, paramSource);
		Object[] params = NamedParameterUtils.buildValueArray(parsedSql, paramSource, null);
		List<SqlParameter> declaredParameters = NamedParameterUtils.buildSqlParameterList(parsedSql, paramSource);
		PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(sqlToUse, declaredParameters);
		return pscf.newPreparedStatementCreator(params);
	}

	public void previewData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String sql = req.getParameter("sql");
		String parameters = req.getParameter("parameters");
		Map<String, Object> map = buildParameters(parameters);
		sql = parseSql(sql, map);
		Connection conn = null;
		try {
			conn = buildConnection(req).getConnection();
			List<Map<String, Object>> list = null;
			if (ProcedureUtils.isProcedure(sql)) {
				list = ProcedureUtils.procedureQuery(sql, map, conn);
			} else {
				DataSource dataSource = new SingleConnectionDataSource(conn, false);
				NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(dataSource);
				list = jdbc.queryForList(sql, map);
			}
			int size = list.size();
			int currentTotal = size;
			if (currentTotal > 500) {
				currentTotal = 500;
			}
			List<Map<String, Object>> ls = new ArrayList<>();
			for (int i = 0; i < currentTotal; i++) {
				ls.add(list.get(i));
			}
			DataResult result = new DataResult();
			List<String> fields = new ArrayList<>();
			if (size > 0) {
				Map<String, Object> item = list.get(0);
				for (String name : item.keySet()) {
					fields.add(name);
				}
			}
			result.setFields(fields);
			result.setCurrentTotal(currentTotal);
			result.setData(ls);
			result.setTotal(size);
			writeObjectToJson(resp, result);
		} catch (Exception ex) {
			throw new ServletException(ex);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String parseSql(String sql, Map<String, Object> parameters) {
		sql = sql.trim();
		Context context = new Context(applicationContext, parameters);
		if (sql.startsWith(ExpressionUtils.EXPR_PREFIX) && sql.endsWith(ExpressionUtils.EXPR_SUFFIX)) {
			sql = sql.substring(2, sql.length() - 1);
			Expression expr = ExpressionUtils.parseExpression(sql);
			sql = executeSqlExpr(expr, context);
			return sql;
		} else {
			String sqlForUse = sql;
			Pattern pattern = Pattern.compile("\\$\\{.*?\\}");
			Matcher matcher = pattern.matcher(sqlForUse);
			while (matcher.find()) {
				String substr = matcher.group();
				String sqlExpr = substr.substring(2, substr.length() - 1);
				Expression expr = ExpressionUtils.parseExpression(sqlExpr);
				String result = executeSqlExpr(expr, context);
				sqlForUse = sqlForUse.replace(substr, result);
			}
			Utils.logToConsole("DESIGN SQL:" + sqlForUse);
			return sqlForUse;
		}
	}

	private String executeSqlExpr(Expression sqlExpr, Context context) {
		String sqlForUse = null;
		ExpressionData<?> exprData = sqlExpr.execute(null, null, context);
		if (exprData instanceof ObjectExpressionData) {
			ObjectExpressionData data = (ObjectExpressionData) exprData;
			Object obj = data.getData();
			if (obj != null) {
				String s = obj.toString();
				s = s.replaceAll("\\\\", "");
				sqlForUse = s;
			}
		}
		return sqlForUse;
	}

	public void testConnection(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String driver = req.getParameter("driver");
		String url = req.getParameter("url");
		Connection conn = null;
		Map<String, Object> map = new HashMap<>();
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
			map.put("result", true);
		} catch (Exception ex) {
			map.put("error", ex.toString());
			map.put("result", false);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		writeObjectToJson(resp, map);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> buildParameters(String parameters) throws IOException, JsonParseException, JsonMappingException {
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isBlank(parameters)) {
			return map;
		}
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, Object>> list = mapper.readValue(parameters, ArrayList.class);
		for (Map<String, Object> param : list) {
			String name = param.get("name").toString();
			DataType type = DataType.valueOf(param.get("type").toString());
			String defaultValue = (String) param.get("defaultValue");
			if (defaultValue == null || defaultValue.equals("")) {
				switch (type) {
					case Boolean:
						map.put(name, false);
					case Date:
						map.put(name, new Date());
					case Float:
						map.put(name, new Float(0));
					case Integer:
						map.put(name, 0);
					case String:
						if (defaultValue != null && defaultValue.equals("")) {
							map.put(name, "");
						} else {
							map.put(name, "null");
						}
						break;
					case List:
						map.put(name, new ArrayList<>());
				}
			} else {
				map.put(name, type.parse(defaultValue));
			}
		}
		return map;
	}

	private DatasourceConnection buildConnection(HttpServletRequest req) throws Exception {
		String type = req.getParameter("type");
		if (type.equals("jdbc")) {
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			String driver = req.getParameter("driver");
			String url = req.getParameter("url");

			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, username, password);
			return DatasourceConnection.builder()
					.connection(conn)
					.connectionInfo(ConnectionInfo.builder()
							.username(username)
							.password(password)
							.driver(driver)
							.url(url)
							.urlPartInfo(JdbcUrlUtils.getJdbcUrlParts(url))
							.build())
					.build();
		} else {
			String name = req.getParameter("name");
			DatasourceConnection conn = Utils.getBuildinConnection(name);
			if (conn == null) {
				throw new ReportDesignException("Buildin datasource [" + name + "] not exist.");
			}
			return conn;
		}
	}


	@Override
	public String url() {
		return "/datasource";
	}
}
