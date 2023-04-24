package com.xjinyao.report.core.image;

import com.xjinyao.report.core.Utils;
import com.xjinyao.report.core.exception.ReportComputeException;
import com.xjinyao.report.core.provider.image.ImageProvider;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class StaticImageProcessor implements ImageProcessor<String> {
	private Logger log = Logger.getGlobal();

	@Override
	public InputStream getImage(String path) {
		Collection<ImageProvider> imageProviders = Utils.getImageProviders();
		ImageProvider targetImageProvider = null;
		for (ImageProvider provider : imageProviders) {
			if (provider.support(path)) {
				targetImageProvider = provider;
				break;
			}
		}
		if (targetImageProvider == null) {
			throw new ReportComputeException("Unsupport image path :" + path);
		}
		try {
			InputStream inputStream = targetImageProvider.getImage(path);
			return inputStream;
		} catch (Exception ex) {
			ApplicationContext applicationContext = Utils.getApplicationContext();
			log.warning("Image [" + path + "] not exist,use default picture.");
			String imageNotExistPath = "classpath:com/bstek/xreport/image/image-not-exist.jpg";
			try {
				return applicationContext.getResource(imageNotExistPath).getInputStream();
			} catch (IOException e1) {
				throw new ReportComputeException(e1);
			}
		}
	}
}
