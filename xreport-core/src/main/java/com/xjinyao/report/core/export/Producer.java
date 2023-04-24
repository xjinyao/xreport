package com.xjinyao.report.core.export;

import com.xjinyao.report.core.model.Report;

import java.io.OutputStream;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface Producer {
	void produce(Report report, OutputStream outputStream);
}
