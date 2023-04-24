package com.xjinyao.report.core.expression.parse.builder;

import com.xjinyao.report.core.exception.ReportParseException;
import com.xjinyao.report.core.expression.model.condition.BaseCondition;
import com.xjinyao.report.core.expression.model.expr.BaseExpression;
import com.xjinyao.report.core.expression.model.expr.set.*;
import com.xjinyao.report.core.dsl.ReportParserParser;
import com.xjinyao.report.core.expression.model.expr.set.*;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class SetExpressionBuilder extends BaseExpressionBuilder {
	@Override
	public BaseExpression build(ReportParserParser.UnitContext unitContext) {
		ReportParserParser.SetContext context = unitContext.set();
		BaseExpression setExpr = buildSetExpression(context);
		setExpr.setExpr(context.getText());
		return setExpr;
	}

	public BaseExpression buildSetExpression(ReportParserParser.SetContext context) {
		if (context instanceof ReportParserParser.SingleCellContext) {
			TerminalNode cellNode = ((ReportParserParser.SingleCellContext) context).Cell();
			return new CellExpression(cellNode.getText());
		} else if (context instanceof ReportParserParser.WholeCellContext) {
			ReportParserParser.WholeCellContext ctx = (ReportParserParser.WholeCellContext) context;
			WholeCellExpression wholeCellExpression = new WholeCellExpression(ctx.Cell().getText());
			ReportParserParser.ConditionsContext conditionsContext = ctx.conditions();
			if (conditionsContext != null) {
				BaseCondition condition = buildConditions(conditionsContext);
				wholeCellExpression.setCondition(condition);
			}
			return wholeCellExpression;
		} else if (context instanceof ReportParserParser.SingleCellConditionContext) {
			ReportParserParser.SingleCellConditionContext ctx = (ReportParserParser.SingleCellConditionContext) context;
			BaseCondition condition = buildConditions(ctx.conditions());
			return new CellConditionExpression(ctx.Cell().getText(), condition);
		} else if (context instanceof ReportParserParser.CellPairContext) {
			ReportParserParser.CellPairContext pairContext = (ReportParserParser.CellPairContext) context;
			String startCellName = pairContext.Cell(0).getText();
			String endCellName = pairContext.Cell(1).getText();
			return new CellPairExpression(startCellName, endCellName);
		} else if (context instanceof ReportParserParser.SingleCellCoordinateContext) {
			ReportParserParser.SingleCellCoordinateContext ctx = (ReportParserParser.SingleCellCoordinateContext) context;
			String cellName = null;
			if (ctx.Cell() != null) {
				cellName = ctx.Cell().getText();
			}
			ReportParserParser.CellCoordinateContext cellCoordinateContext = ctx.cellCoordinate();
			List<ReportParserParser.CoordinateContext> coordinateContexts = cellCoordinateContext.coordinate();
			CellCoordinateSet leftCoordinate = parseCellCoordinateSet(coordinateContexts.get(0));
			CellCoordinateSet rightCoordinate = null;
			if (coordinateContexts.size() > 1) {
				rightCoordinate = parseCellCoordinateSet(coordinateContexts.get(1));
			}
			return new CellCoordinateExpression(cellName, leftCoordinate, rightCoordinate);
		} else if (context instanceof ReportParserParser.CellCoordinateConditionContext) {
			ReportParserParser.CellCoordinateConditionContext ctx = (ReportParserParser.CellCoordinateConditionContext) context;
			String cellName = null;
			if (ctx.Cell() != null) {
				cellName = ctx.Cell().getText();
			}
			ReportParserParser.CellCoordinateContext cellCoordinateContext = ctx.cellCoordinate();
			List<ReportParserParser.CoordinateContext> coordinateContexts = cellCoordinateContext.coordinate();
			CellCoordinateSet leftCoordinate = parseCellCoordinateSet(coordinateContexts.get(0));
			CellCoordinateSet rightCoordinate = null;
			if (coordinateContexts.size() > 1) {
				rightCoordinate = parseCellCoordinateSet(coordinateContexts.get(1));
			}
			BaseCondition condition = buildConditions(ctx.conditions());
			return new CellCoordinateExpression(cellName, leftCoordinate, rightCoordinate, condition);
		} else if (context instanceof ReportParserParser.RangeContext) {
			ReportParserParser.RangeContext ctx = (ReportParserParser.RangeContext) context;
			List<ReportParserParser.SetContext> sets = ctx.set();
			if (sets.size() != 2) {
				throw new ReportParseException("Range expression must have from and to expressions.");
			}
			BaseExpression fromExpr = buildSetExpression(sets.get(0));
			BaseExpression toExpr = buildSetExpression(sets.get(1));
			FromToExpression expr = new FromToExpression(fromExpr, toExpr);
			return expr;
		} else if (context instanceof ReportParserParser.SimpleDataContext) {
			ReportParserParser.SimpleDataContext ctx = (ReportParserParser.SimpleDataContext) context;
			ReportParserParser.SimpleValueContext valueContext = ctx.simpleValue();
			return parseSimpleValueContext(valueContext);
		}
		throw new ReportParseException("Unknow context : " + context);
	}

	private CellCoordinateSet parseCellCoordinateSet(ReportParserParser.CoordinateContext ctx) {
		List<CellCoordinate> coordinates = new ArrayList<>();
		for (ReportParserParser.CellIndicatorContext indicatorContext : ctx.cellIndicator()) {
			if (indicatorContext instanceof ReportParserParser.RelativeContext) {
				ReportParserParser.RelativeContext context = (ReportParserParser.RelativeContext) indicatorContext;
				String cellName = context.Cell().getText();
				CellCoordinate coordinate = new CellCoordinate(cellName, CoordinateType.relative);
				coordinates.add(coordinate);
			} else {
				ReportParserParser.AbsoluteContext context = (ReportParserParser.AbsoluteContext) indicatorContext;
				String cellName = context.Cell().getText();
				String pos = context.INTEGER().getText();
				int position = Integer.valueOf(pos);
				CellCoordinate coordinate = new CellCoordinate(cellName, CoordinateType.absolute);
				coordinate.setPosition(position);
				if (context.EXCLAMATION() != null) {
					coordinate.setReverse(true);
				}
				coordinates.add(coordinate);
			}
		}
		return new CellCoordinateSet(coordinates);
	}

	@Override
	public boolean support(ReportParserParser.UnitContext unitContext) {
		return unitContext.set() != null;
	}
}
