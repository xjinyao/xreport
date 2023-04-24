package com.xjinyao.report.core.parser.impl;

import com.xjinyao.report.core.definition.dataset.*;
import com.xjinyao.report.core.definition.datasource.*;
import com.xjinyao.report.core.definition.dataset.*;
import com.xjinyao.report.core.definition.datasource.*;
import com.xjinyao.report.core.expression.ExpressionUtils;
import com.xjinyao.report.core.expression.model.Expression;
import com.xjinyao.report.core.parser.Parser;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class DatasourceParser implements Parser<DatasourceDefinition> {
	@Override
	public DatasourceDefinition parse(Element element) {
		String type = element.attributeValue("type");
		if (type.equals("jdbc")) {
			JdbcDatasourceDefinition ds = new JdbcDatasourceDefinition();
			ds.setName(element.attributeValue("name"));
			ds.setDriver(element.attributeValue("driver"));
			ds.setUrl(element.attributeValue("url"));
			ds.setUsername(element.attributeValue("username"));
			ds.setPassword(element.attributeValue("password"));
			ds.setDatasets(parseDatasets(element));
			return ds;
		} else if (type.equals("spring")) {
			SpringBeanDatasourceDefinition ds = new SpringBeanDatasourceDefinition();
			ds.setName(element.attributeValue("name"));
			ds.setBeanId(element.attributeValue("bean"));
			ds.setDatasets(parseDatasets(element));
			return ds;
		} else if (type.equals("buildin")) {
			BuildinDatasourceDefinition ds = new BuildinDatasourceDefinition();
			ds.setName(element.attributeValue("name"));
			ds.setDatasets(parseDatasets(element));
			return ds;
		}
		return null;
	}

	private List<DatasetDefinition> parseDatasets(Element element) {
		List<DatasetDefinition> list = new ArrayList<>();
		for (Object obj : element.elements()) {
			if (obj == null || !(obj instanceof Element)) {
				continue;
			}
			Element ele = (Element) obj;
			String type = ele.attributeValue("type");
			if (type.equals("sql")) {
				SqlDatasetDefinition dataset = new SqlDatasetDefinition();
				dataset.setName(ele.attributeValue("name"));
				dataset.setSql(parseSql(ele, dataset));
				dataset.setFields(parseFields(ele));
				dataset.setParameters(parseParameters(ele));
				list.add(dataset);
			} else if (type.equals("bean")) {
				BeanDatasetDefinition dataset = new BeanDatasetDefinition();
				dataset.setName(ele.attributeValue("name"));
				dataset.setMethod(ele.attributeValue("method"));
				dataset.setFields(parseFields(ele));
				dataset.setClazz(ele.attributeValue("clazz"));
				list.add(dataset);
			}
		}
		return list;
	}

	private List<Parameter> parseParameters(Element element) {
		List<Parameter> parameters = new ArrayList<>();
		for (Object obj : element.elements()) {
			if (obj == null || !(obj instanceof Element)) {
				continue;
			}
			Element ele = (Element) obj;
			if (!ele.getName().equals("parameter")) {
				continue;
			}
			Parameter param = new Parameter();
			param.setName(ele.attributeValue("name"));
			param.setDefaultValue(ele.attributeValue("default-value"));
			param.setType(DataType.valueOf(ele.attributeValue("type")));
			parameters.add(param);
		}
		return parameters;
	}

	private List<Field> parseFields(Element element) {
		List<Field> fields = new ArrayList<>();
		for (Object obj : element.elements()) {
			if (obj == null || !(obj instanceof Element)) {
				continue;
			}
			Element ele = (Element) obj;
			if (!ele.getName().equals("field")) {
				continue;
			}
			Field field = new Field(ele.attributeValue("name"));
			fields.add(field);
		}
		return fields;
	}

	private String parseSql(Element element, SqlDatasetDefinition dataset) {
		for (Object obj : element.elements()) {
			if (obj == null || !(obj instanceof Element)) {
				continue;
			}
			Element ele = (Element) obj;
			if (ele.getName().equals("sql")) {
				String sql = ele.getText().trim();
				if (sql.startsWith(ExpressionUtils.EXPR_PREFIX) && sql.endsWith(ExpressionUtils.EXPR_SUFFIX)) {
					String s = sql.substring(2, sql.length() - 1);
					Expression expr = ExpressionUtils.parseExpression(s);
					dataset.setSqlExpression(expr);
				}
				return ele.getText();
			}
		}
		return null;
	}
}
