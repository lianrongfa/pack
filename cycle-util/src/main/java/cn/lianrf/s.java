package cn.lianrf;

import cn.lianrf.utils.poi.PoiUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @version: v1.0
 * @date: 2019/4/12
 * @author: lianrf
 */
public class s {
    public static void main(String[] args) {
        //获取当前时间  含有毫秒值  17:18:41.571
        LocalTime now = LocalTime.now();
        System.out.println(now);

        //获取当前时间   去掉毫秒值   17:45:41
        LocalTime now1 = LocalTime.now().withNano(0);
        System.out.println(now1);
        //00:46:46.651  提供了把时分秒都设为0的方法
        LocalTime now2 = LocalTime.now().withHour(0);
        System.out.println(now2);

        //构造时间  00:20:55
        LocalTime time1 = LocalTime.of(0,20,55);
        System.out.println(time1);
        //构造时间  05:43:22
        LocalTime time2 = LocalTime.parse("05:43:22");
        System.out.println(time2);


        //标准时间 2017-11-06T17:53:15.930
        LocalDateTime lt = LocalDateTime.now();
        System.out.println(lt);
    }

    private static void test1() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        int i=20,j=50;
        for (int m = 0; m < i; m++) {
            for (int n = 0; n < j; n++) {
                Cell cell = PoiUtils.setCellValue(sheet, m, n, n);

                CellStyle cellFillColor = PoiUtils.createCellFillColor(workbook, IndexedColors.GREEN);

                if(m%2==0){
                    cell.setCellStyle(cellFillColor);
                }

            }
            for (int n = 0; n < j; n++) {
                Cell cell = PoiUtils.getOrCreateCell(PoiUtils.getOrcreateRow(sheet, m), n);
                CellStyle cellStyle = cell.getCellStyle();
                CellStyle style = PoiUtils.setBorder(cellStyle);
                cell.setCellStyle(style);
            }
        }


        PoiUtils.saveExcel(workbook,"C:\\Users\\86180\\Desktop\\test1.xlsx");
    }
}
