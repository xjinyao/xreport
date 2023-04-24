package com.xjinyao.report.core.expression.parse.builder;

import com.xjinyao.report.core.expression.model.expr.BaseExpression;
import com.xjinyao.report.core.expression.model.expr.BooleanExpression;
import com.xjinyao.report.core.dsl.ReportParserParser;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class BooleanExpressionBuilder implements ExpressionBuilder {
	@Override
	public BaseExpression build(ReportParserParser.UnitContext unitContext) {
		String text = unitContext.BOOLEAN().getText();
		return new BooleanExpression(Boolean.valueOf(text));
	}

	@Override
	public boolean support(ReportParserParser.UnitContext unitContext) {
		return unitContext.BOOLEAN() != null;
	}
}
