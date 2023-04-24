package com.xjinyao.report.core.build.assertor;


/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class NotEqualsAssertor extends AbstractAssertor {

	@Override
	public boolean eval(Object left, Object right) {
		if (left == null && right == null) {
			return false;
		}
		if (left == null || right == null) {
			return true;
		}
		right = buildObject(right);
		return !left.equals(right);
	}
}
