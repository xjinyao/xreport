package com.xjinyao.report.core.expression.function.seal;

import com.xjinyao.report.core.build.BindData;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.function.Function;
import com.xjinyao.report.core.expression.model.data.BindDataListExpressionData;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectExpressionData;
import com.xjinyao.report.core.model.Cell;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author liwei
 * @createDate 2023-3-3 15:20
 */
public class SealFunction implements Function {
    @Override
    public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
        if (dataList == null || dataList.size() == 0) {
            return null;
        }

        Object rs = null;
        String mainText = "";
        String centerText = "";
        String title = "";

        for (int i = 0; i < dataList.size(); i++) {
            ExpressionData<?> exprData = dataList.get(i);
            if (i == 0 && exprData instanceof BindDataListExpressionData) {
                BindDataListExpressionData data = (BindDataListExpressionData) exprData;
                List<BindData> bindDataList = data.getData();
                for (BindData bindData : bindDataList) {
                    Object obj = bindData.getValue();
                    if (obj == null || StringUtils.isBlank(obj.toString())) {
                        continue;
                    }
                    mainText = mainText.concat(obj.toString());
                }
            } else if (i == 1 && exprData instanceof BindDataListExpressionData) {
                BindDataListExpressionData data = (BindDataListExpressionData) exprData;
                List<BindData> bindDataList = data.getData();
                for (BindData bindData : bindDataList) {
                    Object obj = bindData.getValue();
                    if (obj == null || StringUtils.isBlank(obj.toString())) {
                        continue;
                    }
                    centerText = centerText.concat(obj.toString());
                }
            } else if (i == 2 && exprData instanceof ObjectExpressionData) {
                Object obj = exprData.getData();
                if (obj == null || StringUtils.isBlank(obj.toString())) {
                    continue;
                }
                title = obj.toString();
            }
        }

        if (StringUtils.isEmpty(mainText) || StringUtils.isEmpty(centerText) || StringUtils.isEmpty(title)) {
            return null;
        }

        try {
            rs = Seal.builder()
                    .width(SealConstants.SEAL_WIDTH).height(SealConstants.SEAL_HEIGHT)
                    .borderCircle(SealCircle.builder().line(3).width(170).height(60).build())
                    .mainFont(SealFont.builder()
                            .family("宋体").bold(Boolean.FALSE).text(mainText).size(16).space(12.0).margin(10)
                            .build())
                    .centerFont(SealFont.builder()
                            .family("宋体").bold(Boolean.FALSE).text(centerText).size(14).margin(8)
                            .build())
                    .centerSignFont(SealFont.builder()
                            .family("宋体").bold(Boolean.FALSE).text(centerText + "项目收费处").size(14).margin(-12)
                            .build())
                    .titleFont(SealFont.builder()
                            .family("宋体").bold(Boolean.FALSE).text(title).size(14).space(9.0).margin(44)
                            .build())
                    .build()
                    .drawOfficialSeal();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return rs;
    }

    @Override
    public String name() {
        return "seal";
    }
}
