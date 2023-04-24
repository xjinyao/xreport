package com.xjinyao.report.font.heiti;


import com.xjinyao.report.core.export.pdf.font.FontRegister;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class HeiTiFontRegister implements FontRegister {

	@Override
	public String getFontName() {
		return "黑体";
	}

	@Override
	public String getFontPath() {
		return "font/heiti/SIMHEI.TTF";
	}
}
