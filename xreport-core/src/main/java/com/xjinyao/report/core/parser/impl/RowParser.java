package com.xjinyao.report.core.parser.impl;

import com.xjinyao.report.core.definition.Band;
import com.xjinyao.report.core.definition.RowDefinition;
import com.xjinyao.report.core.parser.Parser;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class RowParser implements Parser<RowDefinition> {
	@Override
	public RowDefinition parse(Element element) {
		RowDefinition row = new RowDefinition();
		row.setRowNumber(Integer.valueOf(element.attributeValue("row-number")));
		String height = element.attributeValue("height");
		if (StringUtils.isNotBlank(height)) {
			row.setHeight(Integer.valueOf(height));
		}
		String band = element.attributeValue("band");
		if (StringUtils.isNotBlank(band)) {
			row.setBand(Band.valueOf(band));
		}
		return row;
	}
}
