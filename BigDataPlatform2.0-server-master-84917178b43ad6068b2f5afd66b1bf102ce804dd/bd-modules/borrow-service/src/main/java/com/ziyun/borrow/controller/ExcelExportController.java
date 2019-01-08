package com.ziyun.borrow.controller;

import com.ziyun.borrow.model.BorrowParamsVo;
import com.ziyun.borrow.service.IEduBorrowService;
import com.ziyun.common.model.Params;
import com.ziyun.common.model.ParamsStatus;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.excelutils.ExcelException;
import net.sf.excelutils.ExcelUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by linxiaojun on 11/3/17 6:33 PM.
 */
@Controller
@RequestMapping("/v2/excel/export")
public class ExcelExportController {

    private Logger logger = Logger.getLogger(ExcelExportController.class);

    /**
     * 限制excel下载记录数.
     */
    @Value("${excel.record.limit}")
    private int limit;
    //文件路径
    @Value("${excel.xlsLocation}")
    private String path;

    //借阅类型
    @Value("${excel.borrow.types.fileName}")
    private String borrowTypesFileName;
    @Value("${excel.borrow.types.name}")
    private String borrowTypesName;

    //借阅图书排名
    @Value("${excel.borrow.bookdetail.fileName}")
    private String borrowBookDetailFileName;
    @Value("${excel.borrow.bookdetail.name}")
    private String borrowBookDetailName;

    //借阅数量排名
    @Value("${excel.borrow.peopledetail.fileName}")
    private String borrowPeopleDetailFileName;
    @Value("${excel.borrow.peopledetail.name}")
    private String borrowPeopleDetailName;

    //借阅数量变化趋势
    @Value("${excel.borrow.variationtrend.fileName}")
    private String borrowVariationTrendFileName;
    @Value("${excel.borrow.variationtrend.name}")
    private String borrowVariationTrendName;

    //借阅频次
    @Value("${excel.borrow.frequency.name}")
    private String borrowFrequencyName;
    @Value("${excel.borrow.frequency.fileName}")
    private String borrowFrequencyFileName;

    //借阅人群
    @Value("${excel.borrow.people.name}")
    private String borrowPeopleName;
    @Value("${excel.borrow.people.fileName}")
    private String borrowPeopleFileName;

    @Autowired
    private IEduBorrowService eduBorrowService;
    
    /**
     * 借阅类型
     * @param para
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "借阅类型-导出", notes = "返回一级类目借阅数据的top7+其他", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code"),
            @ApiImplicitParam(paramType = "query", name = "facultyCode", dataType = "String", value = "院系code"),
            @ApiImplicitParam(paramType = "query", name = "majorCode", dataType = "String", value = "专业code"),
            @ApiImplicitParam(paramType = "query", name = "classSelect", dataType = "String", value = "班级code多选，用逗号隔开，班级只有code,没有名称"),
            @ApiImplicitParam(paramType = "query", name = "bdate", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "edate", dataType = "String", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "semester", dataType = "String", value = "学期字段"),
            @ApiImplicitParam(paramType = "query", name = "start", dataType = "Integer", value = "分页参数：从0开始"),
            @ApiImplicitParam(paramType = "query", name = "limit", dataType = "Integer", value = "分页参数：每页多少条数据"),
            @ApiImplicitParam(paramType = "query", name = "timeframe", dataType = "String", value = "查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年"),
            @ApiImplicitParam(paramType = "query", name = "politicalCode", dataType = "Integer", value = "政治面貌code:(1、团员，2、预备党员，3、党员)"),
            @ApiImplicitParam(paramType = "query", name = "scholarship", dataType = "Integer", value = "传1：优等生，传参过来，即可；或者不传"),
            @ApiImplicitParam(paramType = "query", name = "impoverish", dataType = "Integer", value = "传1：贫困生，传参过来，即可；或者不传")
    })
    @RequestMapping("/borrow/types")
    public void borrowTypesAOP(ParamsStatus para,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	List<Map<String, Object>> list = eduBorrowService.getLevelTwoType(para);
    	ExcelUtils.addValue("list", list);
    	exportExcelBySheets(request, response, path + borrowTypesFileName, borrowTypesName, null);
    }
    
    /**
     * 借阅图书排行
     * @param para
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/borrow/bookDetail")
    public void borrowBookDetailAOP(ParamsStatus para,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	List<Map<String, Object>> list = eduBorrowService.getBorrowBookDetail(para);
    	ExcelUtils.addValue("list", list);
    	exportExcelBySheets(request, response, path + borrowBookDetailFileName, borrowBookDetailName, null);
    }
    
    
    /**
     * 借阅数量排行
     * @param para
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/borrow/peopleDetail")
    public void borrowPeopleDetailAOP(BorrowParamsVo para,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer sortInt = para.getBorrowNumSort();
        String sortStr = "";
        sortInt = sortInt == null ? 1 : sortInt;
        if (1 == sortInt) {
            sortStr = "DESC";
        } else if (0 == sortInt) {
            sortStr = "ASC";
        }
        para.setBorrowNumSortStr(sortStr);
        List<Map<String, Object>> list = eduBorrowService.getBorrowPeopleDetail(para);
        ExcelUtils.addValue("list", list);
        exportExcelBySheets(request, response, path + borrowPeopleDetailFileName, borrowPeopleDetailName, null);
    }

    /**
     * 借阅数量趋势
     * @param params
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/borrow/borrowTrend")
    public void borrowVariationTrendAOP(Params params,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> list = eduBorrowService.getBorrowVariationTrend(params);
        ExcelUtils.addValue("list", list);
        exportExcelBySheets(request, response, path + borrowVariationTrendFileName, borrowVariationTrendName, null);
    }

    /**
     * 借阅频次
     * @param params
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/borrow/frequency")
    public void borrowFrequencyAOP(Params params,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> list = eduBorrowService.getWeekBorrowTotal(params);
        ExcelUtils.addValue("list", list);
        exportExcelBySheets(request, response, path + borrowFrequencyFileName, borrowFrequencyName, null);
    }

    /**
     * 借阅人群
     * @param params
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/borrow/detailSbySex")
    public void borrowPeopleAOP(Params params,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> list = eduBorrowService.getBorrowPeople(params);
        ExcelUtils.addValue("list", list);
        exportExcelBySheets(request, response, path + borrowPeopleFileName, borrowPeopleName, null);
    }

    /**
     * 导出excel：多sheet页
     * @param request   请求参数
     * @param response  响应参数
     * @param templatePath  文件模板
     * @param fileName  文件名
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
            HSSFWorkbook wb = new HSSFWorkbook(getClass().getClassLoader().getResourceAsStream(templatePath));
            if (sheetIndexes == null) {
                ExcelUtils.parseWorkbook(ExcelUtils.getContext(),wb);
            } else {
                ExcelUtils.parseWorkbook(sheetIndexes,wb);
            }
            wb.write(response.getOutputStream());
        } catch (ExcelException | IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Excel导出条数限制
     * @param list
     * @return
     */
    private List recordLimit(List<?> list) {
        if (list == null) return new ArrayList();
        if (list.size() > limit) {
            list = list.subList(0, limit);
        }
        return list;
    }
}
