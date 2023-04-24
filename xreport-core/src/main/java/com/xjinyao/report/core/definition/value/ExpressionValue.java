package com.xjinyao.report.core.definition.value;

import com.xjinyao.report.core.expression.ExpressionUtils;
import com.xjinyao.report.core.expression.model.Expression;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ExpressionValue implements Value {
	private String text;
	private Expression expression;

	public ExpressionValue(String text) {
		this.text = text;
		expression = ExpressionUtils.parseExpression(text);
	}

	@Override
	public ValueType getType() {
		return ValueType.expression;
	}

	@Override
	public String getValue() {
		return text;
	}

	public Expression getExpression() {
		return expression;
	}
}
