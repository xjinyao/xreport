package com.xjinyao.report.core.definition;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public enum BorderStyle {
	solid, dashed, doublesolid;

	public static BorderStyle toBorderStyle(String name) {
		if (name.equals("double")) {
			return BorderStyle.doublesolid;
		} else {
			return BorderStyle.valueOf(name);
		}
	}

	@Override
	public String toString() {
		if (this.equals(BorderStyle.doublesolid)) {
			return "double";
		}
		return super.toString();
	}
}
