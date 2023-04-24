package com.xjinyao.report.core.expression.model.data;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ObjectListExpressionData implements ExpressionData<List<?>> {
	private List<?> list;

	public ObjectListExpressionData(List<?> list) {
		this.list = list;
	}

	@Override
	public List<?> getData() {
		return list;
	}
}
