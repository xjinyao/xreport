package com.xjinyao.report.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface ServletAction {

	String ROOT_URL = "/xreport";

	String PREVIEW_KEY = "p";

	void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

	String url();
}
