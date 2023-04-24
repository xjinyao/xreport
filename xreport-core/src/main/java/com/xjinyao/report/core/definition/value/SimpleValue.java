package com.xjinyao.report.core.definition.value;

/**
 * 普通字符串，或者是表达式
 *
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class SimpleValue implements Value {
	private String value;

	public SimpleValue(String value) {
		this.value = value;
	}

	@Override
	public ValueType getType() {
		return ValueType.simple;
	}

	@Override
	public String getValue() {
		return value;
	}
}
