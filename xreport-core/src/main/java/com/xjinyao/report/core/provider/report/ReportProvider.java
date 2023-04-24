package com.xjinyao.report.core.provider.report;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface ReportProvider {
	/**
	 * 根据报表名加载报表文件
	 *
	 * @param file 报表名称
	 * @return 返回的InputStream
	 */
	InputStream loadReport(String file);

	/**
	 * 根据报表名加载报表文件信息
	 *
	 * @param file 报表名称
	 * @return 返回的InputStream
	 */
	default ReportFile loadReportInfo(String file) {
		return Optional.ofNullable(this.getReportFiles())
				.orElse(Collections.emptyList())
				.stream()
				.filter(d -> Objects.equals(d.getFileName(), file))
				.findFirst()
				.orElse(null);
	}

	/**
	 * 根据报表名，删除指定的报表文件
	 *
	 * @param file 报表名称
	 */
	void deleteReport(String file);

	/**
	 * 获取所有的报表文件
	 *
	 * @return 返回报表文件列表
	 */
	List<ReportFile> getReportFiles();

	/**
	 * 保存报表文件
	 *
	 * @param file    报表名称
	 * @param content 报表的XML内容
	 */
	void saveReport(String file, String content);

	/**
	 * @return 返回存储器名称
	 */
	String getName();

	/**
	 * @return 返回是否禁用
	 */
	boolean disabled();

	/**
	 * @return 返回报表文件名前缀
	 */
	String getPrefix();
}
