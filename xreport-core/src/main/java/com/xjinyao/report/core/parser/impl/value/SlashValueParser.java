package com.xjinyao.report.core.parser.impl.value;

import com.xjinyao.report.core.definition.value.Slash;
import com.xjinyao.report.core.definition.value.SlashValue;
import com.xjinyao.report.core.definition.value.Value;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class SlashValueParser extends ValueParser {
	@Override
	public Value parse(Element element) {
		SlashValue value = new SlashValue();
		List<Slash> slashes = new ArrayList<>();
		value.setSlashes(slashes);
		for (Object obj : element.elements()) {
			if (obj == null || !(obj instanceof Element)) {
				continue;
			}
			Element ele = (Element) obj;
			if (ele.getName().equals("slash")) {
				Slash slash = new Slash();
				slashes.add(slash);
				slash.setDegree(Integer.valueOf(ele.attributeValue("degree")));
				slash.setX(Integer.valueOf(ele.attributeValue("x")));
				slash.setY(Integer.valueOf(ele.attributeValue("y")));
				slash.setText(ele.attributeValue("text"));
			} else if (ele.getName().equals("base64-data")) {
				String prefix = "data:image/png;base64,";
				String base64Data = ele.getText();
				if (base64Data.startsWith(prefix)) {
					base64Data = base64Data.substring(prefix.length(), base64Data.length());
				}
				value.setBase64Data(base64Data);
			}
		}
		return value;
	}
}
