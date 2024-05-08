package com.xjinyao.report.core.config;

import com.xjinyao.report.core.Utils;
import com.xjinyao.report.core.build.HideRowColumnBuilder;
import com.xjinyao.report.core.build.ReportBuilder;
import com.xjinyao.report.core.cache.CacheUtils;
import com.xjinyao.report.core.export.ExportManagerImpl;
import com.xjinyao.report.core.export.ReportRender;
import com.xjinyao.report.core.export.pdf.font.FontBuilder;
import com.xjinyao.report.core.expression.ExpressionUtils;
import com.xjinyao.report.core.expression.function.*;
import com.xjinyao.report.core.expression.function.date.*;
import com.xjinyao.report.core.expression.function.math.*;
import com.xjinyao.report.core.expression.function.page.*;
import com.xjinyao.report.core.expression.function.seal.SealFunction;
import com.xjinyao.report.core.expression.function.string.*;
import com.xjinyao.report.core.parser.ReportParser;
import com.xjinyao.report.core.parser.impl.searchform.*;
import com.xjinyao.report.core.properties.XReportProperties;
import com.xjinyao.report.core.provider.image.Base64ImageProvider;
import com.xjinyao.report.core.provider.image.DefaultImageProvider;
import com.xjinyao.report.core.provider.image.HttpImageProvider;
import com.xjinyao.report.core.provider.image.HttpsImageProvider;
import com.xjinyao.report.core.provider.report.classpath.ClasspathReportProvider;
import com.xjinyao.report.core.provider.report.file.FileReportProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

/**
 * @author 谢进伟
 * @createDate 2023/4/24 09:46
 */
@PropertySource("classpath:xreport.properties")
@EnableConfigurationProperties(XReportProperties.class)
public class CoreAutoConfiguration {

	@Bean
	public ExportManagerImpl exportManagerImpl(ReportRender reportRender) {
		ExportManagerImpl exportManager = new ExportManagerImpl();
		exportManager.setReportRender(reportRender);
		return exportManager;
	}

	@Bean
	public ReportRender reportRender(ReportParser reportParser, ReportBuilder reportBuilder) {
		ReportRender reportRender = new ReportRender();
		reportRender.setReportParser(reportParser);
		reportRender.setReportBuilder(reportBuilder);
		return reportRender;
	}

	@Bean
	public DefaultImageProvider defaultImageProvider() {
		return new DefaultImageProvider();
	}

	@Bean
	public FileReportProvider fileReportProvider(XReportProperties xReportProperties) {
		FileReportProvider fileReportProvider = new FileReportProvider();
		fileReportProvider.setFileStoreDir(xReportProperties.getFileStoreDir());
		fileReportProvider.setDisabled(xReportProperties.getDisableFileProvider());
		return fileReportProvider;
	}

	@Bean
	public HttpImageProvider httpImageProvider() {
		return new HttpImageProvider();
	}

	@Bean
	public HttpsImageProvider httpsImageProvider() {
		return new HttpsImageProvider();
	}

	@Bean
	public Base64ImageProvider base64ImageProvider() {
		return new Base64ImageProvider();
	}

	@Bean
	public ReportBuilder reportBuilder(HideRowColumnBuilder hideRowColumnBuilder) {
		ReportBuilder reportBuilder = new ReportBuilder();
		reportBuilder.setHideRowColumnBuilder(hideRowColumnBuilder);
		return reportBuilder;
	}

	@Bean
	public HideRowColumnBuilder hideRowColumnBuilder() {
		return new HideRowColumnBuilder();
	}

	@Bean
	public ReportParser reportParser() {
		return new ReportParser();
	}

	@Bean
	public FormParserUtils formParserUtils() {
		return new FormParserUtils();
	}

	@Bean
	public CheckboxParser checkboxParser() {
		return new CheckboxParser();
	}

	@Bean
	public GridParser gridParser() {
		return new GridParser();
	}

	@Bean
	public RadioInputParser radioInputParser() {
		return new RadioInputParser();
	}

	@Bean
	public TextInputParser textInputParser() {
		return new TextInputParser();
	}

	@Bean
	public ResetButtonParser resetButtonParser() {
		return new ResetButtonParser();
	}

	@Bean
	public SubmitButtonParser submitButtonParser() {
		return new SubmitButtonParser();
	}

	@Bean
	public SelectInputParser selectInputParser() {
		return new SelectInputParser();
	}

	@Bean
	public DatetimeInputParser datetimeInputParser() {
		return new DatetimeInputParser();
	}

	@Bean
	public ClasspathReportProvider classpathReportProvider() {
		return new ClasspathReportProvider();
	}

	@Bean
	public FontBuilder fontBuilder(XReportProperties xReportProperties) {
		FontBuilder fontBuilder = new FontBuilder();
		fontBuilder.setLoadSystemFont(xReportProperties.getLoadSystemFont());
		return fontBuilder;
	}

	@Bean
	public ExpressionUtils expressionUtils() {
		return new ExpressionUtils();
	}

	@Bean
	public Utils utils(XReportProperties xReportProperties) {
		Utils utils = new Utils();
		utils.setDebug(xReportProperties.getDebug());
		return utils;
	}

	@Bean
	public CacheUtils cacheUtils() {
		return new CacheUtils();
	}


