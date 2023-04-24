package com.xjinyao.report.core.expression.function.seal;

import lombok.Builder;
import lombok.Getter;

/**
 * @author liwei
 * @createDate 2023-3-2 10:34
 */
@Getter
@Builder
public class SealFont {
    private String text;
    private String family;
    private Integer size;
    private Boolean bold;
    private Double space;
    private Integer margin;

    public String getFamily() {
        return family == null ? "宋体" : family;
    }

    public boolean getBold() {
        return bold == null || bold;
    }

    public SealFont append(String text) {
        this.text += text;
        return this;
    }
}
