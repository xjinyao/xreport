package com.xjinyao.report.core.definition.dataset;

import java.io.Serializable;
import java.util.List;


/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface DatasetDefinition extends Serializable {
	String getName();

	List<Field> getFields();
}
