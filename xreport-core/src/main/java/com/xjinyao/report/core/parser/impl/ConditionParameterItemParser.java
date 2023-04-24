package com.xjinyao.report.core.parser.impl;

import com.xjinyao.report.core.definition.ConditionCellStyle;
import com.xjinyao.report.core.definition.ConditionPaging;
import com.xjinyao.report.core.definition.ConditionPropertyItem;
import com.xjinyao.report.core.definition.LinkParameter;
import com.xjinyao.report.core.expression.ExpressionUtils;
import com.xjinyao.report.core.expression.model.Condition;
import com.xjinyao.report.core.expression.model.Expression;
import com.xjinyao.report.core.expression.model.Op;
import com.xjinyao.report.core.expression.model.condition.BaseCondition;
import com.xjinyao.report.core.expression.model.condition.BothExpressionCondition;
import com.xjinyao.report.core.expression.model.condition.Join;
import com.xjinyao.report.core.expression.model.condition.PropertyExpressionCondition;
import com.xjinyao.report.core.parser.Parser;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ConditionParameterItemParser implements Parser<ConditionPropertyItem> {
	private Map<String, Parser<?>> parsers = new HashMap<>();

	public ConditionParameterItemParser() {
		parsers.put("cell-style", new CellStyleParser());
		parsers.put("link-parameter", new LinkParameterParser());
		parsers.put("paging", new ConditionPagingParser());
	}

	@Override
	public ConditionPropertyItem parse(Element element) {
		ConditionPropertyItem item = new ConditionPropertyItem();
		String rowHeight = element.attributeValue("row-height");
		if (StringUtils.isNotBlank(rowHeight)) {
			item.setRowHeight(Integer.valueOf(rowHeight));
		}
		String colWidth = element.attributeValue("col-width");
		if (StringUtils.isNotBlank(colWidth)) {
			item.setColWidth(Integer.valueOf(colWidth));
		}
		item.setName(element.attributeValue("name"));
		item.setNewValue(element.attributeValue("new-value"));
		item.setLinkUrl(element.attributeValue("link-url"));
		item.setLinkTargetWindow(element.attributeValue("link-target-window"));
		List<LinkParameter> parameters = null;
		List<Condition> conditions = new ArrayList<>();
		item.setConditions(conditions);
		BaseCondition topCondition = null;
		BaseCondition prevCondition = null;
		for (Object obj : element.elements()) {
			if (obj == null || !(obj instanceof Element)) {
				continue;
			}
			Element ele = (Element) obj;
			String name = ele.getName();
			if (name.equals("condition")) {
				BaseCondition condition = parseCondition(ele);
				conditions.add(condition);
				if (topCondition == null) {
					topCondition = condition;
					prevCondition = condition;
				} else {
					prevCondition.setNextCondition(condition);
					prevCondition.setJoin(condition.getJoin());
					prevCondition = condition;
				}
				continue;
			}
			Parser<?> parser = parsers.get(name);
			if (parser == null) {
				continue;
			}
			Object data = parser.parse(ele);
			if (data instanceof ConditionCellStyle) {
				item.setCellStyle((ConditionCellStyle) data);
			} else if (data instanceof LinkParameter) {
				if (parameters == null) {
					parameters = new ArrayList<>();
				}
				parameters.add((LinkParameter) data);
			} else if (data instanceof ConditionPaging) {
				item.setPaging((ConditionPaging) data);
			}
		}
		item.setCondition(topCondition);
		item.setLinkParameters(parameters);
		return item;
	}

	private BaseCondition parseCondition(Element ele) {
		String type = ele.attributeValue("type");
		String operation = ele.attributeValue("op");
		if (type == null || type.equals("property")) {
			PropertyExpressionCondition condition = new PropertyExpressionCondition();
			condition.parseCondition(ele);
			return condition;
		} else {
			BothExpressionCondition exprCondition = new BothExpressionCondition();
			exprCondition.setOp(Op.parse(operation));
			exprCondition.setOperation(operation);
			for (Object o : ele.elements()) {
				if (o == null || !(o instanceof Element)) {
					continue;
				}
				Element e = (Element) o;
				String name = e.getName();
				if (name.equals("left")) {
					String left = e.getText();
					Expression leftExpr = ExpressionUtils.parseExpression(left);
					exprCondition.setLeft(left);
					exprCondition.setLeftExpression(leftExpr);
				} else if (name.equals("right")) {
					String right = e.getText();
					Expression rightExpr = ExpressionUtils.parseExpression(right);
					exprCondition.setRight(right);
					exprCondition.setRightExpression(rightExpr);
				}
			}
			String join = ele.attributeValue("join");
			if (StringUtils.isNotBlank(join)) {
				exprCondition.setJoin(Join.valueOf(join));
			}
			return exprCondition;
		}
	}
}
