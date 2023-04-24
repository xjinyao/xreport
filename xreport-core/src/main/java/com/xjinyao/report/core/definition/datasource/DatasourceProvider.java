package com.xjinyao.report.core.definition.datasource;

import java.sql.Connection;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface DatasourceProvider {
	Connection getConnection();

	String getName();
}
