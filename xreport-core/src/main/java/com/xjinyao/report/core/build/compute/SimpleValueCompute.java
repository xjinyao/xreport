package com.xjinyao.report.core.build.compute;

import com.xjinyao.report.core.build.BindData;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.definition.value.ValueType;
import com.xjinyao.report.core.model.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class SimpleValueCompute implements ValueCompute {

	@Override
	public List<BindData> compute(Cell cell, Context context) {
		List<BindData> list = new ArrayList<>();
		list.add(new BindData(cell.getValue().getValue(), null, null));
		return list;
	}

	@Override
	public ValueType type() {
		return ValueType.simple;
	}
}
