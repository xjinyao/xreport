package com.xjinyao.report.core.provider.image;

import java.io.InputStream;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface ImageProvider {
	InputStream getImage(String path);

	boolean support(String path);
}
