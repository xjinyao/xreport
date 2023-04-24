package com.xjinyao.report.core.parser.impl;

import com.xjinyao.report.core.definition.ConditionPaging;
import com.xjinyao.report.core.definition.PagingPosition;
import com.xjinyao.report.core.parser.Parser;
import org.dom4j.Element;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ConditionPagingParser implements Parser<ConditionPaging> {
	@Override
	public ConditionPaging parse(Element element) {
		ConditionPaging paging = new ConditionPaging();
		String position = element.attributeValue("position");
		paging.setPosition(PagingPosition.valueOf(position));
		String line = element.attributeValue("line");
		paging.setLine(Integer.valueOf(line));
		return paging;
	}
}
