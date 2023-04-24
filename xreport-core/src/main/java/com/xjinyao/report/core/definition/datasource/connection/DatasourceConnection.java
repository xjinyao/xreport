package com.xjinyao.report.core.definition.datasource.connection;

import lombok.Builder;
import lombok.Data;

import java.sql.Connection;

/**
 * @author 谢进伟
 * @createDate 2023/3/5 11:08
 */
@Data
@Builder
public class DatasourceConnection {
	/**
	 * 连接
	 */
	private Connection connection;

	/**
	 * 连接信息
	 */
	private ConnectionInfo connectionInfo;
}
