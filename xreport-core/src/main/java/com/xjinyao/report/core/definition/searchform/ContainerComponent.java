package com.xjinyao.report.core.definition.searchform;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public abstract class ContainerComponent implements Component {
	protected List<Component> children;

	public List<Component> getChildren() {
		return children;
	}

	public void setChildren(List<Component> children) {
		this.children = children;
	}
}
