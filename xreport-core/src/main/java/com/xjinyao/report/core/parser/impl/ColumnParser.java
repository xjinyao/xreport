package com.xjinyao.report.core.parser.impl;

import com.xjinyao.report.core.definition.ColumnDefinition;
import com.xjinyao.report.core.parser.Parser;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ColumnParser implements Parser<ColumnDefinition> {
	@Override
	public ColumnDefinition parse(Element element) {
		ColumnDefinition col = new ColumnDefinition();
		col.setColumnNumber(Integer.valueOf(element.attributeValue("col-number")));
		String hide = element.attributeValue("hide");
		if (StringUtils.isNotBlank(hide)) {
			col.setHide(Boolean.valueOf(hide));
		}
		String width = element.attributeValue("width");
		if (StringUtils.isNotBlank(width)) {
			col.setWidth(Integer.valueOf(width));
		}
		return col;
	}
}
