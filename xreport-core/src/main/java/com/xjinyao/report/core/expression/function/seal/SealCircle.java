package com.xjinyao.report.core.expression.function.seal;

import lombok.Builder;
import lombok.Getter;

/**
 * @author liwei
 * @createDate 2023-3-2 10:33
 */
@Getter
@Builder
public class SealCircle {
    /**
     * 圆圈线条宽度（粗细）
     */
    private Integer line;
    private Integer width;
    private Integer height;
}
