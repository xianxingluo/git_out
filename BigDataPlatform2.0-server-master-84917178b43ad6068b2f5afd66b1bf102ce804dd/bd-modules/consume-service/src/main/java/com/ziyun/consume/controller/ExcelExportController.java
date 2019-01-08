package com.ziyun.consume.controller;

import com.ziyun.consume.excel.MyExcelUtils;
import com.ziyun.consume.server.IEcardRecConsumeServer;
import com.ziyun.consume.vo.ConsumeParams;
import com.ziyun.consume.vo.Params;
import com.ziyun.consume.vo.ResultData;
import net.sf.excelutils.ExcelException;
import net.sf.excelutils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 消费微服务，上传下载controller
 */
@Controller
@RequestMapping("/v2/excel/export")
public class ExcelExportController {
    //消费排名模块下载
    @Value("${excel.consumesumTopList.path}")
    private String consumesumTopListPath;
    @Value("${excel.consumesumTopList.name}")
    private String consumesumTopListFileName;


    //-------------------------消费类目明细

    //下载消费
    @Value("${excel.consumeCategoryDetail.path}")
    private String consumeCategoryDetailPath;
    @Value("${excel.consumeCategoryDetail.name}")
    private String consumeCategoryDetailFileName;

    //餐厅消费详情
    @Value("${excel.canteenConsumeDetail1.path}")
    private String canteenConsumeDetailPath;
    @Value("${excel.canteenConsumeDetail1.name}")
    private String canteenConsumeDetail1FileName;

    //餐饮消费详情
    @Value("${excel.canteenConsumeDetail2.path}")
    private String canteenConsumeDetai2Path;
    @Value("${excel.canteenConsumeDetail2.name}")
    private String canteenConsumeDetail2FileName;

    //窗口消费详情
    @Value("${excel.canteenConsumeDetail3.path}")
    private String canteenConsumeDetai3Path;
    @Value("${excel.canteenConsumeDetail3.name}")
    private String canteenConsumeDetail3FileName;

    //购物消费详情
    @Value("${excel.shopConsumeDetail1.path}")
    private String shopConsumeDetail1Path;
    @Value("${excel.shopConsumeDetail1.name}")
    private String shopConsumeDetail1FileName;

    //用水消费详情
    @Value("${excel.shopConsumeDetail2.path}")
    private String shopConsumeDetail2Path;
    @Value("${excel.shopConsumeDetail2.name}")
    private String shopConsumeDetail2FileName;

    //用电消费详情
    @Value("${excel.shopConsumeDetail3.path}")
    private String shopConsumeDetail3Path;
    @Value("${excel.shopConsumeDetail3.name}")
    private String shopConsumeDetail3FileName;

    //其它消费详情
    @Value("${excel.shopConsumeDetail4.path}")
    private String shopConsumeDetail4Path;
    @Value("${excel.shopConsumeDetail4.name}")
    private String shopConsumeDetail4FileName;

    //等级考试 ：英语，计算机，普通话等
    @Value("${excel.examConsumeDetail1.path}")
    private String examConsumeDetail1Path;
    @Value("${excel.examConsumeDetail1.name}")
    private String examConsumeDetail1FileName;

    //等级考试详情 ：英语四级，英语六级等
    @Value("${excel.examConsumeDetail2.path}")
    private String examConsumeDetail2Path;
    @Value("${excel.examConsumeDetail2.name}")
    private String examConsumeDetail2FileName;


    //-------------------------消费类目明细

    @Autowired
    public IEcardRecConsumeServer consumeServe;

    /**
     * 导出excel：多sheet页
     *
     * @param request       请求参数
     * @param response      响应参数
     * @param templatePath  文件模板
     * @param fileName      文件名
     * @param sheetIndexes: delete these sheets
     * @throws UnsupportedEncodingException
     */
    private void exportExcelBySheets(HttpServletRequest request, HttpServletResponse response,
                                     String templatePath, String fileName, int[] sheetIndexes) throws UnsupportedEncodingException {

        String agent = request.getHeader("USER-AGENT").toLowerCase();
        response.reset();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        if (agent.contains("firefox")) {
            fileName = new String(fileName.getBytes(), "ISO8859-1");
        } else {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
        try {
            if (sheetIndexes == null) {
                MyExcelUtils.export2(request.getSession().getServletContext(), templatePath, response.getOutputStream());
            } else {
                ExcelUtils.export(request.getSession().getServletContext(), templatePath, sheetIndexes, response.getOutputStream());
            }
        } catch (ExcelException | IOException e) {
            e.printStackTrace();
        }
    }

    //消费：消费排名
    @RequestMapping("/consume/sumTopListDetail")
    public void exportSumTopListAOP(Params params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<ResultData> list = consumeServe.sumTopList(params);
        ExcelUtils.addValue("list", list);
        exportExcelBySheets(request, response, consumesumTopListPath, consumesumTopListFileName, null);
    }

    /**
     * 消费类目下载
     *
     * @param params
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/consume/consumeCategoryDetail")
    public void getConsumeCategoryDetailAOP(ConsumeParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer queryType = params.getQueryType();
        //下载的path
        String downPath = null;
        //下载的文件名
        String downFileName = null;
        switch (queryType) {
            case 1:
                //展示消费全部：餐费，考试费，购物，用水，用电，其他

                downPath = consumeCategoryDetailPath;
                downFileName = consumeCategoryDetailFileName;
                break;
            //餐厅详情
            case 2:
                downPath = canteenConsumeDetailPath;
                downFileName = canteenConsumeDetail1FileName;
                break;
            //商户详情
            case 3:
                downPath = canteenConsumeDetai2Path;
                downFileName = canteenConsumeDetail2FileName;
                break;
            //窗口详情
            case 4:
                downPath = canteenConsumeDetai3Path;
                downFileName = canteenConsumeDetail3FileName;
                break;
            //购物
            case 5:
                downPath = shopConsumeDetail1Path;
                downFileName = shopConsumeDetail1FileName;
                break;
            //用水
            case 6:
                downPath = shopConsumeDetail2Path;
                downFileName = shopConsumeDetail2FileName;
                break;
            //用电
            case 7:
                downPath = shopConsumeDetail3Path;
                downFileName = shopConsumeDetail3FileName;
                break;
            //其他
            case 8:
                downPath = shopConsumeDetail4Path;
                downFileName = shopConsumeDetail4FileName;
                break;
            //考试费 ：英语，计算机，普通话等
            case 9:
                downPath = examConsumeDetail1Path;
                downFileName = examConsumeDetail1FileName;
                break;
            //考试费 ：英语四级，英语六级等
            case 10:
                downPath = examConsumeDetail2Path;
                downFileName = examConsumeDetail2FileName;
                break;
        }
        List<Map<String, Object>> list = null;
        switch (queryType) {
            case 1:
                //展示消费全部：餐费，考试费，购物，用水，用电，其他
                list = consumeServe.getConsumeCategoryDetail(params);
                break;
            //餐厅详情
            case 2:
                //商户详情
            case 3:
                //窗口详情
            case 4:
                list = consumeServe.getCanteenConsumeDetail(params);
                break;
            //购物
            case 5:
            case 6:
                //用水
            case 7:
                //用电
            case 8:
                //其他
                list = consumeServe.getShopConsumeDetail(params);
                break;
            //考试费 ：英语，计算机，普通话等
            case 9:
                //考试费 ：英语四级，英语六级等
            case 10:
                list = consumeServe.getExamConsumeDetail(params);
                break;


        }
        ExcelUtils.addValue("list", list);
        exportExcelBySheets(request, response, downPath, downFileName, null);
    }


}
