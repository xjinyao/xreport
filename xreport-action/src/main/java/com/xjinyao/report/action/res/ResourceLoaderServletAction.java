package com.xjinyao.report.action.res;

import com.xjinyao.report.action.ServletAction;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ResourceLoaderServletAction implements ServletAction, ApplicationContextAware {
	public static final String URL = "/res";
	private ApplicationContext applicationContext;

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getContextPath() + ROOT_URL + URL;
		String uri = req.getRequestURI();
		String resPath = uri.substring(path.length() + 1);
		String p = "classpath:" + resPath;
		if (p.endsWith(".js")) {
			resp.setContentType("text/javascript");
		} else if (p.endsWith(".css")) {
			resp.setContentType("text/css");
		} else if (p.endsWith(".png")) {
			resp.setContentType("image/png");
		} else if (p.endsWith(".jpg")) {
			resp.setContentType("image/jpeg");
		} else if (p.endsWith(".svg")) {
			resp.setContentType("image/svg+xml");
		} else {
			resp.setContentType("application/octet-stream");
		}
		InputStream input = applicationContext.getResource(p).getInputStream();
		OutputStream output = resp.getOutputStream();
		try {
			IOUtils.copy(input, output);
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public String url() {
		return URL;
	}
}
