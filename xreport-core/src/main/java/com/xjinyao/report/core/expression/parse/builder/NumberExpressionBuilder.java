package com.xjinyao.report.core.expression.parse.builder;

import com.xjinyao.report.core.Utils;
import com.xjinyao.report.core.expression.model.expr.BaseExpression;
import com.xjinyao.report.core.expression.model.expr.NumberExpression;
import com.xjinyao.report.core.dsl.ReportParserParser;

import java.math.BigDecimal;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class NumberExpressionBuilder implements ExpressionBuilder {
	@Override
	public BaseExpression build(ReportParserParser.UnitContext unitContext) {
		BigDecimal value = Utils.toBigDecimal(unitContext.NUMBER().getText());
		return new NumberExpression(value);
	}

	@Override
	public boolean support(ReportParserParser.UnitContext unitContext) {
		return unitContext.NUMBER() != null;
	}
}
