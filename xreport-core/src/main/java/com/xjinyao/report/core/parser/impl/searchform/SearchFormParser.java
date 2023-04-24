package com.xjinyao.report.core.parser.impl.searchform;

import com.xjinyao.report.core.definition.searchform.Component;
import com.xjinyao.report.core.definition.searchform.FormPosition;
import com.xjinyao.report.core.definition.searchform.SearchForm;
import com.xjinyao.report.core.parser.Parser;
import org.dom4j.Element;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class SearchFormParser implements Parser<SearchForm> {
	@Override
	public SearchForm parse(Element element) {
		SearchForm form = new SearchForm();
		form.setFormPosition(FormPosition.valueOf(element.attributeValue("form-position")));
		List<Component> components = FormParserUtils.parse(element);
		form.setComponents(components);
		return form;
	}
}
