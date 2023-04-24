package com.xjinyao.report.core.build;

import com.xjinyao.report.core.build.compute.*;
import com.xjinyao.report.core.definition.value.Value;
import com.xjinyao.report.core.exception.ReportException;
import com.xjinyao.report.core.model.Cell;
import com.xjinyao.report.core.build.compute.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class DataCompute {
	private static Map<String, ValueCompute> valueComputesMap = new HashMap<>();

	static {
		SimpleValueCompute simpleValueCompute = new SimpleValueCompute();
		valueComputesMap.put(simpleValueCompute.type().name(), simpleValueCompute);
		DatasetValueCompute datasetValueCompute = new DatasetValueCompute();
		valueComputesMap.put(datasetValueCompute.type().name(), datasetValueCompute);
		ExpressionValueCompute expressionValueCompute = new ExpressionValueCompute();
		valueComputesMap.put(expressionValueCompute.type().name(), expressionValueCompute);
		ImageValueCompute imageValueCompute = new ImageValueCompute();
		valueComputesMap.put(imageValueCompute.type().name(), imageValueCompute);
		SlashValueCompute slashValueCompute = new SlashValueCompute();
		valueComputesMap.put(slashValueCompute.type().name(), slashValueCompute);
		ZxingValueCompute zxingValueCompute = new ZxingValueCompute();
		valueComputesMap.put(zxingValueCompute.type().name(), zxingValueCompute);
		ChartValueCompute chartValueCompute = new ChartValueCompute();
		valueComputesMap.put(chartValueCompute.type().name(), chartValueCompute);

	}

	public static List<BindData> buildCellData(Cell cell, Context context) {
		context.resetVariableMap();
		Value value = cell.getValue();
		ValueCompute valueCompute = valueComputesMap.get(value.getType().name());
		if (valueCompute != null) {
			List<BindData> list = valueCompute.compute(cell, context);
			return list;
		}
		throw new ReportException("Unsupport value: " + value);
	}
}
