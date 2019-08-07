package cn.lianrf.utils.poi;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * excel下载导出工具包
 *
 * excel直接导出，使用：
 * {@link #exportAction(HSSFWorkbook, HttpServletResponse, String)}
 *
 * 数据填充excel导出，使用：
 * {@link #export(List, String, HttpServletResponse)}
 *
 */
public abstract class ExcelUtil {

    /**
     * 响应导出Excel操作
     *
     * @param fileName 要保存的文件名称
     * @param response
     */
    public static void exportAction(HSSFWorkbook workbook, HttpServletResponse response, String fileName) {

        try {
            setResponseHeader(response, fileName + ".xls");
            OutputStream os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            throw new RuntimeException("服务器异常");
        }
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
                response.getWriter().println("数据为空");
            } catch (IOException e) {

            }
            return;
        }
        String[] titles = generateTitle(data.get(0).getClass());
        String[][] values = generateValue(data);

        if (fileName == null)
            fileName = "数据" + LocalDateTime.now().getSecond();
        HSSFWorkbook workbook = getHSSFWorkbook(fileName, titles, values);
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
    private static <C> String[][] generateValue(List<C> data) {
        if (data == null || data.isEmpty())
            return new String[0][];
        Class<?> aClass = data.get(0).getClass();
        int size = data.size();
        String[][] values = new String[size][];

        for (int i = 0; i < size; i++) {
            Field[] fields = aClass.getDeclaredFields();
            List<String> list = new ArrayList<>();
            for (Field field : fields) {
                if (field.getAnnotation(ColumnName.class) != null) {
                    field.setAccessible(true);
                    try {
                        list.add(String.valueOf(Optional.ofNullable(field.get(data.get(i))).orElse("")));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            values[i] = list.toArray(new String[0]);
        }
        return values;
    }

    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values) {
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
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values, HSSFWorkbook wb) {

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式

        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }
        //创建内容
        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return wb;
    }
}