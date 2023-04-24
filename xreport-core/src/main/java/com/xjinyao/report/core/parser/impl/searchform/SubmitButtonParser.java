package com.xjinyao.report.core.parser.impl.searchform;

import com.xjinyao.report.core.definition.searchform.Align;
import com.xjinyao.report.core.definition.searchform.SubmitButtonComponent;
import org.dom4j.Element;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class SubmitButtonParser implements FormParser<SubmitButtonComponent> {
	@Override
	public SubmitButtonComponent parse(Element element) {
		SubmitButtonComponent btn = new SubmitButtonComponent();
		btn.setLabel(element.attributeValue("label"));
		btn.setStyle(element.attributeValue("style"));
		btn.setType(element.attributeValue("type"));
		String align = element.attributeValue("align");
		if (align != null) {
			btn.setAlign(Align.valueOf(align));
		}
		return btn;
	}

	@Override
	public boolean support(String name) {
		return name.equals("button-submit");
	}
}
