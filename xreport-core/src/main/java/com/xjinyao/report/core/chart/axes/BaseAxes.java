package com.xjinyao.report.core.chart.axes;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public abstract class BaseAxes implements Axes {
	private int rotation;
	private ScaleLabel scaleLabel;

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public ScaleLabel getScaleLabel() {
		return scaleLabel;
	}

	public void setScaleLabel(ScaleLabel scaleLabel) {
		this.scaleLabel = scaleLabel;
	}
}
