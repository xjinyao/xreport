package com.xjinyao.report.core.provider.image;

import com.xjinyao.report.core.exception.ReportException;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.net.URL;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class HttpsImageProvider implements ImageProvider {

	@Override
	public InputStream getImage(String path) {
		try {
			URL url = new URL(path);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.connect();
			InputStream inputStream = connection.getInputStream();
			return inputStream;
		} catch (Exception ex) {
			throw new ReportException(ex);
		}
	}

	@Override
	public boolean support(String path) {
		return path.startsWith("https:");
	}

}
