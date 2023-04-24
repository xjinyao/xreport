package com.xjinyao.report.action.image;

import com.xjinyao.report.action.ServletAction;
import com.xjinyao.report.core.cache.ResourceCache;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ImageServletAction implements ServletAction {
	public static final String URL = "/image";

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String key = req.getParameter("_key");
		if (StringUtils.isNotBlank(key)) {
			byte[] bytes = (byte[]) ResourceCache.getObject(key);
			InputStream input = new ByteArrayInputStream(bytes);
			OutputStream output = resp.getOutputStream();
			resp.setContentType("image/png");
			try {
				IOUtils.copy(input, output);
			} finally {
				IOUtils.closeQuietly(input);
				IOUtils.closeQuietly(output);
			}
		} else {
			//processImage(req, resp);			
		}
	}

	@Override
	public String url() {
		return URL;
	}
}
