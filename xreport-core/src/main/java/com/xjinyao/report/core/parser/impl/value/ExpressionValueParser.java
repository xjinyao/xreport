package com.xjinyao.report.core.parser.impl.value;

import com.xjinyao.report.core.definition.value.ExpressionValue;
import org.dom4j.Element;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ExpressionValueParser extends ValueParser {
	@Override
	public ExpressionValue parse(Element element) {
		ExpressionValue value = new ExpressionValue(element.getText());
		return value;
	}
}
