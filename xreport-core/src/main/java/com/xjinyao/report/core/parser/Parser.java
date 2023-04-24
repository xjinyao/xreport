package com.xjinyao.report.core.parser;

import org.dom4j.Element;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface Parser<T> {
	T parse(Element element);
}
