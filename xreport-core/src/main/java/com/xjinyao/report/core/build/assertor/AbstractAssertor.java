package com.xjinyao.report.core.build.assertor;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public abstract class AbstractAssertor implements Assertor {
	protected Object buildObject(Object obj) {
		if (obj == null) {
			return obj;
		}
		if (obj instanceof List) {
			List<?> list = (List<?>) obj;
			if (list.size() == 1) {
				return list.get(0);
			}
		}
		return obj;
	}
}
