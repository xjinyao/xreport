package com.xjinyao.report.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 谢进伟
 * @createDate 2023/4/24 09:54
 */
@Data
@ConfigurationProperties(prefix = "xreport")
public class XReportProperties {

	private Boolean debug;
	private Boolean disableHttpSessionReportCache;
	private Boolean disableFileProvider;
	private Boolean loadSystemFont;
	private String fileStoreDir;
	private String gatewayUrlPrefix;
}
