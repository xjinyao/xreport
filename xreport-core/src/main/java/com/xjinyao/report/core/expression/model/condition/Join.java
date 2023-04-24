package com.xjinyao.report.core.expression.model.condition;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public enum Join {
	and, or;

	public static Join parse(String join) {
		if (join.equals("and") || join.equals("&&")) {
			return and;
		}
		if (join.equals("or") || join.equals("||")) {
			return or;
		}
		throw new IllegalArgumentException("Unknow join : " + join);
	}
}
