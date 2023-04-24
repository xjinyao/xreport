package com.xjinyao.report.core.definition.datasource.connection;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * jdbc url部分信息
 *
 * @author 谢进伟
 * @createDate 2023/03/05
 */
@Data
@Builder
public class JdbcUrlPartInfo {


	/**
	 * 数据库类型
	 */
	private DBType databaseType;
	/**
	 * host
	 */
	private String host;
	/**
	 * 端口
	 */
	private String port;
	/**
	 * 数据库
	 */
	private String database;

	/**
	 * 参数个数
	 */
	private List<String> params;
}
