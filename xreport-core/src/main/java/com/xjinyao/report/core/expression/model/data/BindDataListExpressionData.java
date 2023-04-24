package com.xjinyao.report.core.expression.model.data;

import com.xjinyao.report.core.build.BindData;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class BindDataListExpressionData implements ExpressionData<List<BindData>> {
	private List<BindData> list;

	public BindDataListExpressionData(List<BindData> list) {
		this.list = list;
	}

	@Override
	public List<BindData> getData() {
		return list;
	}
}
