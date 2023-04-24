package com.xjinyao.report.core.expression.parse.builder;

import com.xjinyao.report.core.expression.model.expr.BaseExpression;
import com.xjinyao.report.core.expression.model.expr.CurrentCellDataExpression;
import com.xjinyao.report.core.dsl.ReportParserParser;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class CurrentCellDataExpressionBuilder implements ExpressionBuilder {

	@Override
	public BaseExpression build(ReportParserParser.UnitContext unitContext) {
		ReportParserParser.CurrentCellDataContext context = unitContext.currentCellData();
		CurrentCellDataExpression expr = new CurrentCellDataExpression();
		expr.setProperty(context.property().getText());
		return expr;
	}

	@Override
	public boolean support(ReportParserParser.UnitContext unitContext) {
		return unitContext.currentCellData() != null;
	}

}
