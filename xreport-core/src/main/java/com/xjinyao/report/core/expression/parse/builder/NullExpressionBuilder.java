package com.xjinyao.report.core.expression.parse.builder;

import com.xjinyao.report.core.expression.model.expr.BaseExpression;
import com.xjinyao.report.core.expression.model.expr.NullExpression;
import com.xjinyao.report.core.dsl.ReportParserParser;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class NullExpressionBuilder implements ExpressionBuilder {
	@Override
	public BaseExpression build(ReportParserParser.UnitContext unitContext) {
		return new NullExpression();
	}

	@Override
	public boolean support(ReportParserParser.UnitContext unitContext) {
		return unitContext.NULL() != null;
	}
}
