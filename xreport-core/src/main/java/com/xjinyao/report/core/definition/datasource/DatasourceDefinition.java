package com.xjinyao.report.core.definition.datasource;

import com.xjinyao.report.core.definition.dataset.DatasetDefinition;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface DatasourceDefinition {
	String getName();

	List<DatasetDefinition> getDatasets();

	DatasourceType getType();
}
