package com.xjinyao.report.font.fangsong;

import com.xjinyao.report.core.export.pdf.font.FontRegister;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class FangSongFontRegister implements FontRegister {

	@Override
	public String getFontName() {
		return "仿宋";
	}

	@Override
	public String getFontPath() {
		return "font/fangsong/SIMFANG.TTF";
	}
}
