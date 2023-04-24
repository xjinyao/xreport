package com.xjinyao.report.core.build.compute;

import com.xjinyao.report.core.build.BindData;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.definition.value.ValueType;
import com.xjinyao.report.core.model.Cell;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface ValueCompute {
	List<BindData> compute(Cell cell, Context context);

	ValueType type();
}
