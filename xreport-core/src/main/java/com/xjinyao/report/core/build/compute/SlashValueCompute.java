package com.xjinyao.report.core.build.compute;

import com.xjinyao.report.core.build.BindData;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.definition.value.SlashValue;
import com.xjinyao.report.core.definition.value.ValueType;
import com.xjinyao.report.core.model.Cell;
import com.xjinyao.report.core.model.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class SlashValueCompute implements ValueCompute {
	@Override
	public List<BindData> compute(Cell cell, Context context) {
		List<BindData> list = new ArrayList<>();
		SlashValue v = (SlashValue) cell.getValue();
		Image img = new Image(v.getBase64Data(), "slash.png", 0, 0);
		BindData bindData = new BindData(img);
		list.add(bindData);
		/*String key=SlashBuilder.buildKey(context.getReport().getReportFullName(), cell.getName());
		Resource res=new Resource(key);
		BindData bindData=new BindData(res);
		list.add(bindData);*/
		return list;
	}


	@Override
	public ValueType type() {
		return ValueType.slash;
	}
}
