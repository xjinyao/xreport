package com.xjinyao.report.core.export.pdf.font;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface FontRegister {
	/**
	 * @return 返回自定义的字体名称
	 */
	String getFontName();

	/**
	 * 返回字体所在位置，需要注意的是字体文件需要放置到classpath下，这里返回的值就是该字体文件所在classpath下位置即可
	 *
	 * @return 返回字体所在位置
	 */
	String getFontPath();
}