	@Bean
	public CountFunction countFunction() {
		return new CountFunction();
	}

	@Bean
	public SumFunction sumFunction() {
		return new SumFunction();
	}

	@Bean
	public MaxFunction maxFunction() {
		return new MaxFunction();
	}

	@Bean
	public MinFunction minFunction() {
		return new MinFunction();
	}

	@Bean
	public ListFunction listFunction() {
		return new ListFunction();
	}

	@Bean
	public ValuesFunction listValueFunction() {
		return new ValuesFunction();
	}

	@Bean
	public AvgFunction avgFunction() {
		return new AvgFunction();
	}

	@Bean
	public OrderFunction orderFunction() {
		return new OrderFunction();
	}

	@Bean
	public WeekFunction weekFunction() {
		return new WeekFunction();
	}

	@Bean
	public DayFunction dayFunction() {
		return new DayFunction();
	}

	@Bean
	public MonthFunction monthFunction() {
		return new MonthFunction();
	}

	@Bean
	public YearFunction yearFunction() {
		return new YearFunction();
	}

	@Bean
	public DateFunction dateFunction() {
		return new DateFunction();
	}

	@Bean
	public FormatNumberFunction formatNumberFunction() {
		return new FormatNumberFunction();
	}

	@Bean
	public FormatDateFunction formatDateFunction() {
		return new FormatDateFunction();
	}

	@Bean
	public GetFunction getFunction() {
		return new GetFunction();
	}

	@Bean
	public AbsFunction absFunction() {
		return new AbsFunction();
	}

	@Bean
	public CeilFunction ceilFunction() {
		return new CeilFunction();
	}

	@Bean
	public ChnFunction chnFunction() {
		return new ChnFunction();
	}

	@Bean
	public ChnMoneyFunction chnMoneyFunction() {
		return new ChnMoneyFunction();
	}

	@Bean
	public CosFunction cosFunction() {
		return new CosFunction();
	}

	@Bean
	public ExpFunction expFunction() {
		return new ExpFunction();
	}

	@Bean
	public FloorFunction floorFunction() {
		return new FloorFunction();
	}

	@Bean
	public Log10Function log10Function() {
		return new Log10Function();
	}

	@Bean
	public LogFunction logFunction() {
		return new LogFunction();
	}

	@Bean
	public PowFunction powFunction() {
		return new PowFunction();
	}

	@Bean
	public RandomFunction randomFunction() {
		return new RandomFunction();
	}

	@Bean
	public RoundFunction roundFunction() {
		return new RoundFunction();
	}

	@Bean
	public SinFunction sinFunction() {
		return new SinFunction();
	}

	@Bean
	public SqrtFunction sqrtFunction() {
		return new SqrtFunction();
	}

	@Bean
	public TanFunction tanFunction() {
		return new TanFunction();
	}

	@Bean
	public StdevpFunction stdevpFunction() {
		return new StdevpFunction();
	}

	@Bean
	public VaraFunction varaFunction() {
		return new VaraFunction();
	}

	@Bean
	public ModeFunction modeFunction() {
		return new ModeFunction();
	}

	@Bean
	public MedianFunction medianFunction() {
		return new MedianFunction();
	}

	@Bean
	public LengthFunction lengthFunction() {
		return new LengthFunction();
	}

	@Bean
	public LowerFunction lowerFunction() {
		return new LowerFunction();
	}

	@Bean
	public IndexOfFunction indexOfFunction() {
		return new IndexOfFunction();
	}

	@Bean
	public ReplaceFunction replaceFunction() {
		return new ReplaceFunction();
	}

	@Bean
	public SubstringFunction substringFunction() {
		return new SubstringFunction();
	}

	@Bean
	public TrimFunction trimFunction() {
		return new TrimFunction();
	}

	@Bean
	public UpperFunction upperFunction() {
		return new UpperFunction();
	}

	@Bean
	public PageTotalFunction pageTotalFunction() {
		return new PageTotalFunction();
	}

	@Bean
	public PageNumberFunction pageNumberFunction() {
		return new PageNumberFunction();
	}

	@Bean
	public PageAvgFunction pageAvgFunction() {
		return new PageAvgFunction();
	}

	@Bean
	public PageCountFunction pageCountFunction() {
		return new PageCountFunction();
	}

	@Bean
	public PageMaxFunction pageMaxFunction() {
		return new PageMaxFunction();
	}

	@Bean
	public PageMinFunction pageMinFunction() {
		return new PageMinFunction();
	}

	@Bean
	public PageRowsFunction pageRowsFunction() {
		return new PageRowsFunction();
	}

	@Bean
	public PageSumFunction pageSumFunction() {
		return new PageSumFunction();
	}

	@Bean
	public ParameterFunction parameterFunction() {
		return new ParameterFunction();
	}

	@Bean
	public ParameterIsEmptyFunction parameterIsEmptyFunction() {
		return new ParameterIsEmptyFunction();
	}

	@Bean
	public JsonFunction jsonFunction() {
		return new JsonFunction();
	}

	@Bean
	public RowFunction rowFunction() {
		return new RowFunction();
	}

	@Bean
	public ColumnFunction columnFunction() {
		return new ColumnFunction();
	}

	@Bean
	public SealFunction sealFunction() {
		return new SealFunction();
	}
}
