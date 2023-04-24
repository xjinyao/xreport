package com.xjinyao.report.core;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.definition.datasource.BuiltinDatasource;
import com.xjinyao.report.core.definition.datasource.connection.ConnectionInfo;
import com.xjinyao.report.core.definition.datasource.connection.DatasourceConnection;
import com.xjinyao.report.core.exception.ConvertException;
import com.xjinyao.report.core.exception.ReportComputeException;
import com.xjinyao.report.core.model.Cell;
import com.xjinyao.report.core.model.Report;
import com.xjinyao.report.core.provider.image.ImageProvider;
import com.xjinyao.report.core.utils.JdbcUrlUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class Utils implements ApplicationContextAware {
	private static ApplicationContext applicationContext;
	private static Collection<BuiltinDatasource> builtinDatasources;
	private static Collection<ImageProvider> imageProviders;
	private static boolean debug;

	public static boolean isDebug() {
		return Utils.debug;
	}

	public void setDebug(boolean debug) {
		Utils.debug = debug;
	}

	public static void logToConsole(String msg) {
		if (Utils.debug) {
			System.out.println(msg);
		}
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Utils.applicationContext = applicationContext;
		builtinDatasources = new ArrayList<>();
		builtinDatasources.addAll(applicationContext.getBeansOfType(BuiltinDatasource.class).values());
		imageProviders = new ArrayList<>();
		imageProviders.addAll(applicationContext.getBeansOfType(ImageProvider.class).values());
	}

	public static Collection<BuiltinDatasource> getBuildinDatasources() {
		return builtinDatasources;
	}

	public static Collection<ImageProvider> getImageProviders() {
		return imageProviders;
	}

	public static DatasourceConnection getBuildinConnection(String name) {
		for (BuiltinDatasource datasource : builtinDatasources) {
			if (name.equals(datasource.name())) {
				Connection conn = datasource.getConnection();
				DataSourceProperties properties = datasource.getProperties();
				return DatasourceConnection.builder()
						.connection(conn)
						.connectionInfo(ConnectionInfo.builder()
								.username(properties.getUsername())
								.password(properties.getPassword())
								.driver(properties.getDriverClassName())
								.url(properties.getUrl())
								.urlPartInfo(JdbcUrlUtils.getJdbcUrlParts(properties.getUrl()))
								.build())
						.build();
			}
		}
		return null;
	}

	public static List<Cell> fetchTargetCells(Cell cell, Context context, String cellName) {
		while (!context.isCellPocessed(cellName)) {
			context.getReportBuilder().buildCell(context, null);
		}
		List<Cell> leftCells = fetchCellsByLeftParent(context, cell, cellName);
		List<Cell> topCells = fetchCellsByTopParent(context, cell, cellName);
		if (leftCells != null && topCells != null) {
			int leftSize = leftCells.size(), topSize = topCells.size();
			if (leftSize == 1 || topSize == 0) {
				return leftCells;
			}
			if (topSize == 1 || leftSize == 0) {
				return topCells;
			}
			if (leftSize == 0 && topSize == 0) {
				return new ArrayList<>();
			}
			List<Cell> list = new ArrayList<>();
			if (leftSize <= topSize) {
				for (Cell c : leftCells) {
					if (topCells.contains(c)) {
						list.add(c);
					}
				}
			} else {
				for (Cell c : topCells) {
					if (leftCells.contains(c)) {
						list.add(c);
					}
				}
			}
			return list;
		} else if (leftCells != null && topCells == null) {
			return leftCells;
		} else if (leftCells == null && topCells != null) {
			return topCells;
		} else {
			Report report = context.getReport();
			return report.getCellsMap().get(cellName);
		}
	}

	private static List<Cell> fetchCellsByLeftParent(Context context, Cell cell, String cellName) {
		Cell leftParentCell = cell.getLeftParentCell();
		if (leftParentCell == null) {
			return null;
		}
		if (leftParentCell.getName().equals(cellName)) {
			List<Cell> list = new ArrayList<>();
			list.add(leftParentCell);
			return list;
		}
		Map<String, List<Cell>> childrenCellsMap = leftParentCell.getRowChildrenCellsMap();
		List<Cell> targetCells = childrenCellsMap.get(cellName);
		if (targetCells != null) {
			return targetCells;
		}
		return fetchCellsByLeftParent(context, leftParentCell, cellName);
	}

	private static List<Cell> fetchCellsByTopParent(Context context, Cell cell, String cellName) {
		Cell topParentCell = cell.getTopParentCell();
		if (topParentCell == null) {
			return null;
		}
		if (topParentCell.getName().equals(cellName)) {
			List<Cell> list = new ArrayList<>();
			list.add(topParentCell);
			return list;
		}
		Map<String, List<Cell>> childrenCellsMap = topParentCell.getColumnChildrenCellsMap();
		List<Cell> targetCells = childrenCellsMap.get(cellName);
		if (targetCells != null) {
			return targetCells;
		}
		return fetchCellsByTopParent(context, topParentCell, cellName);
	}

	public static Object getProperty(Object obj, String property) {
		if (obj == null) return null;
		try {
			if (obj instanceof Map && property.indexOf(".") == -1) {
				Map<?, ?> map = (Map<?, ?>) obj;
				return map.get(property);
			}
			return PropertyUtils.getProperty(obj, property);
		} catch (Exception ex) {
			throw new ReportComputeException(ex);
		}
	}

	public static Date toDate(Object obj) {
		if (obj instanceof Date) {
			return (Date) obj;
		} else if (obj instanceof String) {
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return sd.parse(obj.toString());
			} catch (Exception ex) {
				sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					return sd.parse(obj.toString());
				} catch (Exception e) {
					throw new ReportComputeException("Can not convert " + obj + " to Date.");
				}
			}
		}
		throw new ReportComputeException("Can not convert " + obj + " to Date.");
	}

	public static BigDecimal toBigDecimal(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof BigDecimal) {
			return (BigDecimal) obj;
		} else if (obj instanceof String) {
			if (obj.toString().trim().equals("")) {
				return new BigDecimal(0);
			}
			try {
				String str = obj.toString().trim();
				return new BigDecimal(str);
			} catch (Exception ex) {
				throw new ConvertException("Can not convert " + obj + " to BigDecimal.");
			}
		} else if (obj instanceof Number) {
			Number n = (Number) obj;
			return new BigDecimal(n.doubleValue());
		}
		throw new ConvertException("Can not convert " + obj + " to BigDecimal.");
	}
}
