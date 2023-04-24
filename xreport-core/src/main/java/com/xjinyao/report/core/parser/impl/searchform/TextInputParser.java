package com.xjinyao.report.core.parser.impl.searchform;

import com.xjinyao.report.core.definition.searchform.LabelPosition;
import com.xjinyao.report.core.definition.searchform.TextInputComponent;
import org.dom4j.Element;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class TextInputParser implements FormParser<TextInputComponent> {
	@Override
	public TextInputComponent parse(Element element) {
		TextInputComponent component = new TextInputComponent();
		component.setBindParameter(element.attributeValue("bind-parameter"));
		component.setLabel(element.attributeValue("label"));
		component.setType(element.attributeValue("type"));
		component.setLabelPosition(LabelPosition.valueOf(element.attributeValue("label-position")));
		return component;
	}

	@Override
	public boolean support(String name) {
		return name.equals("input-text");
	}
}
