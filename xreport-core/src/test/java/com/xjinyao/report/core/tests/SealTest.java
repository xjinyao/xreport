package com.xjinyao.report.core.tests;

import com.xjinyao.report.core.expression.function.seal.Seal;
import com.xjinyao.report.core.expression.function.seal.SealCircle;
import com.xjinyao.report.core.expression.function.seal.SealConstants;
import com.xjinyao.report.core.expression.function.seal.SealFont;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author 谢进伟
 * @createDate 2023/6/2 17:27
 */
public class SealTest {

    public static void main(String[] args) throws Exception {
        String mainText = "测试一波";
        String centerText = "";
        String title = "";

        String base64ImageStr = Seal.builder()
                .width(SealConstants.SEAL_WIDTH).height(SealConstants.SEAL_HEIGHT)
                .borderCircle(SealCircle.builder().line(3).width(170).height(60).build())
                .mainFont(SealFont.builder()
                        .family("Songti TC").bold(Boolean.FALSE).text(mainText).size(16).space(12.0).margin(10)
                        .build())
                .centerFont(SealFont.builder()
                        .family("Songti TC").bold(Boolean.FALSE).text(centerText).size(14).margin(8)
                        .build())
                .centerSignFont(SealFont.builder()
                        .family("Songti TC").bold(Boolean.FALSE).text(centerText).size(14).margin(-12)
                        .build())
                .titleFont(SealFont.builder()
                        .family("Songti TC").bold(Boolean.FALSE).text(title).size(14).space(9.0).margin(44)
                        .build())
                .build()
                .drawOfficialSeal();

        System.out.println(base64ImageStr);

        File file = new File("/Users/yang/Downloads/test.png");
        System.out.println(file.getPath());
        FileUtils.writeByteArrayToFile(file,
                base64ImageStr.getBytes(StandardCharsets.UTF_8));
    }
}
