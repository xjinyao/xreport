package com.xjinyao.report.core.expression.parse.builder;

import com.xjinyao.report.core.expression.model.expr.BaseExpression;
import com.xjinyao.report.core.expression.model.expr.cell.CellObjectExpression;
import com.xjinyao.report.core.dsl.ReportParserParser;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class CellObjectExpressionBuilder implements ExpressionBuilder {

	@Override
	public BaseExpression build(ReportParserParser.UnitContext unitContext) {
		ReportParserParser.CellContext ctx = unitContext.cell();
		String property = null;
		ReportParserParser.PropertyContext propCtx = ctx.property();
		if (propCtx != null) {
			property = propCtx.getText();
		}
		CellObjectExpression expr = new CellObjectExpression(property);
		expr.setExpr(ctx.getText());
		return expr;
	}

	@Override
	public boolean support(ReportParserParser.UnitContext unitContext) {
		return unitContext.cell() != null;
	}
}
