package com.xjinyao.report.core.utils;

import com.xjinyao.report.core.definition.datasource.connection.DBType;
import com.xjinyao.report.core.definition.datasource.connection.JdbcUrlPartInfo;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JDBC-URL参数提取工具类
 *
 * @author 谢进伟
 * @createDate 2023/3/5 09:46
 */
@Slf4j
@UtilityClass
public class JdbcUrlUtils {

	/**
	 * 主机
	 */
	public static final String HOST = "host";
	/**
	 * 端口
	 */
	public static final String PORT = "port";
	/**
	 * 数据库
	 */
	public static final String DATABASE = "database";
	/**
	 * 参数
	 */
	public static final String PARAMS = "params";
	/**
	 * 文件夹
	 */
	public static final String FOLDER = "folder";
	/**
	 * 文件
	 */
	public static final String FILE = "file";
	/**
	 * sid
	 */
	private static final String SID = "sid";
	/**
	 * 名字
	 */
	private static final String NAME = "name";

	/**
	 * mysql jdbc url模式
	 */
	private static final String JDBC_MYSQL_URL_PATTERN = "jdbc:mysql://{host}[:{port}]/[{database}][\\?{params}]";
	/**
	 * oracle sid jdbc url模式
	 */
	private static final String JDBC_ORACLE_SID_URL_PATTERN = "jdbc:oracle:thin:@{host}[:{port}]:{sid}";
	/**
	 * oracle name jdbc url模式
	 */
	private static final String JDBC_ORACLE_NAME_URL_PATTERN = "jdbc:oracle:thin:@//{host}[:{port}]/{name}";
	/**
	 * postgresql jdbc url模式
	 */
	private static final String JDBC_POSTGRESQL_URL_PATTERN = "jdbc:postgresql://{host}[:{port}]/[{database}][\\?{params}]";
	/**
	 * teradata jdbc url模式
	 */
	private static final String JDBC_TERADATA_URL_PATTERN = "jdbc:teradata://{host}/DATABASE={database},DBS_PORT={port}[,{params}]";
	/**
	 * mariadb jdbc url模式
	 */
	private static final String JDBC_MARIADB_URL_PATTERN = "jdbc:mariadb://{host}[:{port}]/[{database}][\\?{params}]";
	/**
	 * sqlserver jdbc url模式
	 */
	private static final String JDBC_SQLSERVER_URL_PATTERN = "jdbc:sqlserver://{host}[:{port}][;databaseName={database}][;{params}]";
	/**
	 * kingbase8 jdbc  url模式
	 */
	private static final String JDBC_KINGBASE8_URL_PATTERN = "jdbc:kingbase8://{host}[:{port}]/[{database}][\\?{params}]";
	/**
	 * dm jdbcurl模式
	 */
	private static final String JDBC_DM_URL_PATTERN = "jdbc:dm://{host}:{port}[/{database}][\\?{params}]";
	/**
	 * db2 jdbc url模式
	 */
	private static final String JDBC_DB2_URL_PATTERN = "jdbc:db2://{host}:{port}/{database}[:{params}]";

	/**
	 * 正则表达式得到属性
	 *
	 * @param property 属性
	 * @return {@link String}
	 */
	public String getPropertyRegex(String property) {
		switch (property) {
			case FOLDER:
			case FILE:
			case PARAMS:
				return ".+?";
			default:
				return "[\\\\w\\\\-_.~]+";
		}
	}

	/**
	 * 替换所有
	 *
	 * @param input    输入
	 * @param regex    正则表达式
	 * @param replacer 替代者
	 * @return {@link String}
	 */
	public String replaceAll(String input, String regex, Function<Matcher, String> replacer) {
		final Matcher matcher = Pattern.compile(regex).matcher(input);
		final StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, replacer.apply(matcher));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 得到模式
	 *
	 * @param sampleUrl 示例url
	 * @return {@link Pattern}
	 */
	public Pattern getPattern(String sampleUrl) {
		String pattern = sampleUrl;
		pattern = replaceAll(pattern, "\\[(.*?)]", m -> "\\\\E(?:\\\\Q" + m.group(1) + "\\\\E)?\\\\Q");
		pattern = replaceAll(pattern, "\\{(.*?)}", m -> "\\\\E(\\?<\\\\Q" + m.group(1) + "\\\\E>" +
				getPropertyRegex(m.group(1)) + ")\\\\Q");
		pattern = "^\\Q" + pattern + "\\E$";
		return Pattern.compile(pattern);
	}


