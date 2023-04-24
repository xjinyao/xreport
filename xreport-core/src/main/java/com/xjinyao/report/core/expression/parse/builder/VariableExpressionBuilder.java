package com.xjinyao.report.core.expression.parse.builder;

import com.xjinyao.report.core.expression.model.expr.BaseExpression;
import com.xjinyao.report.core.expression.model.expr.VariableExpression;
import com.xjinyao.report.core.dsl.ReportParserParser;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class VariableExpressionBuilder implements ExpressionBuilder {
	@Override
	public BaseExpression build(ReportParserParser.UnitContext unitContext) {
		String text = unitContext.variable().Identifier().getText();
		VariableExpression varExpr = new VariableExpression(text);
		return varExpr;
	}

	@Override
	public boolean support(ReportParserParser.UnitContext unitContext) {
		return unitContext.variable() != null;
	}
}
