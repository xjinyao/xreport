package com.xjinyao.report.core.build.assertor;

import com.xjinyao.report.core.Utils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class EqualsLessThenAssertor extends AbstractAssertor {

	@Override
	public boolean eval(Object left, Object right) {
		if (left == null || right == null) {
			return false;
		}
		if (StringUtils.isBlank(left.toString()) || StringUtils.isBlank(right.toString())) {
			return false;
		}
		BigDecimal leftObj = Utils.toBigDecimal(left);
		right = buildObject(right);
		BigDecimal rightObj = Utils.toBigDecimal(right);
		return leftObj.compareTo(rightObj) < 1;
	}
}
