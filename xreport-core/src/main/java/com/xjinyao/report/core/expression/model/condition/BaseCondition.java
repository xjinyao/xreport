package com.xjinyao.report.core.expression.model.condition;

import com.xjinyao.report.core.build.BindData;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.ExpressionUtils;
import com.xjinyao.report.core.expression.model.Condition;
import com.xjinyao.report.core.expression.model.Op;
import com.xjinyao.report.core.expression.model.data.*;
import com.xjinyao.report.core.model.Cell;
import com.xjinyao.report.core.expression.model.data.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public abstract class BaseCondition implements Condition {
	protected Op op;
	protected Logger log = Logger.getAnonymousLogger();
	private String operation;
	private Join join;
	private Condition nextCondition;
	private String left;
	private String right;

	@Override
	public final boolean filter(Cell cell, Cell currentCell, Object obj, Context context) {
		Object left = computeLeft(cell, currentCell, obj, context);
		Object right = computeRight(cell, currentCell, obj, context);
		boolean result = ExpressionUtils.conditionEval(op, left, right);
		if (join != null && nextCondition != null) {
			if (result) {
				if (join.equals(Join.and)) {
					return nextCondition.filter(cell, currentCell, obj, context);
				} else {
					return result;
				}
			} else {
				if (join.equals(Join.and)) {
					return result;
				} else {
					return nextCondition.filter(cell, currentCell, obj, context);
				}
			}
		}
		return result;
	}

	abstract Object computeLeft(Cell cell, Cell currentCell, Object obj, Context context);

	abstract Object computeRight(Cell cell, Cell currentCell, Object obj, Context context);

	public abstract ConditionType getType();

	protected Object extractExpressionData(ExpressionData<?> data) {
		if (data instanceof ObjectExpressionData) {
			return data.getData();
		} else if (data instanceof ObjectListExpressionData) {
			ObjectListExpressionData listData = (ObjectListExpressionData) data;
			List<?> list = listData.getData();
			return list;
		} else if (data instanceof BindDataListExpressionData) {
			BindDataListExpressionData bindData = (BindDataListExpressionData) data;
			List<BindData> bindDataList = bindData.getData();
			List<Object> list = new ArrayList<>();
			for (BindData bd : bindDataList) {
				Object v = bd.getValue();
				list.add(v);
			}
			if (list.size() == 1) {
				return list.get(0);
			} else if (list.size() == 0) {
				return null;
			}
			return list;
		} else if (data instanceof NoneExpressionData) {
			return null;
		}
		return null;
	}

	public Op getOp() {
		return op;
	}

	public void setOp(Op op) {
		this.op = op;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public void setNextCondition(Condition nextCondition) {
		this.nextCondition = nextCondition;
	}

	public Join getJoin() {
		return join;
	}

	public void setJoin(Join join) {
		this.join = join;
	}

	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right = right;
	}
}
