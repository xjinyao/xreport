package com.xjinyao.report.core.definition.datasource.connection;

import lombok.Builder;
import lombok.Data;

/**
 * 连接信息
 *
 * @author 谢进伟
 * @createDate 2023/03/05
 */
@Data
@Builder
public class ConnectionInfo {
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 驱动
	 */
	private String driver;
	/**
	 * url
	 */
	private String url;

	/**
	 * url一部分信息
	 */
	private JdbcUrlPartInfo urlPartInfo;
}
