package com.xjinyao.report.action.designer;

import com.xjinyao.report.action.RenderPageServletAction;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class SearchFormDesignerAction extends RenderPageServletAction {

	@Setter
	private String gatewayUrlPrefix;

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		VelocityContext context = new VelocityContext();
		String contextPath = req.getContextPath();
		String basePath = StringUtils.isNotBlank(gatewayUrlPrefix) ? (gatewayUrlPrefix + contextPath) : contextPath;
		context.put("contextPath", "/".equals(basePath) ? "" : basePath);
		resp.setContentType("text/html");
		resp.setCharacterEncoding("utf-8");
		Template template = ve.getTemplate("xreport-html/searchform.html", "utf-8");
		PrintWriter writer = resp.getWriter();
		template.merge(context, writer);
		writer.close();
	}

	@Override
	public String url() {
		return "/searchFormDesigner";
	}
}
