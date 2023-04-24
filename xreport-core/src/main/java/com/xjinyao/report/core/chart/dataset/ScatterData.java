package com.xjinyao.report.core.chart.dataset;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ScatterData {
	private double x;
	private double y;

	public ScatterData(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
