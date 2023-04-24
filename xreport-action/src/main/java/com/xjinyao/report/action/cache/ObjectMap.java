package com.xjinyao.report.action.cache;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ObjectMap {
	private static final int MILLISECOND = 300000;//default expired time is 5 minutes.
	private final int MAX_ITEM = 3;
	private Map<String, Object> map = new LinkedHashMap<>();
	private long start;

	public ObjectMap() {
		this.start = System.currentTimeMillis();
	}

	public void put(String key, Object obj) {
		this.start = System.currentTimeMillis();
		if (map.containsKey(key)) {
			map.remove(key);
		} else {
			if (map.size() > MAX_ITEM) {
				String lastFile = null;
				for (Iterator<Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext(); ) {
					Entry<String, Object> entry = it.next();
					lastFile = entry.getKey();
				}
				map.remove(lastFile);
			}
		}
		map.put(key, obj);
	}

	public Object get(String key) {
		this.start = System.currentTimeMillis();
		return this.map.get(key);
	}

	public void remove(String key) {
		this.map.remove(key);
	}

	public boolean isExpired() {
		long end = System.currentTimeMillis();
		long value = end - start;
		if (value >= MILLISECOND) {
			return true;
		}
		return false;
	}
}
