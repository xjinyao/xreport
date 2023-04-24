package com.xjinyao.report.font.yahei;

import com.xjinyao.report.core.export.pdf.font.FontRegister;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class YaheiFontRegister implements FontRegister {

	@Override
	public String getFontName() {
		return "微软雅黑";
	}

	@Override
	public String getFontPath() {
		return "font/yahei/msyh.ttc";
	}
}
