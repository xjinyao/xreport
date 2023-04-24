package com.xjinyao.report.core.parser.impl.searchform;

import com.xjinyao.report.core.parser.Parser;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface FormParser<T> extends Parser<T> {
	boolean support(String name);
}
