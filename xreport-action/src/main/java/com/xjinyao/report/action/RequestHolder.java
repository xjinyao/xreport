package com.xjinyao.report.action;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class RequestHolder {
	private static final ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();

	public static HttpServletRequest getRequest() {
		return requestThreadLocal.get();
	}

	public static void setRequest(HttpServletRequest request) {
		requestThreadLocal.set(request);
	}

	public static void clean() {
		requestThreadLocal.remove();
	}
}
