package com.xjinyao.report.core.definition.datasource.connection;

/**
 * @author 谢进伟
 * @createDate 2023/3/5 11:18
 */
public enum DBType {

	/**
	 * MySQL数据库
	 */
	MYSQL,
	/**
	 * oracle sid 方式
	 */
	ORACLE_SID,
	/**
	 * oracle service name 方式
	 */
	ORACLE_SERVICE_NAME,
	/**
	 * PostgreSQL数据库
	 */
	POSTGRESQL,
	/**
	 * teradata数据库
	 */
	TERADATA,
	/**
	 * MariaDB数据库
	 */
	MARIADB,
	/**
	 * Microsoft SQLServer数据库
	 */
	SQLSERVER,
	/**
	 * 人大金仓数据库
	 */
	KINGBASE8,
	/**
	 * 达梦数据库
	 */
	DM,
	/**
	 * DB2数据库
	 */
	DB2,
}
