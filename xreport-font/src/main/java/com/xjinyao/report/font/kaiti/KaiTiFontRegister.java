package com.xjinyao.report.font.kaiti;

import com.xjinyao.report.core.export.pdf.font.FontRegister;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class KaiTiFontRegister implements FontRegister {

	@Override
	public String getFontName() {
		return "楷体";
	}

	@Override
	public String getFontPath() {
		return "font/kaiti/SIMKAI.TTF";
	}
}
