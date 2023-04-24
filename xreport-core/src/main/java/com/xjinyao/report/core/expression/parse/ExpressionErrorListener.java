package com.xjinyao.report.core.expression.parse;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ExpressionErrorListener extends BaseErrorListener {
	private StringBuffer sb;

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
							int line, int charPositionInLine,
							String msg, RecognitionException e) {
		if (sb == null) {
			sb = new StringBuffer();
		}
		sb.append("[" + offendingSymbol + "] is invalid:" + msg);
		sb.append("\r\n");
	}

	public String getErrorMessage() {
		if (sb == null) {
			return null;
		}
		return sb.toString();
	}
}
