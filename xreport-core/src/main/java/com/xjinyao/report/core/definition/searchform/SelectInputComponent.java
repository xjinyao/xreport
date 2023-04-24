package com.xjinyao.report.core.definition.searchform;

import com.xjinyao.report.core.Utils;
import com.xjinyao.report.core.build.Dataset;
import com.xjinyao.report.core.exception.DatasetUndefinitionException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class SelectInputComponent extends InputComponent {
	private boolean useDataset;
	private String dataset;
	private String labelField;
	private String valueField;
	private List<Option> options;

	@Override
	String inputHtml(RenderContext context) {
		String name = getBindParameter();
		Object pvalue = context.getParameter(name) == null ? "" : context.getParameter(name);
		StringBuilder sb = new StringBuilder();
		sb.append("<select style=\"padding:3px;height:28px\" id='" + context.buildComponentId(this) + "' name='" + name + "' class='form-control'>");
		if (useDataset && StringUtils.isNotBlank(dataset)) {
			Dataset ds = context.getDataset(dataset);
			if (ds == null) {
				throw new DatasetUndefinitionException(dataset);
			}
			for (Object obj : ds.getData()) {
				Object label = Utils.getProperty(obj, labelField);
				Object value = Utils.getProperty(obj, valueField);
				String selected = value.equals(pvalue) ? "selected" : "";
				sb.append("<option value='" + value + "' " + selected + ">" + label + "</option>");
			}
			if (pvalue.equals("")) {
				sb.append("<option value='' selected></option>");
			}
		} else {
			for (Option option : options) {
				String value = option.getValue();
				String selected = value.equals(pvalue) ? "selected" : "";
				sb.append("<option value='" + value + "' " + selected + ">" + option.getLabel() + "</option>");
			}
			if (pvalue.equals("")) {
				sb.append("<option value='' selected></option>");
			}
		}
		sb.append("</select>");
		return sb.toString();
	}

	@Override
	public String initJs(RenderContext context) {
		String name = getBindParameter();
		StringBuilder sb = new StringBuilder();
		sb.append("formElements.push(");
		sb.append("function(){");
		sb.append("if(''==='" + name + "'){");
		sb.append("alert('列表框未绑定查询参数名，不能进行查询操作!');");
		sb.append("throw '列表框未绑定查询参数名，不能进行查询操作!'");
		sb.append("}");
		sb.append("return {");
		sb.append("\"" + name + "\":");
		sb.append("$('#" + context.buildComponentId(this) + "').val()");
		sb.append("}");
		sb.append("}");
		sb.append(");");
		return sb.toString();
	}

	public boolean isUseDataset() {
		return useDataset;
	}

	public void setUseDataset(boolean useDataset) {
		this.useDataset = useDataset;
	}

	public String getDataset() {
		return dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	public String getLabelField() {
		return labelField;
	}

	public void setLabelField(String labelField) {
		this.labelField = labelField;
	}

	public String getValueField() {
		return valueField;
	}

	public void setValueField(String valueField) {
		this.valueField = valueField;
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}
}
