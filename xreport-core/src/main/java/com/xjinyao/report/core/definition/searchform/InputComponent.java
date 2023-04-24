package com.xjinyao.report.core.definition.searchform;


/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public abstract class InputComponent implements Component {
	protected LabelPosition labelPosition = LabelPosition.top;
	private String label;
	private String bindParameter;
	private String type;

	abstract String inputHtml(RenderContext context);

	@Override
	public String toHtml(RenderContext context) {
		StringBuffer sb = new StringBuffer();
		if (this.labelPosition.equals(LabelPosition.top)) {
			sb.append("<div>");
		} else {
			sb.append("<div class='form-horizontal'>");
		}
		sb.append("<div class='form-group' style='margin:0px 0px 10px 0px'>");
		if (this.labelPosition.equals(LabelPosition.top)) {
			sb.append("<span style='font-size:13px'>" + this.label + "</span>");
			sb.append(inputHtml(context));
		} else {
			sb.append("<span class='col-md-3' style='text-align:right;padding-right:1px;font-size:13px;line-height:28px;'>" + this.label + "</span>");
			sb.append("<div class='col-md-9' style='padding-left:1px;'>");
			sb.append(inputHtml(context));
			sb.append("</div>");
		}
		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public LabelPosition getLabelPosition() {
		return labelPosition;
	}

	public void setLabelPosition(LabelPosition labelPosition) {
		this.labelPosition = labelPosition;
	}

	public String getBindParameter() {
		return bindParameter;
	}

	public void setBindParameter(String bindParameter) {
		this.bindParameter = bindParameter;
	}

	@Override
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
