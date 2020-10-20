package cn.lianrf.utils.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * excel下载导出工具包
 *
 * excel直接导出，使用：
 * {@link #exportAction(Workbook, HttpServletResponse, String)}
 *
 * 数据填充excel导出，使用：
 * {@link #export(List, String, HttpServletResponse)}
 *
 * 追加数据到单一sheet导出Excel文件
 * {@link #export(List, Workbook, String)}
 */
public abstract class ExcelUtil {

    /**
     * 响应导出Excel操作
     *
     * @param fileName 要保存的文件名称
     * @param response
     */
    public static void exportAction(Workbook workbook, HttpServletResponse response, String fileName) {

        try {
            setResponseHeader(response, fileName + ".xlsx");
            OutputStream os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            throw new RuntimeException("服务器异常");
        }
    }

    /**
     * 追加数据到单一sheet导出Excel文件
     *
     * @param data     数据列表
     * @param <C>      必须是泛型列表
     */
    public static <C> void export(List<C> data, Workbook workbook, String sheetName) {
        if(CollectionUtils.isEmpty(data)){
            return;
        }
        String[] titles = generateTitle(data.get(0).getClass());
        Object[][] values = generateValue(data);
        getHSSFWorkbook(sheetName, titles, values, workbook);
    }

    /**
     * 单一sheet导出Excel文件
     *
     * @param data     数据列表
     * @param fileName 文件名称
     * @param response 响应
     * @param <C>      必须是泛型列表
     */
    public static <C> void export(List<C> data, String fileName, HttpServletResponse response) {
        if (data == null || data.isEmpty()) {
            try {
                response.getWriter().println("data empty");
            } catch (IOException e) {

            }
            return;
        }
        String[] titles = generateTitle(data.get(0).getClass());
        Object[][] values = generateValue(data);

        if (fileName == null)
            fileName = "数据" + LocalDateTime.now().getSecond();
        Workbook workbook = getHSSFWorkbook(fileName, titles, values);
        exportAction(workbook, response, fileName);
    }

    public static <C> void export(List<C> data, String fileName, Workbook workbook, HttpServletResponse response) {
        if (data == null || data.isEmpty()) {
            try {
                response.getWriter().println("data empty");
            } catch (IOException e) {

            }
            return;
        }
        Object[][] values = generateValue(data);

        if (fileName == null)
            fileName = "数据" + LocalDateTime.now().getSecond();
        billSheet(values,workbook);
        exportAction(workbook, response, fileName);
    }


    //设置响应头
    private static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    

    /**
     * 获得Excel标题
     *
     * @param aClass
     * @return
     */
    private static String[] generateTitle(Class aClass) {
        List<String> columns = new ArrayList<>();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            ColumnName annotation = field.getAnnotation(ColumnName.class);
            if (annotation != null)
                columns.add(annotation.value());
        }
        columns.toArray(new String[0]);
        return columns.toArray(new String[0]);
    }

    /**
     * 计算内容
     *
     * @param data 查询条件参数
     */
    private static <C> Object[][] generateValue(List<C> data) {
        if (data == null || data.isEmpty())
            return new String[0][];
        Class<?> aClass = data.get(0).getClass();
        int size = data.size();
        Object[][] values = new Object[size][];

        for (int i = 0; i < size; i++) {
            Field[] fields = aClass.getDeclaredFields();
            List<Object> list = new ArrayList<>();
            for (Field field : fields) {
                if (field.getAnnotation(ColumnName.class) != null) {
                    field.setAccessible(true);
                    try {
                        list.add(Optional.ofNullable(field.get(data.get(i))).orElse(""));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            values[i] = list.toArray();
        }
        return values;
    }

    public static Workbook getHSSFWorkbook(String sheetName, String[] title, Object[][] values) {
        return getHSSFWorkbook(sheetName, title, values, null);
    }

    /**
     * 导出Excel
     *
     * @param sheetName sheet名称
     * @param title     标题
     * @param values    内容
     * @param wb        HSSFWorkbook对象
     * @return
     */
    public static Workbook getHSSFWorkbook(String sheetName, String[] title, Object[][] values, Workbook wb) {

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        Sheet sheet = wb.getSheet(sheetName);
        if(sheet==null){
            sheet = wb.createSheet(sheetName);
        }

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        Row row=null;
        int lastRowNum = sheet.getLastRowNum();
        if (lastRowNum == 0) {
            row = sheet.createRow(0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            CellStyle style = wb.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式
            //声明列对象
            Cell cell = null;
            for (int i = 0; i < title.length; i++) {
                cell = row.createCell(i);
                cell.setCellValue(title[i]);
                cell.setCellStyle(style);
            }
        }

        //创建内容
        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(++lastRowNum);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                setCellValue(row.createCell(j),values[i][j]);
            }
        }
        return wb;
    }

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

    public static void billSheet(Object[][] values, Workbook wb) {

        Sheet sheet = wb.getSheetAt(0);

        //创建单元格，并设置值表头 设置表头居中
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式


        //创建内容
        for (int i = 0; i < values.length; i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                setCellValue(row.createCell(j),values[i][j]);
            }
        }
    }


    public static String getStringCellValue(Cell cell) {
        if (cell == null
                || (cell.getCellTypeEnum() == CellType.STRING && StringUtils.isEmpty(cell.getStringCellValue()))) {
            return null;
        }
        CellType cellType = cell.getCellTypeEnum();
        switch (cellType) {
            case BLANK:
                return null;
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case NUMERIC:
                return NumberToTextConverter.toText(cell.getNumericCellValue());
            case STRING:
                return cell.getStringCellValue();
            default:
                return null;
        }
    }
}