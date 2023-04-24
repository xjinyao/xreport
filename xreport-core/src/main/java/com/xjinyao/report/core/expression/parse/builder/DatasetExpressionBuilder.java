package com.xjinyao.report.core.expression.parse.builder;

import com.xjinyao.report.core.definition.Order;
import com.xjinyao.report.core.definition.value.AggregateType;
import com.xjinyao.report.core.expression.model.condition.BaseCondition;
import com.xjinyao.report.core.expression.model.expr.BaseExpression;
import com.xjinyao.report.core.expression.model.expr.dataset.DatasetExpression;
import com.xjinyao.report.core.dsl.ReportParserParser;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class DatasetExpressionBuilder extends BaseExpressionBuilder {
	@Override
	public BaseExpression build(ReportParserParser.UnitContext unitContext) {
		ReportParserParser.DatasetContext context = (ReportParserParser.DatasetContext) unitContext.dataset();
		DatasetExpression expr = new DatasetExpression();
		expr.setExpr(context.getText());
		expr.setDatasetName(context.Identifier().getText());
		expr.setAggregate(AggregateType.valueOf(context.aggregate().getText()));
		if (context.property() != null) {
			expr.setProperty(context.property().getText());
		}
		ReportParserParser.ConditionsContext conditionsContext = context.conditions();
		if (conditionsContext != null) {
			BaseCondition condition = buildConditions(conditionsContext);
			expr.setCondition(condition);
		}
		TerminalNode orderNode = context.ORDER();
		if (orderNode != null) {
			expr.setOrder(Order.valueOf(orderNode.getText()));
		}
		return expr;
	}

	@Override
	public boolean support(ReportParserParser.UnitContext unitContext) {
		return unitContext.dataset() != null;
	}
}
