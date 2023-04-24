package com.xjinyao.report.core;

import java.io.Serializable;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class Range implements Serializable {
	private static final long serialVersionUID = -4547468301777433024L;
	private int start = -1;
	private int end;

	public Range() {
	}

	public Range(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
}
