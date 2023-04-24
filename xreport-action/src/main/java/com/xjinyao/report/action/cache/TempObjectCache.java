package com.xjinyao.report.action.cache;

import com.xjinyao.report.action.RequestHolder;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class TempObjectCache {
	private static final TempObjectCache tempObjectCache = new TempObjectCache();
	private final Map<String, ObjectMap> cacheMap = new HashMap<>();

	public static Object getObject(String key) {
		return tempObjectCache.get(key);
	}

	public static void putObject(String key, Object obj) {
		tempObjectCache.store(key, obj);
	}

	public static void removeObject(String key) {
		tempObjectCache.remove(key);
	}

	public void remove(String key) {
		HttpServletRequest req = RequestHolder.getRequest();
		if (req == null) {
			return;
		}
		ObjectMap mapObject = getReportMap(req);
		if (mapObject != null) {
			mapObject.remove(key);
		}
	}

	public Object get(String key) {
		HttpServletRequest req = RequestHolder.getRequest();
		if (req == null) {
			return null;
		}
		ObjectMap mapObject = getReportMap(req);
		return mapObject.get(key);
	}

	public void store(String key, Object obj) {
		HttpServletRequest req = RequestHolder.getRequest();
		if (req == null) {
			return;
		}
		ObjectMap mapObject = getReportMap(req);
		mapObject.put(key, obj);
	}

	private ObjectMap getReportMap(HttpServletRequest req) {
		List<String> expiredList = new ArrayList<>();
		for (String key : cacheMap.keySet()) {
			ObjectMap reportObj = cacheMap.get(key);
			if (reportObj.isExpired()) {
				expiredList.add(key);
			}
		}
		for (String key : expiredList) {
			cacheMap.remove(key);
		}
		String id = req.getParameter("cid");
		String cacheId = StringUtils.defaultString(id, req.getSession().getId());
		ObjectMap obj = cacheMap.get(cacheId);
		if (obj != null) {
			return obj;
		} else {
			ObjectMap mapObject = new ObjectMap();
			cacheMap.put(cacheId, mapObject);
			return mapObject;
		}
	}
}
