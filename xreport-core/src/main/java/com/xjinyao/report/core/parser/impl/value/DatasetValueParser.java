package com.xjinyao.report.core.parser.impl.value;

import com.xjinyao.report.core.definition.Order;
import com.xjinyao.report.core.definition.mapping.MappingItem;
import com.xjinyao.report.core.definition.mapping.MappingType;
import com.xjinyao.report.core.definition.value.AggregateType;
import com.xjinyao.report.core.definition.value.DatasetValue;
import com.xjinyao.report.core.definition.value.GroupItem;
import com.xjinyao.report.core.definition.value.Value;
import com.xjinyao.report.core.expression.model.Condition;
import com.xjinyao.report.core.expression.model.condition.PropertyExpressionCondition;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class DatasetValueParser extends ValueParser {
	@Override
	public Value parse(Element element) {
		DatasetValue value = new DatasetValue();
		value.setAggregate(AggregateType.valueOf(element.attributeValue("aggregate")));
		value.setDatasetName(element.attributeValue("dataset-name"));
		value.setProperty(element.attributeValue("property"));
		String order = element.attributeValue("order");
		if (StringUtils.isNotBlank(order)) {
			value.setOrder(Order.valueOf(order));
		}
		String mappingType = element.attributeValue("mapping-type");
		if (StringUtils.isNotBlank(mappingType)) {
			value.setMappingType(MappingType.valueOf(mappingType));
		}
		value.setMappingDataset(element.attributeValue("mapping-dataset"));
		value.setMappingKeyProperty(element.attributeValue("mapping-key-property"));
		value.setMappingValueProperty(element.attributeValue("mapping-value-property"));
		List<GroupItem> groupItems = null;
		List<MappingItem> mappingItems = null;
		List<Condition> conditions = new ArrayList<>();
		PropertyExpressionCondition topCondition = null;
		PropertyExpressionCondition prevCondition = null;
		value.setConditions(conditions);
		for (Object obj : element.elements()) {
			if (obj == null || !(obj instanceof Element)) {
				continue;
			}
			Element ele = (Element) obj;
			if (ele.getName().equals("condition")) {
				PropertyExpressionCondition condition = parseCondition(ele);
				conditions.add(condition);
				if (topCondition == null) {
					topCondition = condition;
					prevCondition = topCondition;
				} else {
					prevCondition.setNextCondition(condition);
					prevCondition.setJoin(condition.getJoin());
					prevCondition = condition;
				}
			} else if (ele.getName().equals("group-item")) {
				if (groupItems == null) {
					groupItems = new ArrayList<>();
					value.setGroupItems(groupItems);
				}
				GroupItem item = new GroupItem();
				item.setName(ele.attributeValue("name"));
				groupItems.add(item);
				PropertyExpressionCondition groupItemTopCondition = null;
				List<Condition> itemConditions = new ArrayList<>();
				for (Object o : ele.elements()) {
					if (o == null || !(o instanceof Element)) {
						continue;
					}
					PropertyExpressionCondition itemCondition = parseCondition((Element) o);
					itemConditions.add(itemCondition);
					if (groupItemTopCondition == null) {
						groupItemTopCondition = itemCondition;
					} else {
						groupItemTopCondition.setNextCondition(itemCondition);
					}
				}
				item.setCondition(groupItemTopCondition);
				item.setConditions(itemConditions);
			} else if (ele.getName().equals("mapping-item")) {
				MappingItem item = new MappingItem();
				item.setLabel(ele.attributeValue("label"));
				item.setValue(ele.attributeValue("value"));
				if (mappingItems == null) {
					mappingItems = new ArrayList<>();
				}
				mappingItems.add(item);
			}
		}
		if (topCondition != null) {
			value.setCondition(topCondition);
		}
		if (mappingItems != null) {
			value.setMappingItems(mappingItems);
		}
		return value;
	}

	private PropertyExpressionCondition parseCondition(Element ele) {
		PropertyExpressionCondition condition = new PropertyExpressionCondition();
		condition.parseCondition(ele);
		return condition;
	}
}
