package com.xjinyao.report.font.comicsansms;

import com.xjinyao.report.core.export.pdf.font.FontRegister;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ComicSansMSFontRegister implements FontRegister {

	@Override
	public String getFontName() {
		return "Comic Sans MS";
	}

	@Override
	public String getFontPath() {
		return "font/comicsansms/COMIC.TTF";
	}
}
