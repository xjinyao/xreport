package com.xjinyao.report.core.build.assertor;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class LikeAssertor implements Assertor {

	@Override
	public boolean eval(Object left, Object right) {
		if (left == null || right == null) {
			return false;
		}
		if (left.equals(right)) {
			return true;
		}
		return left.toString().indexOf(right.toString()) > -1;
	}

}
