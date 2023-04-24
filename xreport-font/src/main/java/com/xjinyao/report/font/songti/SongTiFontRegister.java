package com.xjinyao.report.font.songti;

import com.xjinyao.report.core.export.pdf.font.FontRegister;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class SongTiFontRegister implements FontRegister {

	@Override
	public String getFontName() {
		return "宋体";
	}

	@Override
	public String getFontPath() {
		return "font/songti/SIMSUN.TTC";
	}
}
