package com.xjinyao.report.core.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ResourceCache {
	private static Map<String, Object> map = new HashMap<>();

	public static void putObject(String key, Object obj) {
		if (map.containsKey(key)) {
			map.remove(key);
		}
		map.put(key, obj);
	}

	public static Object getObject(String key) {
		return map.get(key);
	}
}
