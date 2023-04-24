package com.xjinyao.report.core.image;

import java.io.InputStream;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface ImageProcessor<T> {
	InputStream getImage(T data);
}
