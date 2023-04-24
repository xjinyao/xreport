package com.xjinyao.report.font.impact;

import com.xjinyao.report.core.export.pdf.font.FontRegister;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ImpactFontRegister implements FontRegister {

	@Override
	public String getFontName() {
		return "Impact";
	}

	@Override
	public String getFontPath() {
		return "font/impact/IMPACT.TTF";
	}
}
