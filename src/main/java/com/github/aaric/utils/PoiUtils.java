package com.github.aaric.utils;

import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellUtil;

/**
 * POI工具类
 * 
 * @author Aaric
 * @since 2017-03-20
 */
public final class PoiUtils {

	/**
	 * 日期格式化对象
	 */
	private static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 日期时间格式化对象
	 */
	protected static final SimpleDateFormat DATE_FORMAT_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");

	/**
	 * 私有构造函数
	 */
	private PoiUtils() {
		super();
	}

	/**
	 * 获得单元格对象
	 * 
	 * @param sheet
	 *            Sheet对象
	 * @param rowIndex
	 *            行号
	 * @param columnIndex
	 *            列号
	 * @return
	 * @author Aaric
	 * @since 2017-03-20
	 */
	public static Cell getCell(Sheet sheet, int rowIndex, int columnIndex) {
		return CellUtil.getCell(CellUtil.getRow(rowIndex, sheet), columnIndex);
	}

	/**
	 * 获得单元格内容
	 * 
	 * @param sheet
	 *            Sheet对象
	 * @param rowIndex
	 *            行号
	 * @param columnIndex
	 *            列号
	 * @return
	 * @author Aaric
	 * @since 2017-03-20
	 */
	public static Object getCellContent(Sheet sheet, int rowIndex, int columnIndex) {
		return getContent(getCell(sheet, rowIndex, columnIndex));
	}

	/**
	 * 获得单元格内容
	 * 
	 * @param cell
	 *            单元格
	 * @return
	 * @author Aaric
	 * @since 2017-03-20
	 */
	public static Object getContent(Cell cell) {
		if (null != cell) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				return cell.getRichStringCellValue().toString();
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					return DATE_FORMAT_DATE.format(cell.getDateCellValue());
				} else {
					return cell.getNumericCellValue();
				}
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue();
			case Cell.CELL_TYPE_FORMULA:
				return cell.getCellFormula();
			}
		}
		return "";
	}

	/**
	 * 获得单元格的整型数值
	 * 
	 * @param sheet
	 *            Sheet对象
	 * @param rowIndex
	 *            行号
	 * @param columnIndex
	 *            列号
	 * @return
	 * @author Aaric
	 * @since 2017-03-20
	 */
	public static Integer getCellInteger(Sheet sheet, int rowIndex, int columnIndex) {
		try {
			Cell cell = getCell(sheet, rowIndex, columnIndex);
			if (null != cell && Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
				return Double.valueOf(cell.getNumericCellValue()).intValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得单元格的字符串
	 * 
	 * @param sheet
	 *            Sheet对象
	 * @param rowIndex
	 *            行号
	 * @param columnIndex
	 *            列号
	 * @return
	 * @author Aaric
	 * @since 2017-03-20
	 */
	public static String getCellString(Sheet sheet, int rowIndex, int columnIndex) {
		try {
			Object content = getCellContent(sheet, rowIndex, columnIndex);
			if (null != content) {
				return StringUtils.trim(String.valueOf(content).replaceAll("\\.0+$", ""));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
