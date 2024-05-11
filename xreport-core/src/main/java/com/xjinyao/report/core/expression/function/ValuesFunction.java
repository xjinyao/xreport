package com.xjinyao.report.core.expression.function;

import com.xjinyao.report.core.Utils;
import com.xjinyao.report.core.build.BindData;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.data.BindDataListExpressionData;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.model.Cell;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 循环函数
 *
 * @author 谢进伟
 * @since 2024/05/07
 */
public class ValuesFunction implements Function {

    @Override
    public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
        List<Object> list = new ArrayList<>();

        Integer index = null;
        String propertyName = null;
        if (dataList.size() > 2) {
            Object indexData = dataList.get(1).getData();
            if (indexData != null) {
                index = Integer.parseInt(indexData.toString());
            }
            Object propertyNameData = dataList.get(2).getData();
            if (propertyNameData != null) {
                propertyName = propertyNameData.toString();
            }
        } else if (dataList.size() > 1) {
            Object propertyNameData = dataList.get(1).getData();
            if (propertyNameData != null) {
                propertyName = propertyNameData.toString();
            }
        } else {
            return dataList.get(0);
        }
        if (propertyName == null) {
            return StringUtils.EMPTY;
        }

        ExpressionData<?> d = dataList.get(0);
        if (d instanceof BindDataListExpressionData) {
            List<BindData> data = ((BindDataListExpressionData) d).getData();
            String targetPropertyName = propertyName;
            list.addAll(data.stream()
                    .map(BindData::getValue)
                    .filter(_d -> _d instanceof Collection)
                    .map(_d -> (List<?>) _d)
                    .flatMap(Collection::stream)
                    .map(_d -> Optional.ofNullable(Utils.getProperty(_d, targetPropertyName))
                            .map(Object::toString)
                            .orElse(StringUtils.EMPTY))
                    .collect(Collectors.toList()));
        }
        if (index != null && !CollectionUtils.isEmpty(list)) {
            return list.get(index - 1);
        }
        return list;
    }

    @Override
    public String name() {
        return "values";
    }
}
