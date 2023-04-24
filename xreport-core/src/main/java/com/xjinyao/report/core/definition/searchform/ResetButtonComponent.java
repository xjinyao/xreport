package com.xjinyao.report.core.definition.searchform;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ResetButtonComponent extends ButtonComponent {
	@Override
	public String toHtml(RenderContext context) {
		return "<button type=\"reset\" id=\"" + context.buildComponentId(this) + "\" class=\"btn " + getStyle() + " btn-sm\">" + getLabel() + "</button>";
	}

	@Override
	public String initJs(RenderContext context) {
		return "";
	}
}
