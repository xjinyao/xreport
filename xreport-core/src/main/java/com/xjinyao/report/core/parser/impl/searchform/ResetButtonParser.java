package com.xjinyao.report.core.parser.impl.searchform;

import com.xjinyao.report.core.definition.searchform.ResetButtonComponent;
import org.dom4j.Element;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ResetButtonParser implements FormParser<ResetButtonComponent> {
	@Override
	public ResetButtonComponent parse(Element element) {
		ResetButtonComponent btn = new ResetButtonComponent();
		btn.setLabel(element.attributeValue("label"));
		btn.setStyle(element.attributeValue("style"));
		btn.setType(element.attributeValue("type"));
		return btn;
	}

	@Override
	public boolean support(String name) {
		return name.equals("button-reset");
	}
}
