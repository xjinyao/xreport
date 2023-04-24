package com.xjinyao.report.font.couriernew;

import com.xjinyao.report.core.export.pdf.font.FontRegister;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class CourierNewFontRegister implements FontRegister {

	@Override
	public String getFontName() {
		return "Courier New";
	}

	@Override
	public String getFontPath() {
		return "font/couriernew/COUR.TTF";
	}
}
