package com.xjinyao.report.core.expression.model.condition;

import com.xjinyao.report.core.Utils;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.ExpressionUtils;
import com.xjinyao.report.core.expression.model.Expression;
import com.xjinyao.report.core.expression.model.Op;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.model.Cell;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.dom4j.Element;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class PropertyExpressionCondition extends BaseCondition {
	private ConditionType type = ConditionType.property;
	@JsonIgnore
	private String leftProperty;
	@JsonIgnore
	private Expression rightExpression;

	@Override
	Object computeLeft(Cell cell, Cell currentCell, Object obj, Context context) {
		if (StringUtils.isNotBlank(leftProperty)) {
			return Utils.getProperty(obj, leftProperty);
		} else {
			return cell.getData();
		}
	}

	@Override
	Object computeRight(Cell cell, Cell currentCell, Object obj, Context context) {
		ExpressionData<?> exprData = rightExpression.execute(cell, currentCell, context);
		return extractExpressionData(exprData);
	}

	@Override
	public ConditionType getType() {
		return type;
	}

	public String getLeftProperty() {
		return leftProperty;
	}

	public void setLeftProperty(String leftProperty) {
		this.leftProperty = leftProperty;
	}

	public Expression getRightExpression() {
		return rightExpression;
	}

	public void setRightExpression(Expression rightExpression) {
		this.rightExpression = rightExpression;
	}

	public PropertyExpressionCondition parseCondition(Element ele) {
		String property = ele.attributeValue("property");
		String operation = ele.attributeValue("op");
		this.setLeftProperty(property);
		this.setLeft(property);
		this.setOperation(operation);
		this.setOp(Op.parse(operation));
		for (Object o : ele.elements()) {
			if (o == null || !(o instanceof Element)) {
				continue;
			}
			Element e = (Element) o;
			if (!e.getName().equals("value")) {
				continue;
			}
			String expr = e.getTextTrim();
			this.setRightExpression(ExpressionUtils.parseExpression(expr));
			this.setRight(expr);
			break;
		}
		String join = ele.attributeValue("join");
		if (StringUtils.isNotBlank(join)) {
			this.setJoin(Join.valueOf(join));
		}
		return this;
	}
}
