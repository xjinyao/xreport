package com.xjinyao.report.core.build.cell;

import com.xjinyao.report.core.build.BindData;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.model.Cell;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface CellBuilder {
	Cell buildCell(List<BindData> dataList, Cell cell, Context context);
}
