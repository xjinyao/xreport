package com.xjinyao.report.core.provider.image;

import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class Base64ImageProvider implements ImageProvider {

    @Override
    public InputStream getImage(String content) {
        if (StringUtils.isBlank(content)) {
            return new ByteArrayInputStream(new byte[0]);
        }
        return new ByteArrayInputStream(content.getBytes());
    }

    @Override
    public boolean support(String content) {
        if (StringUtils.isBlank(content)) {
            return false;
        }
        return content.startsWith("data:image/png;base64,") ||
                content.startsWith("data:image/jpeg;base64,");
    }

}
