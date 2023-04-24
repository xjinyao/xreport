package com.xjinyao.report.core.expression.parse.builder;

import com.xjinyao.report.core.expression.ExpressionUtils;
import com.xjinyao.report.core.expression.model.expr.BaseExpression;
import com.xjinyao.report.core.expression.model.expr.FunctionExpression;
import com.xjinyao.report.core.dsl.ReportParserParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class FunctionExpressionBuilder extends BaseExpressionBuilder {
	@Override
	public BaseExpression build(ReportParserParser.UnitContext unitContext) {
		ReportParserParser.FunctionContext ctx = unitContext.function();
		FunctionExpression expr = new FunctionExpression();
		expr.setExpr(ctx.getText());
		expr.setName(ctx.Identifier().getText());
		ReportParserParser.FunctionParameterContext functionParameterContext = ctx.functionParameter();
		if (functionParameterContext != null) {
			List<BaseExpression> exprList = new ArrayList<>();
			List<ReportParserParser.ItemContext> itemContexts = functionParameterContext.item();
			if (itemContexts != null) {
				for (int i = 0; i < itemContexts.size(); i++) {
					ReportParserParser.ItemContext itemContext = itemContexts.get(i);
					BaseExpression baseExpr = ExpressionUtils.getExprVisitor().parseItemContext(itemContext);
					exprList.add(baseExpr);
				}
			}
			expr.setExpressions(exprList);
		}
		return expr;
	}

	@Override
	public boolean support(ReportParserParser.UnitContext unitContext) {
		return unitContext.function() != null;
	}
}
