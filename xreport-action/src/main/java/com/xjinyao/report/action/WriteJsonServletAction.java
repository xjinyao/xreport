package com.xjinyao.report.action;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;


/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public abstract class WriteJsonServletAction extends BaseServletAction {
	protected void writeObjectToJson(HttpServletResponse resp, Object obj) throws ServletException, IOException {
		resp.setContentType("text/json");
		resp.setCharacterEncoding("UTF-8");
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Inclusion.NON_NULL);
		mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		OutputStream out = resp.getOutputStream();
		try {
			mapper.writeValue(out, obj);
		} finally {
			out.flush();
			out.close();
		}
	}
}
