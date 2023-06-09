package com.xjinyao.report.core.expression;

import com.xjinyao.report.core.build.assertor.*;
import com.xjinyao.report.core.dsl.ReportParserLexer;
import com.xjinyao.report.core.dsl.ReportParserParser;
import com.xjinyao.report.core.exception.ReportParseException;
import com.xjinyao.report.core.expression.function.Function;
import com.xjinyao.report.core.expression.model.Expression;
import com.xjinyao.report.core.expression.model.Op;
import com.xjinyao.report.core.expression.parse.ExpressionErrorListener;
import com.xjinyao.report.core.expression.parse.ExpressionVisitor;
import com.xjinyao.report.core.expression.parse.builder.*;
import com.xjinyao.report.core.build.assertor.*;
import com.xjinyao.report.core.expression.parse.builder.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.*;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ExpressionUtils implements ApplicationContextAware {
	public static final String EXPR_PREFIX = "${";
	public static final String EXPR_SUFFIX = "}";
	private static ExpressionVisitor exprVisitor;
	private static Map<String, Function> functions = new HashMap<>();
	private static Map<Op, Assertor> assertorsMap = new HashMap<>();
	private static List<ExpressionBuilder> expressionBuilders = new ArrayList<>();
	private static List<String> cellNameList = new ArrayList<>();
	private static String[] LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

	static {
		expressionBuilders.add(new StringExpressionBuilder());
		expressionBuilders.add(new VariableExpressionBuilder());
		expressionBuilders.add(new BooleanExpressionBuilder());
		expressionBuilders.add(new IntegerExpressionBuilder());
		expressionBuilders.add(new DatasetExpressionBuilder());
		expressionBuilders.add(new FunctionExpressionBuilder());
		expressionBuilders.add(new NumberExpressionBuilder());
		expressionBuilders.add(new CellPositionExpressionBuilder());
		expressionBuilders.add(new RelativeCellExpressionBuilder());
		expressionBuilders.add(new SetExpressionBuilder());
		expressionBuilders.add(new CellObjectExpressionBuilder());
		expressionBuilders.add(new NullExpressionBuilder());
		expressionBuilders.add(new CurrentCellValueExpressionBuilder());
		expressionBuilders.add(new CurrentCellDataExpressionBuilder());

		assertorsMap.put(Op.Equals, new EqualsAssertor());
		assertorsMap.put(Op.EqualsGreatThen, new EqualsGreatThenAssertor());
		assertorsMap.put(Op.EqualsLessThen, new EqualsLessThenAssertor());
		assertorsMap.put(Op.GreatThen, new GreatThenAssertor());
		assertorsMap.put(Op.LessThen, new LessThenAssertor());
		assertorsMap.put(Op.NotEquals, new NotEqualsAssertor());
		assertorsMap.put(Op.In, new InAssertor());
		assertorsMap.put(Op.NotIn, new NotInAssertor());
		assertorsMap.put(Op.Like, new LikeAssertor());

		for (int i = 0; i < LETTERS.length; i++) {
			cellNameList.add(LETTERS[i]);
		}

		for (int i = 0; i < LETTERS.length; i++) {
			String name = LETTERS[i];
			for (int j = 0; j < LETTERS.length; j++) {
				cellNameList.add(name + LETTERS[j]);
			}
		}
	}

	public static List<String> getCellNameList() {
		return cellNameList;
	}

	public static Map<String, Function> getFunctions() {
		return functions;
	}

	public static Map<Op, Assertor> getAssertorsMap() {
		return assertorsMap;
	}

	public static boolean conditionEval(Op op, Object left, Object right) {
		Assertor assertor = assertorsMap.get(op);
		boolean result = assertor.eval(left, right);
		return result;
	}

	public static Expression parseExpression(String text) {
		ANTLRInputStream antlrInputStream = new ANTLRInputStream(text);
		ReportParserLexer lexer = new ReportParserLexer(antlrInputStream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		ReportParserParser parser = new ReportParserParser(tokenStream);
		ExpressionErrorListener errorListener = new ExpressionErrorListener();
		parser.addErrorListener(errorListener);
		exprVisitor = new ExpressionVisitor(expressionBuilders);
		Expression expression = exprVisitor.visitEntry(parser.entry());
		String error = errorListener.getErrorMessage();
		if (error != null) {
			throw new ReportParseException("Expression parse error:" + error);
		}
		return expression;
	}

	public static ExpressionVisitor getExprVisitor() {
		return exprVisitor;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Collection<Function> coll = applicationContext.getBeansOfType(Function.class).values();
		for (Function fun : coll) {
			functions.put(fun.name(), fun);
		}
	}
}
