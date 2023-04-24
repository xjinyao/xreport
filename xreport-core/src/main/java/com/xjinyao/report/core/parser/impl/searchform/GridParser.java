package com.xjinyao.report.core.parser.impl.searchform;

import com.xjinyao.report.core.definition.searchform.ColComponent;
import com.xjinyao.report.core.definition.searchform.Component;
import com.xjinyao.report.core.definition.searchform.GridComponent;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class GridParser implements FormParser<GridComponent> {
	@Override
	public GridComponent parse(Element element) {
		GridComponent grid = new GridComponent();
		grid.setType(element.attributeValue("type"));
		grid.setShowBorder(Boolean.valueOf(element.attributeValue("show-border")));
		if (grid.isShowBorder()) {
			grid.setBorderColor(element.attributeValue("border-color"));
			grid.setBorderWidth(Integer.valueOf(element.attributeValue("border-width")));
		}
		List<ColComponent> cols = new ArrayList<>();
		for (Object obj : element.elements()) {
			if (obj == null || !(obj instanceof Element)) {
				continue;
			}
			Element ele = (Element) obj;
			if (!ele.getName().equals("col")) {
				continue;
			}
			ColComponent col = new ColComponent();
			cols.add(col);
			col.setSize(Integer.valueOf(ele.attributeValue("size")));
			List<Component> components = FormParserUtils.parse(ele);
			col.setChildren(components);
		}
		grid.setCols(cols);
		return grid;
	}

	@Override
	public boolean support(String name) {
		return name.equals("grid");
	}
}
