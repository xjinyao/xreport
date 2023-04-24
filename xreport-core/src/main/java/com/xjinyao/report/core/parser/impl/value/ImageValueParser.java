package com.xjinyao.report.core.parser.impl.value;

import com.xjinyao.report.core.definition.value.ImageValue;
import com.xjinyao.report.core.definition.value.Source;
import com.xjinyao.report.core.definition.value.Value;
import com.xjinyao.report.core.expression.ExpressionUtils;
import com.xjinyao.report.core.expression.model.Expression;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ImageValueParser extends ValueParser {

	@Override
	public Value parse(Element element) {
		ImageValue value = new ImageValue();
		String width = element.attributeValue("width");
		if (StringUtils.isNotBlank(width)) {
			value.setWidth(Integer.valueOf(width));
		}
		String height = element.attributeValue("height");
		if (StringUtils.isNotBlank(height)) {
			value.setHeight(Integer.valueOf(height));
		}
		Source source = Source.valueOf(element.attributeValue("source"));
		value.setSource(source);
		for (Object obj : element.elements()) {
			if (obj == null || !(obj instanceof Element)) {
				continue;
			}
			Element ele = (Element) obj;
			if (ele.getName().equals("text")) {
				if (source.equals(Source.text)) {
					value.setPath(ele.getText());
				} else {
					value.setExpr(ele.getText());
				}
				break;
			}
		}
		if (source.equals(Source.expression)) {
			String expr = value.getExpr();
			Expression expression = ExpressionUtils.parseExpression(expr);
			value.setExpression(expression);
		}
		return value;
	}
}
