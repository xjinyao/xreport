package com.xjinyao.report.core.expression.model;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.model.Cell;

import java.io.Serializable;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface Expression extends Serializable {
	ExpressionData<?> execute(Cell cell, Cell currentCell, Context context);
}
