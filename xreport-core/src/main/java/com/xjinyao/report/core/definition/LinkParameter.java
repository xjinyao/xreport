package com.xjinyao.report.core.definition;

import com.xjinyao.report.core.expression.model.Expression;

import java.io.Serializable;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class LinkParameter implements Serializable {
	private static final long serialVersionUID = -5156733452111427492L;
	private String name;
	private String value;
	private Expression valueExpression;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Expression getValueExpression() {
		return valueExpression;
	}

	public void setValueExpression(Expression valueExpression) {
		this.valueExpression = valueExpression;
	}
}
