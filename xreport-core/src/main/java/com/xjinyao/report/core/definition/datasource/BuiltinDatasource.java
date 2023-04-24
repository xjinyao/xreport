package com.xjinyao.report.core.definition.datasource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import java.sql.Connection;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface BuiltinDatasource {
	/**
	 * @return 返回数据源名称
	 */
	String name();

	/**
	 * @return 返回当前采用数据源的一个连接
	 */
	Connection getConnection();


	/**
	 * 属性
	 *
	 * @return {@link DataSourceProperties}
	 */
	DataSourceProperties getProperties();
}
