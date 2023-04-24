package com.xjinyao.report.core.build.assertor;


/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface Assertor {
	boolean eval(Object left, Object right);
}
