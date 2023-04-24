package com.xjinyao.report.core.expression.model.data;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ObjectExpressionData implements ExpressionData<Object> {
	private Object data;

	public ObjectExpressionData(Object data) {
		this.data = data;
	}

	@Override
	public Object getData() {
		return data;
	}
}
