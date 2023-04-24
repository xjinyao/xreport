package com.xjinyao.report.core.tests;

import com.xjinyao.report.core.utils.JdbcUrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;

/**
 * @author 谢进伟
 * @createDate 2023/3/5 09:49
 */
@Slf4j
public class JdbcUrlUtilsTest {


	/**
	 * 测试代码
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		// 1、teradata数据库
		// jdbc:teradata://localhost/DATABASE=test,DBS_PORT=1234,CLIENT_CHARSET=EUC_CN,TMODE=TERA,CHARSET=ASCII,LOB_SUPPORT=true
		final Matcher matcher0 = JdbcUrlUtils.getPattern("jdbc:teradata://{host}/DATABASE={database},DBS_PORT={port}[,{params}]").matcher(
				"jdbc:teradata://localhost/DATABASE=test,DBS_PORT=1234,CLIENT_CHARSET=EUC_CN,TMODE=TERA,CHARSET=ASCII,LOB_SUPPORT=true");
		if (matcher0.matches()) {
			log.info("teradata host:" + matcher0.group(JdbcUrlUtils.HOST));
			log.info("teradata port:" + matcher0.group(JdbcUrlUtils.PORT));
			log.info("teradata database:" + matcher0.group(JdbcUrlUtils.DATABASE));
			String params = matcher0.group(JdbcUrlUtils.PARAMS);
			if (null != params) {
				String[] pairs = params.split(",");
				for (String pair : pairs) {
					log.info("teradata params:" + pair);
				}
			}
		} else {
			log.info("error for teradata!");
		}

		// 2、PostgreSQL数据库
		// jdbc:postgresql://localhost:5432/dvdrental?currentSchema=test&ssl=true
		// https://jdbc.postgresql.org/documentation/head/connect.html
		final Matcher matcher1 = JdbcUrlUtils.getPattern("jdbc:postgresql://{host}[:{port}]/[{database}][\\?{params}]")
				.matcher("jdbc:postgresql://localhost:5432/dvdrental?currentSchema=test&ssl=true");
		if (matcher1.matches()) {
			log.info("postgresql host:" + matcher1.group(JdbcUrlUtils.HOST));
			log.info("postgresql port:" + matcher1.group(JdbcUrlUtils.PORT));
			log.info("postgresql database:" + matcher1.group(JdbcUrlUtils.DATABASE));
			String params = matcher1.group(JdbcUrlUtils.PARAMS);
			if (null != params) {
				String[] pairs = params.split("&");
				for (String pair : pairs) {
					log.info("postgresql params:" + pair);
				}
			}
		} else {
			log.info("error for postgresql!");
		}

		// 3、Oracle数据库
		// oracle sid 方式
		final Matcher matcher2 = JdbcUrlUtils.getPattern("jdbc:oracle:thin:@{host}[:{port}]:{sid}")
				.matcher("jdbc:oracle:thin:@localhost:1521:orcl");
		if (matcher2.matches()) {
			log.info("oracle sid host:" + matcher2.group(JdbcUrlUtils.HOST));
			log.info("oracle sid port:" + matcher2.group(JdbcUrlUtils.PORT));
			log.info("oracle sid name:" + matcher2.group("sid"));
		} else {
			log.info("error for oracle sid!");
		}

		// oracle service name 方式
		final Matcher matcher2_1 = JdbcUrlUtils.getPattern("jdbc:oracle:thin:@//{host}[:{port}]/{name}")
				.matcher("jdbc:oracle:thin:@//localhost:1521/orcl.city.com");
		if (matcher2_1.matches()) {
			log.info("oracle ServiceName host:" + matcher2_1.group(JdbcUrlUtils.HOST));
			log.info("oracle ServiceName port:" + matcher2_1.group(JdbcUrlUtils.PORT));
			log.info("oracle ServiceName name:" + matcher2_1.group("name"));
		} else {
			log.info("error for oracle ServiceName!");
		}

		// oracle TNSName 方式不支持
		// jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=192.168.16.91)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=orcl)))
		// ..............................

		// 4、MySQL数据库
		// jdbc:mysql://172.17.2.10:3306/test?useUnicode=true&useSSL=false
		final Matcher matcher3 = JdbcUrlUtils.getPattern("jdbc:mysql://{host}[:{port}]/[{database}][\\?{params}]")
				.matcher("jdbc:mysql://localhost:3306/test_demo?useUnicode=true&useSSL=false");
		if (matcher3.matches()) {
			log.info("mysql host:" + matcher3.group(JdbcUrlUtils.HOST));
			log.info("mysql port:" + matcher3.group(JdbcUrlUtils.PORT));
			log.info("mysql database:" + matcher3.group(JdbcUrlUtils.DATABASE));
			String params = matcher3.group(JdbcUrlUtils.PARAMS);
			if (null != params) {
				String[] pairs = params.split("&");
				for (String pair : pairs) {
					log.info("mysql params:" + pair);
				}
			}
		} else {
			log.info("error for mysql!");
		}

		// 5、MariaDB数据库
		// 同Mysql的jdbc-url
		final Matcher matcher4 = JdbcUrlUtils.getPattern("jdbc:mariadb://{host}[:{port}]/[{database}][\\?{params}]")
				.matcher("jdbc:mariadb://localhost:3306/test_demo");
		if (matcher4.matches()) {
			log.info("mariadb host:" + matcher4.group(JdbcUrlUtils.HOST));
			log.info("mariadb port:" + matcher4.group(JdbcUrlUtils.PORT));
			log.info("mariadb database:" + matcher4.group(JdbcUrlUtils.DATABASE));
			String params = matcher4.group(JdbcUrlUtils.PARAMS);
			if (null != params) {
				String[] pairs = params.split("&");
				for (String pair : pairs) {
					log.info("mysql params:" + pair);
				}
			}
		} else {
			log.info("error for mariadb!");
		}

		// 6、Microsoft SQLServer数据库
		// jdbc:sqlserver://localhost:1433;databaseName=AdventureWorks;user=MyUserName;password=123456;
		final Matcher matcher5 = JdbcUrlUtils
				.getPattern("jdbc:sqlserver://{host}[:{port}][;databaseName={database}][;{params}]")
				.matcher("jdbc:sqlserver://localhost:1433;databaseName=master;user=MyUserName");
		if (matcher5.matches()) {
			log.info("sqlserver host:" + matcher5.group(JdbcUrlUtils.HOST));
			log.info("sqlserver port:" + matcher5.group(JdbcUrlUtils.PORT));
			log.info("sqlserver database:" + matcher5.group(JdbcUrlUtils.DATABASE));
			String params = matcher5.group(JdbcUrlUtils.PARAMS);
			if (null != params) {
				String[] pairs = params.split(";");
				for (String pair : pairs) {
					log.info("sqlserver params:" + pair);
				}
			}
		} else {
			log.info("error for sqlserver!");
		}

		// 7、人大金仓数据库
		// 同postgresql的jdbc-url
		final Matcher matcher6 = JdbcUrlUtils.getPattern("jdbc:kingbase8://{host}[:{port}]/[{database}][\\?{params}]")
				.matcher("jdbc:kingbase8://localhost:5432/sample");
		if (matcher6.matches()) {
			log.info("kingbase8 host:" + matcher6.group(JdbcUrlUtils.HOST));
			log.info("kingbase8 port:" + matcher6.group(JdbcUrlUtils.PORT));
			log.info("kingbase8 database:" + matcher6.group(JdbcUrlUtils.DATABASE));
			String params = matcher6.group(JdbcUrlUtils.PARAMS);
			if (null != params) {
				String[] pairs = params.split("&");
				for (String pair : pairs) {
					log.info("mysql params:" + pair);
				}
			}
		} else {
			log.info("error for kingbase8!");
		}

		// 8、达梦数据库
		// jdbc:dm://localhost:5236/user?param=hello
		final Matcher matcher7 = JdbcUrlUtils.getPattern("jdbc:dm://{host}:{port}[/{database}][\\?{params}]")
				.matcher("jdbc:dm://localhost:5236");
		if (matcher7.matches()) {
			log.info("dm host:" + matcher7.group(JdbcUrlUtils.HOST));
			log.info("dm port:" + matcher7.group(JdbcUrlUtils.PORT));
			log.info("dm database:" + matcher7.group(JdbcUrlUtils.DATABASE));
			String params = matcher7.group(JdbcUrlUtils.PARAMS);
			if (null != params) {
				String[] pairs = params.split("&");
				for (String pair : pairs) {
					log.info("dm params:" + pair);
				}
			}
		} else {
			log.info("error for dm!");
		}

		// 9、DB2数据库
		// jdbc:db2://localhost:50000/testdb:driverType=4;fullyMaterializeLobData=true;fullyMaterializeInputStreams=true;progressiveStreaming=2;progresssiveLocators=2;
		final Matcher matcher8 = JdbcUrlUtils.getPattern("jdbc:db2://{host}:{port}/{database}[:{params}]")
				.matcher("jdbc:db2://localhost:50000/testdb:driverType=4;fullyMaterializeLobData=true");
		if (matcher8.matches()) {
			log.info("db2 host:" + matcher8.group(JdbcUrlUtils.HOST));
			log.info("db2 port:" + matcher8.group(JdbcUrlUtils.PORT));
			log.info("db2 database:" + matcher8.group(JdbcUrlUtils.DATABASE));
			String params = matcher8.group(JdbcUrlUtils.PARAMS);
			if (null != params) {
				String[] pairs = params.split(";");
				for (String pair : pairs) {
					log.info("mysql params:" + pair);
				}
			}
		} else {
			log.info("error for db2!");
		}

		// 9、SQLite数据库
		// jdbc:sqlite:/tmp/phone.db
		final Matcher matcher9 = JdbcUrlUtils.getPattern("jdbc:sqlite:{file}")
				.matcher("jdbc:sqlite:D:\\Project\\Test\\phone.db");
		if (matcher9.matches()) {
			log.info("sqlite file:" + matcher9.group("file"));
		} else {
			log.info("error for sqlite!");
		}
	}

	@Test
	public void test01() {
		JdbcUrlUtils.getJdbcUrlParts("jdbc:teradata://localhost/DATABASE=test,DBS_PORT=1234,CLIENT_CHARSET=EUC_CN,TMODE=TERA,CHARSET=ASCII,LOB_SUPPORT=true");
		JdbcUrlUtils.getJdbcUrlParts("jdbc:postgresql://localhost:5432/dvdrental?currentSchema=test&ssl=true");
		JdbcUrlUtils.getJdbcUrlParts("jdbc:oracle:thin:@localhost:1521:orcl");
		JdbcUrlUtils.getJdbcUrlParts("jdbc:oracle:thin:@//localhost:1521/orcl.city.com");
		JdbcUrlUtils.getJdbcUrlParts("jdbc:mysql://localhost:3306/test_demo?useUnicode=true&useSSL=false");
		JdbcUrlUtils.getJdbcUrlParts("jdbc:mariadb://localhost:3306/test_demo");
		JdbcUrlUtils.getJdbcUrlParts("jdbc:sqlserver://localhost:1433;databaseName=master;user=MyUserName");
		JdbcUrlUtils.getJdbcUrlParts("jdbc:kingbase8://localhost:5432/sample");
		JdbcUrlUtils.getJdbcUrlParts("jdbc:dm://localhost:5236");
		JdbcUrlUtils.getJdbcUrlParts("jdbc:db2://localhost:50000/testdb:driverType=4;fullyMaterializeLobData=true");
	}
}
