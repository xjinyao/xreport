package com.xjinyao.report.action.designer;

import com.xjinyao.report.action.exception.ReportDesignException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ReportUtils {
	public static String decodeFileName(String fileName) {
		if (fileName == null) {
			return fileName;
		}
		try {
			fileName = URLDecoder.decode(fileName, "utf-8");
			fileName = URLDecoder.decode(fileName, "utf-8");
			return fileName;
		} catch (UnsupportedEncodingException e) {
			throw new ReportDesignException(e);
		}
	}
}
