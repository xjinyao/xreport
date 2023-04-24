package com.xjinyao.report.core.export;

import java.io.OutputStream;
import java.util.Map;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ExportConfigureImpl implements ExportConfigure {
	private String file;
	private OutputStream outputStream;
	private Map<String, Object> parameters;

	public ExportConfigureImpl(String file, Map<String, Object> parameters, OutputStream outputStream) {
		this.file = file;
		this.parameters = parameters;
		this.outputStream = outputStream;
	}

	@Override
	public OutputStream getOutputStream() {
		return outputStream;
	}

	@Override
	public Map<String, Object> getParameters() {
		return parameters;
	}

	@Override
	public String getFile() {
		return file;
	}
}
