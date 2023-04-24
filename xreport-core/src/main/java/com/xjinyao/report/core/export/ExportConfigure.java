package com.xjinyao.report.core.export;

import java.io.OutputStream;
import java.util.Map;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface ExportConfigure {
	OutputStream getOutputStream();

	String getFile();

	Map<String, Object> getParameters();
}
