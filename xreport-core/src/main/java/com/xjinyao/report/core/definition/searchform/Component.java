package com.xjinyao.report.core.definition.searchform;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface Component {
	String toHtml(RenderContext context);

	String initJs(RenderContext context);

	String getType();
}
