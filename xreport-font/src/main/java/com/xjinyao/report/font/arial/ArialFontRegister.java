package com.xjinyao.report.font.arial;

import com.xjinyao.report.core.export.pdf.font.FontRegister;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ArialFontRegister implements FontRegister {

	@Override
	public String getFontName() {
		return "Arial";
	}

	@Override
	public String getFontPath() {
		return "font/arial/ARIAL.TTF";
	}
}
