package com.xjinyao.report.core.definition.searchform;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class SubmitButtonComponent extends ButtonComponent {
	@Override
	public String initJs(RenderContext context) {
		StringBuilder sb = new StringBuilder();
		sb.append("$('#" + context.buildComponentId(this) + "').click(function(){");
		sb.append("doSearch();");
		sb.append("});");
		return sb.toString();
	}
}
