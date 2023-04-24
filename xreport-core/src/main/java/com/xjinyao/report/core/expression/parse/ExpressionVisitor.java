package com.xjinyao.report.core.expression.parse;

import com.xjinyao.report.core.dsl.ReportParserBaseVisitor;
import com.xjinyao.report.core.exception.ReportParseException;
import com.xjinyao.report.core.expression.model.Expression;
import com.xjinyao.report.core.expression.model.Op;
import com.xjinyao.report.core.expression.model.Operator;
import com.xjinyao.report.core.expression.model.condition.Join;
import com.xjinyao.report.core.expression.model.expr.*;
import com.xjinyao.report.core.expression.model.expr.ifelse.*;
import com.xjinyao.report.core.expression.parse.builder.ExpressionBuilder;
import com.xjinyao.report.core.dsl.ReportParserParser;
import com.xjinyao.report.core.expression.model.expr.*;
import com.xjinyao.report.core.expression.model.expr.ifelse.*;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ExpressionVisitor extends ReportParserBaseVisitor<Expression> {
	private List<ExpressionBuilder> expressionBuilders;

	public ExpressionVisitor(List<ExpressionBuilder> expressionBuilders) {
		this.expressionBuilders = expressionBuilders;
	}

	@Override
	public Expression visitEntry(ReportParserParser.EntryContext ctx) {
		StringBuilder sb = new StringBuilder();
		List<ReportParserParser.ExpressionContext> exprs = ctx.expression();
		List<Expression> list = new ArrayList<>();
		for (ReportParserParser.ExpressionContext exprContext : exprs) {
			sb.append(exprContext.getText());
			Expression expr = visitExpression(exprContext);
			list.add(expr);
		}
		ExpressionBlock block = new ExpressionBlock();
		block.setExpressionList(list);
		block.setExpr(sb.toString());
		return block;
	}

	@Override
	public Expression visitExpression(ReportParserParser.ExpressionContext ctx) {
		ReportParserParser.ExprCompositeContext exprCompositeContext = ctx.exprComposite();
		ReportParserParser.IfExprContext ifExprContext = ctx.ifExpr();
		ReportParserParser.CaseExprContext caseExprContext = ctx.caseExpr();
		ReportParserParser.VariableAssignContext assignCtx = ctx.variableAssign();
		ReportParserParser.ReturnExprContext returnCtx = ctx.returnExpr();
		if (exprCompositeContext != null) {
			return parseExprComposite(exprCompositeContext);
		} else if (ifExprContext != null) {
			IfExpression expr = parseIfExprContext(ifExprContext);
			return expr;
		} else if (caseExprContext != null) {
			IfExpression expr = parseCaseExprContext(caseExprContext);
			return expr;
		} else if (assignCtx != null) {
			VariableAssignExpression expr = new VariableAssignExpression();
			expr.setExpr(assignCtx.getText());
			expr.setVariable(assignCtx.variable().Identifier().getText());
			expr.setExpression(parseItemContext(assignCtx.item()));
			return expr;
		} else if (returnCtx != null) {
			return visitExpr(returnCtx.expr());
		} else {
			throw new ReportParseException("Expression [" + ctx.getText() + "] is invalid.");
		}
	}

	private Expression parseExprComposite(ReportParserParser.ExprCompositeContext exprCompositeContext) {
		if (exprCompositeContext instanceof ReportParserParser.SingleExprCompositeContext) {
			ReportParserParser.SingleExprCompositeContext singleExprCompositeContext = (ReportParserParser.SingleExprCompositeContext) exprCompositeContext;
			ReportParserParser.ExprContext exprContext = singleExprCompositeContext.expr();
			return parseExpr(exprContext);
		} else if (exprCompositeContext instanceof ReportParserParser.ParenExprCompositeContext) {
			ReportParserParser.ParenExprCompositeContext parenExprCompositeContext = (ReportParserParser.ParenExprCompositeContext) exprCompositeContext;
			ReportParserParser.ExprCompositeContext childExprCompositeContext = parenExprCompositeContext.exprComposite();
			return parseExprComposite(childExprCompositeContext);
		} else if (exprCompositeContext instanceof ReportParserParser.TernaryExprCompositeContext) {
			ReportParserParser.TernaryExprCompositeContext ternaryExprCompositeContext = (ReportParserParser.TernaryExprCompositeContext) exprCompositeContext;
			ReportParserParser.TernaryExprContext ternaryExprContext = ternaryExprCompositeContext.ternaryExpr();
			List<ReportParserParser.IfConditionContext> ifConditionContexts = ternaryExprContext.ifCondition();
			IfExpression expr = new IfExpression();
			expr.setConditionList(parseCondtionList(ifConditionContexts, ternaryExprContext.join()));
			ReportParserParser.BlockContext firstBlockContext = ternaryExprContext.block(0);
			expr.setExpression(parseBlock(firstBlockContext));

			ReportParserParser.BlockContext secondBlockContext = ternaryExprContext.block(1);
			ElseExpression elseExpr = new ElseExpression();
			elseExpr.setExpression(parseBlock(secondBlockContext));
			expr.setElseExpression(elseExpr);
			return expr;
		} else if (exprCompositeContext instanceof ReportParserParser.ComplexExprCompositeContext) {
			ReportParserParser.ComplexExprCompositeContext complexExprCompositeContext = (ReportParserParser.ComplexExprCompositeContext) exprCompositeContext;
			ReportParserParser.ExprCompositeContext leftExprCompositeContext = complexExprCompositeContext.exprComposite(0);
			Expression leftExpression = parseExprComposite(leftExprCompositeContext);
			ReportParserParser.ExprCompositeContext rightExprCompositeContext = complexExprCompositeContext.exprComposite(1);
			Expression rightExpression = parseExprComposite(rightExprCompositeContext);
			String op = complexExprCompositeContext.Operator().getText();
			Operator operator = Operator.parse(op);
			List<BaseExpression> expressions = new ArrayList<>();
			expressions.add((BaseExpression) leftExpression);
			expressions.add((BaseExpression) rightExpression);
			List<Operator> operators = new ArrayList<>();
			operators.add(operator);
			ParenExpression expression = new ParenExpression(operators, expressions);
			expression.setExpr(complexExprCompositeContext.getText());
			return expression;
		} else {
			throw new ReportParseException("Unknow context :" + exprCompositeContext);
		}
	}

	private ExpressionBlock parseExpressionBlock(List<ReportParserParser.ExprBlockContext> contexts) {
		StringBuilder sb = new StringBuilder();
		List<Expression> expressionList = new ArrayList<>();
		for (ReportParserParser.ExprBlockContext ctx : contexts) {
			sb.append(ctx.getText());
			ReportParserParser.VariableAssignContext assignContext = ctx.variableAssign();
			if (assignContext != null) {
				ReportParserParser.VariableContext varCtx = assignContext.variable();
				String variableName = varCtx.Identifier().getText();
				VariableAssignExpression assignExpr = new VariableAssignExpression();
				assignExpr.setExpr(assignContext.getText());
				assignExpr.setVariable(variableName);
				ReportParserParser.ItemContext itemCtx = assignContext.item();
				BaseExpression itemExpr = parseItemContext(itemCtx);
				assignExpr.setExpression(itemExpr);
				expressionList.add(assignExpr);
			}
			ReportParserParser.IfExprContext ifCtx = ctx.ifExpr();
			if (ifCtx != null) {
				IfExpression ifExpr = parseIfExprContext(ifCtx);
				expressionList.add(ifExpr);
			}
			ReportParserParser.CaseExprContext caseCtx = ctx.caseExpr();
			if (caseCtx != null) {
				IfExpression caseExpr = parseCaseExprContext(caseCtx);
				expressionList.add(caseExpr);
			}
		}
		ExpressionBlock blockExpr = new ExpressionBlock();
		blockExpr.setExpressionList(expressionList);
		blockExpr.setExpr(sb.toString());
		return blockExpr;
	}

	private IfExpression parseIfExprContext(ReportParserParser.IfExprContext ifExprContext) {
		IfExpression expr = new IfExpression();
		expr.setExpr(ifExprContext.getText());
		ReportParserParser.IfPartContext ifPartContext = ifExprContext.ifPart();
		List<ReportParserParser.IfConditionContext> ifConditionContexts = ifPartContext.ifCondition();
		List<ReportParserParser.JoinContext> joinContexts = ifPartContext.join();
		expr.setConditionList(parseCondtionList(ifConditionContexts, joinContexts));
		ExpressionBlock blockExpr = parseBlock(ifPartContext.block());
		expr.setExpression(blockExpr);
		List<ReportParserParser.ElseIfPartContext> elseIfPartContexts = ifExprContext.elseIfPart();
		if (elseIfPartContexts != null && elseIfPartContexts.size() > 0) {
			List<ElseIfExpression> elseIfExpressionList = new ArrayList<>();
			for (ReportParserParser.ElseIfPartContext elseIfContext : elseIfPartContexts) {
				ifConditionContexts = elseIfContext.ifCondition();
				joinContexts = elseIfContext.join();
				ElseIfExpression elseIfExpr = new ElseIfExpression();
				elseIfExpr.setConditionList(parseCondtionList(ifConditionContexts, joinContexts));
				elseIfExpr.setExpression(parseBlock(elseIfContext.block()));
				elseIfExpressionList.add(elseIfExpr);
			}
			expr.setElseIfExpressions(elseIfExpressionList);
		}

		ReportParserParser.ElsePartContext elsePartContext = ifExprContext.elsePart();
		if (elsePartContext != null) {
			ElseExpression elseExpression = new ElseExpression();
			elseExpression.setExpression(parseBlock(elsePartContext.block()));
			expr.setElseExpression(elseExpression);
		}
		return expr;
	}

	private ExpressionBlock parseBlock(ReportParserParser.BlockContext blockCtx) {
		List<ReportParserParser.ExprBlockContext> exprBlockCtxs = blockCtx.exprBlock();
		ReportParserParser.ReturnExprContext returnCtx = blockCtx.returnExpr();
		ExpressionBlock block = null;
		if (exprBlockCtxs != null) {
			block = parseExpressionBlock(exprBlockCtxs);
		}
		if (returnCtx != null) {
			if (block == null) block = new ExpressionBlock();
			block.setReturnExpression(visitExpr(returnCtx.expr()));
		}
		return block;
	}

	private IfExpression parseCaseExprContext(ReportParserParser.CaseExprContext caseExprContext) {
		IfExpression expr = new IfExpression();
		List<ElseIfExpression> elseIfExpressionList = new ArrayList<>();
		expr.setElseIfExpressions(elseIfExpressionList);
		List<ReportParserParser.CasePartContext> casePartContexts = caseExprContext.casePart();
		for (ReportParserParser.CasePartContext casePartContext : casePartContexts) {
			List<ReportParserParser.IfConditionContext> ifConditionContexts = casePartContext.ifCondition();
			List<ReportParserParser.JoinContext> joinContexts = casePartContext.join();
			ElseIfExpression elseIfExpr = new ElseIfExpression();
			elseIfExpr.setConditionList(parseCondtionList(ifConditionContexts, joinContexts));
			elseIfExpr.setExpr(casePartContext.getText());
			ExpressionBlock blockExpr = parseBlock(casePartContext.block());
			elseIfExpr.setExpression(blockExpr);
			elseIfExpressionList.add(elseIfExpr);
		}
		return expr;
	}


	private Expression parseExpr(ReportParserParser.ExprContext exprContext) {
		List<BaseExpression> expressions = new ArrayList<>();
		List<Operator> operators = new ArrayList<>();
		List<ReportParserParser.ItemContext> itemContexts = exprContext.item();
		List<TerminalNode> operatorNodes = exprContext.Operator();
		for (int i = 0; i < itemContexts.size(); i++) {
			ReportParserParser.ItemContext itemContext = itemContexts.get(i);
			BaseExpression expr = parseItemContext(itemContext);
			expressions.add(expr);
			if (i > 0) {
				TerminalNode operatorNode = operatorNodes.get(i - 1);
				String op = operatorNode.getText();
				operators.add(Operator.parse(op));
			}
		}
		ParenExpression expression = new ParenExpression(operators, expressions);
		expression.setExpr(exprContext.getText());
		return expression;
	}

	private ExpressionConditionList parseCondtionList(List<ReportParserParser.IfConditionContext> ifConditionContexts, List<ReportParserParser.JoinContext> joinContexts) {
		List<ExpressionCondition> list = new ArrayList<>();
		List<Join> joins = new ArrayList<>();
		for (int i = 0; i < ifConditionContexts.size(); i++) {
			ReportParserParser.IfConditionContext context = ifConditionContexts.get(i);
			ReportParserParser.ExprContext left = context.expr(0);
			ReportParserParser.ExprContext right = context.expr(1);
			Expression leftExpr = parseExpr(left);
			Expression rightExpr = parseExpr(right);
			Op op = Op.parse(context.OP().getText());
			ExpressionCondition condition = new ExpressionCondition(leftExpr, op, rightExpr);
			list.add(condition);
			if (i > 0) {
				ReportParserParser.JoinContext joinContext = joinContexts.get(i - 1);
				String text = joinContext.getText();
				Join join = Join.and;
				if (text.equals("or") || text.equals("||")) {
					join = Join.or;
				}
				joins.add(join);
			}
		}
		return new ExpressionConditionList(list, joins);
	}

	public BaseExpression parseItemContext(ReportParserParser.ItemContext itemContext) {
		BaseExpression expression = null;
		if (itemContext instanceof ReportParserParser.SimpleJoinContext) {
			ReportParserParser.SimpleJoinContext simpleJoinContext = (ReportParserParser.SimpleJoinContext) itemContext;
			expression = visitSimpleJoin(simpleJoinContext);
		} else if (itemContext instanceof ReportParserParser.ParenJoinContext) {
			ReportParserParser.ParenJoinContext parenJoinContext = (ReportParserParser.ParenJoinContext) itemContext;
			expression = visitParenJoin(parenJoinContext);
		} else if (itemContext instanceof ReportParserParser.SingleParenJoinContext) {
			ReportParserParser.SingleParenJoinContext singleContext = (ReportParserParser.SingleParenJoinContext) itemContext;
			ReportParserParser.ItemContext childItemContext = singleContext.item();
			expression = parseItemContext(childItemContext);
		} else {
			throw new ReportParseException("Unknow context :" + itemContext);
		}
		return expression;
	}

	@Override
	public BaseExpression visitSimpleJoin(ReportParserParser.SimpleJoinContext ctx) {
		List<BaseExpression> expressions = new ArrayList<>();
		List<Operator> operators = new ArrayList<>();
		List<ReportParserParser.UnitContext> unitContexts = ctx.unit();
		List<TerminalNode> operatorNodes = ctx.Operator();
		for (int i = 0; i < unitContexts.size(); i++) {
			ReportParserParser.UnitContext unitContext = unitContexts.get(i);
			BaseExpression expr = buildExpression(unitContext);
			expressions.add(expr);
			if (i > 0) {
				TerminalNode operatorNode = operatorNodes.get(i - 1);
				String op = operatorNode.getText();
				operators.add(Operator.parse(op));
			}
		}
		if (operators.size() == 0 && expressions.size() == 1) {
			return expressions.get(0);
		}
		JoinExpression expression = new JoinExpression(operators, expressions);
		expression.setExpr(ctx.getText());
		return expression;
	}

	@Override
	public BaseExpression visitParenJoin(ReportParserParser.ParenJoinContext ctx) {
		List<BaseExpression> expressions = new ArrayList<>();
		List<Operator> operators = new ArrayList<>();
		List<ReportParserParser.ItemContext> itemContexts = ctx.item();
		List<TerminalNode> operatorNodes = ctx.Operator();
		for (int i = 0; i < itemContexts.size(); i++) {
			ReportParserParser.ItemContext itemContext = itemContexts.get(i);
			BaseExpression expr = parseItemContext(itemContext);
			expressions.add(expr);
			if (i > 0) {
				TerminalNode operatorNode = operatorNodes.get(i - 1);
				String op = operatorNode.getText();
				operators.add(Operator.parse(op));
			}
		}
		ParenExpression expression = new ParenExpression(operators, expressions);
		expression.setExpr(ctx.getText());
		return expression;
	}

	private BaseExpression buildExpression(ReportParserParser.UnitContext unitContext) {
		for (ExpressionBuilder builder : expressionBuilders) {
			if (builder.support(unitContext)) {
				return builder.build(unitContext);
			}
		}
		throw new ReportParseException("Unknow context :" + unitContext);
	}
}
