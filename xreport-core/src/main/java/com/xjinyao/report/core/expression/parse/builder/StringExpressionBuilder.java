package com.xjinyao.report.core.expression.parse.builder;

import com.xjinyao.report.core.expression.model.expr.BaseExpression;
import com.xjinyao.report.core.expression.model.expr.StringExpression;
import com.xjinyao.report.core.dsl.ReportParserParser;


/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class StringExpressionBuilder implements ExpressionBuilder {
	@Override
	public BaseExpression build(ReportParserParser.UnitContext unitContext) {
		String text = unitContext.STRING().getText();
		text = text.substring(1, text.length() - 1);
		StringExpression stringExpr = new StringExpression(text);
		return stringExpr;
	}

	@Override
	public boolean support(ReportParserParser.UnitContext unitContext) {
		return unitContext.STRING() != null;
	}
}
