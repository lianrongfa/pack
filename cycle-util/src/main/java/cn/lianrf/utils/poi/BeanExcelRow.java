package cn.lianrf.utils.poi;


import com.sun.media.sound.InvalidFormatException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于Excel行与Bean之间的解析映射
 * @version: v1.0
 * @date: 2019/6/10
 * @author: lianrf
 */
public class BeanExcelRow<T> {

    private Map<Integer,String> fieldIdxs;

    public BeanExcelRow(Map fieldIdxs) {
        this.fieldIdxs = fieldIdxs;
    }

    public Map getFieldIdxs() {
        return fieldIdxs;
    }

    public void setFieldIdxs(Map fieldIdxs) {
        this.fieldIdxs = fieldIdxs;
    }

    public T rowMap(Row row, Class<T> clazz) throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        if(row==null||this.fieldIdxs ==null){
            return null;
        }

        T t = clazz.newInstance();

        for (Map.Entry<Integer, String> entry : fieldIdxs.entrySet()) {
            Integer idx = entry.getKey();
            Cell cell = row.getCell(idx);
            if(cell==null){
                //log.error("第{}行，第{}数据为空",row.getRowNum()+1,idx+1);
                continue;
            }
            String fieldName = entry.getValue();
            Field field = clazz.getDeclaredField(fieldName);
            //根据类型填充Bean值
            fillFieldValue(t, cell, field);


        }
        return t;
    }

    /**
     * 根据类型填充Bean值
     * @param t  bean
     * @param cell cell
     * @param field 目标字段
     */
    private void fillFieldValue(T t, Cell cell, Field field) {

        String value = cell.toString();

        if(StringUtils.isBlank(value))return;

        field.setAccessible(true);
        Class<?> type = field.getType();
        if (String.class.equals(type)) {
            try {
                field.set(t,value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (Integer.class.equals(type)) {
            try {
                field.set(t,(int)Double.parseDouble(value));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (Double.class.equals(type)) {
            try {
                field.set(t,Double.parseDouble(value));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (Date.class.equals(type)){
            Date date = cell.getDateCellValue();
            try {
                field.set(t,date);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

            Workbook workbook = null;//WorkbookFactory.create(new File("C:\\Users\\86180\\Desktop\\model\\code\\sibr\\java\\src\\main\\resources\\template\\人才招聘(模板).xlsx"));
            Sheet sheetAt = workbook.getSheetAt(0);

            Row row = sheetAt.getRow(1);

            HashMap<Integer,String> hashMap = new HashMap();
            hashMap.put(0,"position");
            hashMap.put(1,"num");
            hashMap.put(2,"education");
            hashMap.put(3,"workAddress");
            hashMap.put(4,"responsibility");
            hashMap.put(5,"qualification");
            BeanExcelRow<?> recruitBeanExcelRow = new BeanExcelRow(hashMap);

            //recruitBeanExcelRow.rowMap(row,Recruit.class);



    }
}
