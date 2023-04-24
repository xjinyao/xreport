package com.xjinyao.report.action.chart;

import com.xjinyao.report.action.RenderPageServletAction;
import com.xjinyao.report.core.cache.CacheUtils;
import com.xjinyao.report.core.chart.ChartData;
import com.xjinyao.report.core.utils.UnitUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ChartServletAction extends RenderPageServletAction {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = retriveMethod(req);
		if (method != null) {
			invokeMethod(method, req, resp);
		}
	}

	public void storeData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String file = req.getParameter("_u");
		file = decode(file);
		String chartId = req.getParameter("_chartId");
		ChartData chartData = CacheUtils.getChartData(chartId);
		if (chartData == null) {
			return;
		}
		String base64Data = req.getParameter("_base64Data");
		String prefix = "data:image/png;base64,";
		if (base64Data != null) {
			if (base64Data.startsWith(prefix)) {
				base64Data = base64Data.substring(prefix.length(), base64Data.length());
			}
		}
		chartData.setBase64Data(base64Data);
		String width = req.getParameter("_width");
		String height = req.getParameter("_height");
		chartData.setHeight(UnitUtils.pixelToPoint(Integer.valueOf(height)));
		chartData.setWidth(UnitUtils.pixelToPoint(Integer.valueOf(width)));
	}

	@Override
	public String url() {
		return "/chart";
	}
}
