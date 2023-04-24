package com.xjinyao.report.config;

import com.xjinyao.report.font.arial.ArialFontRegister;
import com.xjinyao.report.font.comicsansms.ComicSansMSFontRegister;
import com.xjinyao.report.font.couriernew.CourierNewFontRegister;
import com.xjinyao.report.font.fangsong.FangSongFontRegister;
import com.xjinyao.report.font.heiti.HeiTiFontRegister;
import com.xjinyao.report.font.kaiti.KaiTiFontRegister;
import com.xjinyao.report.font.songti.SongTiFontRegister;
import com.xjinyao.report.font.timesnewroman.TimesNewRomanFontRegister;
import com.xjinyao.report.font.yahei.YaheiFontRegister;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author 谢进伟
 * @createDate 2023/4/24 09:46
 */
@EnableConfigurationProperties
public class FontAutoConfiguration {

	@Bean
	public ArialFontRegister arialFontRegister() {
		return new ArialFontRegister();
	}

	@Bean
	public ComicSansMSFontRegister comicSansMSFontRegister() {
		return new ComicSansMSFontRegister();
	}

	@Bean
	public CourierNewFontRegister courierNewFontRegister() {
		return new CourierNewFontRegister();
	}

	@Bean
	public FangSongFontRegister fangSongFontRegister() {
		return new FangSongFontRegister();
	}

	@Bean
	public HeiTiFontRegister heiTiFontRegister() {
		return new HeiTiFontRegister();
	}

	@Bean
	public KaiTiFontRegister kaiTiFontRegister() {
		return new KaiTiFontRegister();
	}

	@Bean
	public SongTiFontRegister songTiFontRegister() {
		return new SongTiFontRegister();
	}

	@Bean
	public TimesNewRomanFontRegister timesNewRomanFontRegister() {
		return new TimesNewRomanFontRegister();
	}

	@Bean
	public YaheiFontRegister yaheiFontRegister() {
		return new YaheiFontRegister();
	}
}
