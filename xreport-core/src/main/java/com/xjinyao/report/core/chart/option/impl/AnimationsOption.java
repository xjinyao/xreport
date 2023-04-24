package com.xjinyao.report.core.chart.option.impl;

import com.xjinyao.report.core.chart.option.Easing;
import com.xjinyao.report.core.chart.option.Option;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class AnimationsOption implements Option {
	private int duration = 1000;
	private Easing easing = Easing.easeOutQuart;

	@Override
	public String buildOptionJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("\"animation\":{");
		sb.append("\"duration\":" + duration + ",");
		sb.append("\"easing\":\"" + easing + "\"");
		sb.append("}");
		return sb.toString();
	}

	@Override
	public String getType() {
		return "animation";
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Easing getEasing() {
		return easing;
	}

	public void setEasing(Easing easing) {
		this.easing = easing;
	}
}
