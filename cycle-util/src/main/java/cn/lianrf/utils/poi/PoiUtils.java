package cn.lianrf.utils.poi;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * poi工具类
 * @version: v1.0
 * @date: 2019/4/11
 * @author: lianrf
 */
public abstract class PoiUtils {

    public static void setCellValue(Cell cell, Object value){
        if(value instanceof String){
            cell.setCellValue((String)value);
        }else if(value instanceof Date){
            cell.setCellValue((Date)value);
        }else if(value instanceof Double){
            cell.setCellValue((Double)value);
        }else if(value instanceof Boolean){
            cell.setCellValue((Boolean)value);
        }else if(value instanceof Integer){
            cell.setCellValue((Integer)value);
        }
    }

    /**
     * 合并单元格方法
     * @param sheet
     * @param regions
     */
    public static void doMergedRegion(Sheet sheet, List<CellRangeAddress> regions){
        if(regions!=null){
            for (CellRangeAddress region : regions) {
                sheet.addMergedRegion(region);
            }
        }
    }

    /**
     * 获得或创建cell
     * @param row
     * @param index
     * @return
     */
    public static Cell getOrCreateCell(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) {
            cell=row.createCell(index);
        }
        return cell;
    }
    /**
     * 获取或新建row
     * @param sheet
     * @param rowIndex
     * @return
     */
    public static Row getOrcreateRow(Sheet sheet, Integer rowIndex) {
        Row row = sheet.getRow(rowIndex);
        if(row==null){
            row = sheet.createRow(rowIndex);
        }
        return row;
    }

    /**
     * 字体粗
     * @param workbook
     * @return
     */
    public static CellStyle fontBStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        //粗体显示

        font.setBold(true);
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 设置单元格值 并且居中
     * @return
     */
    public static Cell setCellValueCenter(Workbook workbook, Sheet sheet, int rowIdx, int cellidx, Object value,short fontSize) {
        CellStyle centerStyle = centerStyle(workbook,fontSize);
        Row sheetRow = getOrcreateRow(sheet,rowIdx);
        Cell cell = getOrCreateCell(sheetRow, cellidx);
        cell.setCellStyle(centerStyle);
        setCellValue(cell,value);
        return cell;
    }

    /**
     * 设置单元格值
     *
     * @return
     */
    public static Cell setCellValue(Sheet sheet, int rowIdx, int cellidx, Object value) {
        Row sheetRow = getOrcreateRow(sheet,rowIdx);
        Cell cell = getOrCreateCell(sheetRow, cellidx);
        setCellValue(cell,value);
        return cell;
    }

    /**
     * 居中样式
     *
     * @param workbook
     * @return
     */
    public static CellStyle centerStyle(Workbook workbook,short fontSize) {
        CellStyle centerStyle = workbook.createCellStyle();
        // 居中
        centerStyle.setAlignment(HorizontalAlignment.CENTER);
        //垂直
        centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints(fontSize);
        //粗体显示
        centerStyle.setFont(font);
        return centerStyle;
    }

    /**
     * 居中字体粗样式
     *
     * @param workbook
     * @return
     */
    public static CellStyle centerAndFontBStyle(Workbook workbook,short fontSize) {
        CellStyle centerStyle = workbook.createCellStyle();
        // 居中
        centerStyle.setAlignment(HorizontalAlignment.CENTER);
        //垂直
        centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints(fontSize);
        //粗体显示
        font.setBold(true);
        centerStyle.setFont(font);
        return centerStyle;
    }


    /**
     * 设置单元格值且居中且字体加粗
     *
     * @return
     */
    public static Cell setCellValueFontB(Workbook workbook, Sheet sheet, int rowIdx, int cellIdx, Object value,short fontSize) {
        CellStyle centerStyle = centerAndFontBStyle(workbook,fontSize);
        Row sheetRow = getOrcreateRow(sheet,rowIdx);
        Cell cell = getOrCreateCell(sheetRow, cellIdx);
        cell.setCellStyle(centerStyle);
        setCellValue(cell,value);
        return cell;
    }

    /**
     * 创建背景填充色
     * @param workbook
     * @param green
     * @return
     */
    public static CellStyle createCellFillColor(Workbook workbook, IndexedColors green) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(green.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return cellStyle;
    }
    /**
     * 获得或创建sheet
     * @param workbook
     * @param sheetName sheet名称
     * @return
     */
    public static Sheet getOrCreateSheet(Workbook workbook,String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }
        return sheet;
    }

    /**
     * 设置边框
     * @param cellStyle
     * @return
     */
    public static CellStyle setBorder(CellStyle cellStyle){
        //下边框
        cellStyle.setBorderBottom(BorderStyle.THIN);
        //左边框
        cellStyle.setBorderLeft(BorderStyle.THIN);
        //上边框
        cellStyle.setBorderTop(BorderStyle.THIN);
        //右边框
        cellStyle.setBorderRight(BorderStyle.THIN);
        return cellStyle;
    }

    public static void saveExcel(Workbook wb,String filepath) {
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(filepath);
            wb.write(fileOut);
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
