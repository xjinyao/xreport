package com.xjinyao.report.core.provider.report;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.Date;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
@Data
@Builder
public class ReportFile {

	private String type;
	private String name;
	private String fileName;
	private String description;
	private Boolean isTemplate;
	private Boolean visible;
	private Boolean previewImmediatelyLoad;
	private String previewParamsDeclarationConfig;
	private Date updateDate;

	@Tolerate
	public ReportFile() {

	}

	@Tolerate
	public ReportFile(String type, String name, String fileName, String description, Boolean isTemplate, Boolean visible,
					  Boolean previewImmediatelyLoad, Date updateDate) {
		this.type = type;
		this.name = name;
		this.fileName = fileName;
		this.description = description;
		this.isTemplate = isTemplate;
		this.visible = visible;
		this.previewImmediatelyLoad = previewImmediatelyLoad;
		this.updateDate = updateDate;
	}

	@Tolerate
	public ReportFile(String name, Date updateDate) {
		this.type = "默认";
		this.fileName = name;
		this.name = name;
		this.updateDate = updateDate;
	}

}
