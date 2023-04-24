package com.xjinyao.report.core.parser.impl;

import com.xjinyao.report.core.definition.LinkParameter;
import com.xjinyao.report.core.expression.ExpressionUtils;
import com.xjinyao.report.core.expression.model.Expression;
import com.xjinyao.report.core.parser.Parser;
import org.dom4j.Element;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class LinkParameterParser implements Parser<LinkParameter> {
	@Override
	public LinkParameter parse(Element element) {
		LinkParameter param = new LinkParameter();
		param.setName(element.attributeValue("name"));
		for (Object obj : element.elements()) {
			if (obj == null || !(obj instanceof Element)) {
				continue;
			}
			Element ele = (Element) obj;
			if (ele.getName().equals("value")) {
				param.setValue(ele.getText());
				Expression expr = ExpressionUtils.parseExpression(ele.getText());
				param.setValueExpression(expr);
				;
				break;
			}
		}
		return param;
	}
}
