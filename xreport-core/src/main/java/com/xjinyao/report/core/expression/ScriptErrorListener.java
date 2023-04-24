package com.xjinyao.report.core.expression;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ScriptErrorListener extends BaseErrorListener {
	private List<ErrorInfo> infos = new ArrayList<>();

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
		infos.add(new ErrorInfo(line, charPositionInLine, msg));
	}

	public List<ErrorInfo> getInfos() {
		return infos;
	}
}
