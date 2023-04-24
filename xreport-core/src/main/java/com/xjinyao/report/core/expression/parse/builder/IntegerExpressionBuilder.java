package com.xjinyao.report.core.expression.parse.builder;

import com.xjinyao.report.core.expression.model.expr.BaseExpression;
import com.xjinyao.report.core.expression.model.expr.IntegerExpression;
import com.xjinyao.report.core.dsl.ReportParserParser;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class IntegerExpressionBuilder implements ExpressionBuilder {
	@Override
	public BaseExpression build(ReportParserParser.UnitContext unitContext) {
		Integer value = null;
		if (unitContext.INTEGER() != null) {
			value = Integer.valueOf(unitContext.INTEGER().getText());
		}
		return new IntegerExpression(value);
	}

	@Override
	public boolean support(ReportParserParser.UnitContext unitContext) {
		return unitContext.INTEGER() != null;
	}

}
