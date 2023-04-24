package com.xjinyao.report.core.expression.model.expr;

import com.xjinyao.report.core.expression.model.Operator;

import java.util.List;


/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ParenExpression extends JoinExpression {
	private static final long serialVersionUID = 142186918381087393L;

	public ParenExpression(List<Operator> operators, List<BaseExpression> expressions) {
		super(operators, expressions);
	}
}
