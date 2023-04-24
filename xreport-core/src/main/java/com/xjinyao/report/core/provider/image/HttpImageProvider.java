package com.xjinyao.report.core.provider.image;

import com.xjinyao.report.core.exception.ReportException;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class HttpImageProvider implements ImageProvider {

	@Override
	public InputStream getImage(String path) {
		try {
			URL url = new URL(path);
			URLConnection connection = url.openConnection();
			connection.connect();
			InputStream inputStream = connection.getInputStream();
			return inputStream;
		} catch (Exception ex) {
			throw new ReportException(ex);
		}
	}

	@Override
	public boolean support(String path) {
		return path.startsWith("http:");
	}

}
