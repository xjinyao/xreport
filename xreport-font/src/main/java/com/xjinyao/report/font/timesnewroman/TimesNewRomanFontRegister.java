package com.xjinyao.report.font.timesnewroman;

import com.xjinyao.report.core.export.pdf.font.FontRegister;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class TimesNewRomanFontRegister implements FontRegister {

	@Override
	public String getFontName() {
		return "Times New Roman";
	}

	@Override
	public String getFontPath() {
		return "font/timesnewroman/TIMES.TTF";
	}
}
