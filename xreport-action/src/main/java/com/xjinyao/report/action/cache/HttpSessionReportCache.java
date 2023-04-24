package com.xjinyao.report.action.cache;

import com.xjinyao.report.action.RequestHolder;
import com.xjinyao.report.core.cache.ReportCache;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class HttpSessionReportCache implements ReportCache {
	private Map<String, ObjectMap> sessionReportMap = new HashMap<>();
	private boolean disabled;

	@Override
	public Object getObject(String file) {
		HttpServletRequest req = RequestHolder.getRequest();
		if (req == null) {
			return null;
		}
		ObjectMap objMap = getObjectMap(req);
		return objMap.get(file);
	}

	@Override
	public void storeObject(String file, Object object) {
		HttpServletRequest req = RequestHolder.getRequest();
		if (req == null) {
			return;
		}
		ObjectMap map = getObjectMap(req);
		map.put(file, object);
	}

	@Override
	public boolean disabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	private ObjectMap getObjectMap(HttpServletRequest req) {
		List<String> expiredList = new ArrayList<>();
		for (String key : sessionReportMap.keySet()) {
			ObjectMap reportObj = sessionReportMap.get(key);
			if (reportObj.isExpired()) {
				expiredList.add(key);
			}
		}
		for (String key : expiredList) {
			sessionReportMap.remove(key);
		}
		String sessionId = req.getSession().getId();
		ObjectMap obj = sessionReportMap.get(sessionId);
		if (obj != null) {
			return obj;
		} else {
			ObjectMap objMap = new ObjectMap();
			sessionReportMap.put(sessionId, objMap);
			return objMap;
		}
	}
}