	public JdbcUrlPartInfo getJdbcUrlParts(String url) {
		if (url.startsWith("jdbc:mysql")) {
			return getJdbcUrlPartInfo(JDBC_MYSQL_URL_PATTERN, url, DBType.MYSQL, "&");
		} else if (url.startsWith("jdbc:oracle")) {
			String regex = null;
			JdbcUrlPartInfo partInfo = getJdbcUrlPartInfo(JDBC_ORACLE_SID_URL_PATTERN, url, DBType.ORACLE_SID, SID,
					null);
			if (partInfo != null) {
				return partInfo;
			}
			return getJdbcUrlPartInfo(JDBC_ORACLE_NAME_URL_PATTERN, url, DBType.ORACLE_SERVICE_NAME, NAME, null);
		} else if (url.startsWith("jdbc:postgresql")) {
			return getJdbcUrlPartInfo(JDBC_POSTGRESQL_URL_PATTERN, url, DBType.POSTGRESQL, "&");
		} else if (url.startsWith("jdbc:teradata")) {
			return getJdbcUrlPartInfo(JDBC_TERADATA_URL_PATTERN, url, DBType.TERADATA, ",");
		} else if (url.startsWith("jdbc:mariadb")) {
			return getJdbcUrlPartInfo(JDBC_MARIADB_URL_PATTERN, url, DBType.MARIADB, "&");
		} else if (url.startsWith("jdbc:sqlserver")) {
			return getJdbcUrlPartInfo(JDBC_SQLSERVER_URL_PATTERN, url, DBType.SQLSERVER, ";");
		} else if (url.startsWith("jdbc:kingbase8")) {
			return getJdbcUrlPartInfo(JDBC_KINGBASE8_URL_PATTERN, url, DBType.KINGBASE8, "&");
		} else if (url.startsWith("jdbc:dm")) {
			return getJdbcUrlPartInfo(JDBC_DM_URL_PATTERN, url, DBType.DM, "&");
		} else if (url.startsWith("jdbc:db2")) {
			return getJdbcUrlPartInfo(JDBC_DB2_URL_PATTERN, url, DBType.DB2, ";");
		}
		return null;
	}

	private static JdbcUrlPartInfo getJdbcUrlPartInfo(String jdbcDb2UrlPattern,
													  String url,
													  DBType dbType,
													  String regex) {
		return getJdbcUrlPartInfo(jdbcDb2UrlPattern, url, dbType, DATABASE, regex);

	}

	private static JdbcUrlPartInfo getJdbcUrlPartInfo(String jdbcDb2UrlPattern,
													  String url,
													  DBType dbType,
													  String databaseGroupName,
													  String regex) {
		final Matcher matcher = JdbcUrlUtils.getPattern(jdbcDb2UrlPattern)
				.matcher(url);
		if (matcher.matches()) {
			String host = matcher.group(HOST);
			String port = matcher.group(PORT);
			String database = matcher.group(databaseGroupName);
			log.info("{} host:{}", dbType, host);
			log.info("{} port:{}", dbType, port);
			log.info("{} {}:{}", dbType, databaseGroupName, database);
			List<String> urlParams = new ArrayList<>();
			if (regex != null) {
				String params = matcher.group(PARAMS);
				if (null != params) {
					String[] pairs = params.split(regex);
					for (String pair : pairs) {
						log.info("{} params:{}", dbType, pair);
						urlParams.add(pair);
					}
				}
			}
			return JdbcUrlPartInfo.builder()
					.databaseType(dbType)
					.host(host)
					.port(port)
					.database(database)
					.params(urlParams)
					.build();
		} else {
			log.error("error for {}!", dbType);
			return null;
		}
	}

}
