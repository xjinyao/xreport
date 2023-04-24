package com.xjinyao.report.action;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public abstract class BaseServletAction implements ServletAction {
	protected Throwable buildRootException(Throwable throwable) {
		if (throwable.getCause() == null) {
			return throwable;
		}
		return buildRootException(throwable.getCause());
	}

	protected String decode(String value) {
		if (value == null) {
			return value;
		}
		try {
			value = URLDecoder.decode(value, "utf-8");
			value = URLDecoder.decode(value, "utf-8");
			return value;
		} catch (Exception ex) {
			return value;
		}
	}

	protected String decodeContent(String content) {
		if (content == null) {
			return content;
		}
		try {
			content = URLDecoder.decode(content, "utf-8");
			return content;
		} catch (Exception ex) {
			return content;
		}
	}

	protected Map<String, Object> buildParameters(HttpServletRequest req) {
		Map<String, Object> parameters = new HashMap<>();
		Enumeration<?> enumeration = req.getParameterNames();
		while (enumeration.hasMoreElements()) {
			Object obj = enumeration.nextElement();
			if (obj == null) {
				continue;
			}
			String name = obj.toString();
			String value = req.getParameter(name);
			if (name == null || value == null || name.startsWith("_")) {
				continue;
			}
			parameters.put(name, decode(value));
		}
		return parameters;
	}

	protected void invokeMethod(String methodName, HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		try {
			Method method = this.getClass().getMethod(methodName, new Class<?>[]{HttpServletRequest.class, HttpServletResponse.class});
			method.invoke(this, new Object[]{req, resp});
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}

	protected String retriveMethod(HttpServletRequest req) throws ServletException {
		String path = req.getContextPath() + ROOT_URL;
		String uri = req.getRequestURI();
		String targetUrl = uri.substring(path.length());
		int slashPos = targetUrl.indexOf("/", 1);
		if (slashPos > -1) {
			String methodName = targetUrl.substring(slashPos + 1).trim();
			return methodName.length() > 0 ? methodName : null;
		}
		return null;
	}

	protected String buildDownloadFileName(String reportFileName, String fileName, String extName) {
		if (StringUtils.isNotBlank(fileName)) {
			fileName = decode(fileName);
			if (!fileName.toLowerCase().endsWith(extName)) {
				fileName = fileName + extName;
			}
			return fileName;
		} else {
			int pos = reportFileName.indexOf(":");
			if (pos > 0) {
				reportFileName = reportFileName.substring(pos + 1, reportFileName.length());
			}
			pos = reportFileName.toLowerCase().indexOf(".xreport.xml");
			if (pos > 0) {
				reportFileName = reportFileName.substring(0, pos);
			}
			return "xreport-" + reportFileName + extName;
		}
	}
}
