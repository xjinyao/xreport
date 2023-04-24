package com.xjinyao.report.core.definition.datasource;

import com.xjinyao.report.core.build.Dataset;
import com.xjinyao.report.core.definition.dataset.DatasetDefinition;
import com.xjinyao.report.core.definition.dataset.SqlDatasetDefinition;
import com.xjinyao.report.core.exception.ReportComputeException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class BuildinDatasourceDefinition implements DatasourceDefinition {
	private String name;
	private List<DatasetDefinition> datasets;

	public List<Dataset> buildDatasets(Connection conn, Map<String, Object> parameters) {
		if (datasets == null || datasets.size() == 0) {
			return null;
		}
		List<Dataset> list = new ArrayList<>();
		try {
			for (DatasetDefinition dsDef : datasets) {
				SqlDatasetDefinition sqlDataset = (SqlDatasetDefinition) dsDef;
				Dataset ds = sqlDataset.buildDataset(parameters, conn);
				list.add(ds);
			}
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new ReportComputeException(e);
			}
		}
		return list;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public List<DatasetDefinition> getDatasets() {
		return datasets;
	}

	public void setDatasets(List<DatasetDefinition> datasets) {
		this.datasets = datasets;
	}

	@Override
	public DatasourceType getType() {
		return DatasourceType.buildin;
	}
}
