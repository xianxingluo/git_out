package com.ziyun.consume.excel;

import net.sf.excelutils.ExcelException;
import net.sf.excelutils.WorkbookUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.util.ResourceUtils;

import javax.servlet.ServletContext;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 自定义 工作簿工具类
 */
public class MyWorkbookUtils extends WorkbookUtils {
    public static HSSFWorkbook openWorkbook2(ServletContext ctx, String config) throws ExcelException {
        InputStream in = null;
        HSSFWorkbook wb = null;

        try {

//  基类WorkbookUtils 源代码如下，发现获取不到 自己xls/course_pointTop.xls 文件，所以重写此处代码
//         in = ctx.getResourceAsStream(config);
            in = new FileInputStream(ResourceUtils.getFile(config));
            wb = new HSSFWorkbook(in);
        } catch (Exception var12) {
            throw new ExcelException("File" + config + "not found," + var12.getMessage());
        } finally {
            try {
                in.close();
            } catch (Exception var11) {
                ;
            }

        }

        return wb;
    }
}
