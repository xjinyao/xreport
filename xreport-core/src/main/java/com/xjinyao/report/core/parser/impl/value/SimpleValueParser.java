package com.xjinyao.report.core.parser.impl.value;

import com.xjinyao.report.core.definition.value.SimpleValue;
import com.xjinyao.report.core.definition.value.Value;
import org.dom4j.Element;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class SimpleValueParser extends ValueParser {
	@Override
	public Value parse(Element element) {
		SimpleValue simpleValue = new SimpleValue(element.getText());
		return simpleValue;
	}
}
