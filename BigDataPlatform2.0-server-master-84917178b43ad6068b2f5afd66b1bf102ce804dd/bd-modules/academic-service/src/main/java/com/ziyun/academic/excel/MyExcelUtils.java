package com.ziyun.academic.excel;

import net.sf.excelutils.ExcelException;
import net.sf.excelutils.ExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletContext;
import java.io.OutputStream;

/**
 * 自定义 excel文件下载工具类
 */
public class MyExcelUtils extends ExcelUtils {

    public static void export2(ServletContext ctx, String config, OutputStream out) throws ExcelException {
        try {
            export2(ctx, config, getContext(), out);
        } catch (Exception var4) {
            throw new ExcelException(var4.getMessage());
        }
    }

    public static void export2(ServletContext ctx, String config, Object context, OutputStream out) throws ExcelException {
        try {
            HSSFWorkbook wb = new HSSFWorkbook(MyExcelUtils.class.getClassLoader().getResourceAsStream(config));
            // HSSFWorkbook wb = MyWorkbookUtils.openWorkbook2(ctx, config);
            parseWorkbook(context, wb);
            wb.write(out);
        } catch (Exception var5) {
            throw new ExcelException(var5.getMessage());
        }
    }
}
