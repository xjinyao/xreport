package com.xjinyao.report.core.model;

import com.xjinyao.report.core.cache.ResourceCache;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class Resource {
	private String key;

	public Resource(String path) {
		this.key = path;
	}

	public InputStream getResourceData() {
		byte[] imageBytes = (byte[]) ResourceCache.getObject(key);
		InputStream inputStream = new ByteArrayInputStream(imageBytes);
		return inputStream;
	}

	public String getKey() {
		return key;
	}
}
