package com.ziyun.dormitory.controller;

import com.ziyun.common.model.Params;
import com.ziyun.common.model.ParamsStatus;
import com.ziyun.dormitory.service.IEcardAccessInoutService;
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

    //限制excel下载记录数.
    @Value("${excel.record.limit}")
    private int limit;
    //文件路径
    @Value("${excel.xlsLocation}")
    private String path;

    //宿舍进出流量
    @Value("${excel.dorm.inOutFlow.name}")
    private String dormInOutFlowName;
    @Value("${excel.dorm.inOutFlow.fileName}")
    private String dormInOutFlowFileName;

    //晚归情况
    @Value("${excel.dorm.lateness.fileName}")
    private String dormLateNessFileName;
    @Value("${excel.dorm.lateness.name}")
    private String dormLateNessName;

    //晚归情况
    @Value("${excel.dorm.latenessdetail.fileName}")
    private String dormLateNessDetailFileName;
    @Value("${excel.dorm.latenessdetail.name}")
    private String dormLateNessDetailName;

    //可能晚归情况
    @Value("${excel.dorm.possible.fileName}")
    private String dormPossibleFileName;
    @Value("${excel.dorm.possible.name}")
    private String dormPossibleName;

    //晚归次数百分比
    @Value("${excel.dorm.ratio.fileName}")
    private String dormRatioFileName;
    @Value("${excel.dorm.ratio.name}")
    private String dormRatioName;

    //打卡记录
    @Value("${excel.dorm.record.fileName}")
    private String dormRecordFileName;
    @Value("${excel.dorm.record.name}")
    private String dormRecordName;
    //日可能未归学生记录
    @Value("${excel.dorm.noBack.fileName}")
    private String dormNoBackFileName;
    @Value("${excel.dorm.noBack.name}")
    private String dormNoBackName;

    @Autowired
    private IEcardAccessInoutService ecardAccessInoutService;

    @ApiOperation(value = "宿舍出入特征-宿舍进出流量图-导出", notes = "宿舍出入特征-宿舍进出流量图-导出", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code"),
            @ApiImplicitParam(paramType = "query", name = "facultyCode", dataType = "String", value = "院系code"),
            @ApiImplicitParam(paramType = "query", name = "majorCode", dataType = "String", value = "专业code"),
            @ApiImplicitParam(paramType = "query", name = "classSelect", dataType = "String", value = "班级code多选，用逗号隔开，班级只有code,没有名称"),
            @ApiImplicitParam(paramType = "query", name = "bdate", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "edate", dataType = "String", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "timeframe", dataType = "String", value = "查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年"),
            @ApiImplicitParam(paramType = "query", name = "politicalCode", dataType = "Integer", value = "政治面貌code:(1、团员，2、预备党员，3、党员)"),
            @ApiImplicitParam(paramType = "query", name = "scholarship", dataType = "Integer", value = "传1：优等生，传参过来，即可；或者不传"),
            @ApiImplicitParam(paramType = "query", name = "impoverish", dataType = "Integer", value = "传1：贫困生，传参过来，即可；或者不传"),
    })
    @RequestMapping("/dorm/inOutFlowsAll")
    public void exportDormInOutFlowAOP(Params params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> inOutMap = ecardAccessInoutService.listDormInOutFlowsAll(params);
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(inOutMap);
        ExcelUtils.addValue("list", list);
        exportExcelBySheets(request, response, path + dormInOutFlowFileName, dormInOutFlowName, null);
    }

    /**
     * 晚归情况
     *
     * @param params
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/dorm/lateness")
    public void getLateToBedroom(Params params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> lateMap = ecardAccessInoutService.getLateToBedroom(params);
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(lateMap);
        ExcelUtils.addValue("list", list);
        exportExcelBySheets(request, response, path + dormLateNessFileName, dormLateNessName, null);
    }

    /**
     * 晚归情况详情（序号，姓名，学号，打卡时间）
     *
     * @param params
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "晚归情况详情-导出", notes = "晚归情况详情-导出", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code"),
            @ApiImplicitParam(paramType = "query", name = "facultyCode", dataType = "String", value = "院系code"),
            @ApiImplicitParam(paramType = "query", name = "majorCode", dataType = "String", value = "专业code"),
            @ApiImplicitParam(paramType = "query", name = "classSelect", dataType = "String", value = "班级code多选，用逗号隔开，班级只有code,没有名称"),
            @ApiImplicitParam(paramType = "query", name = "bdate", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "edate", dataType = "String", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "semester", dataType = "String", value = "学期字段"),
            @ApiImplicitParam(paramType = "query", name = "timeframe", dataType = "String", value = "查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年"),
            @ApiImplicitParam(paramType = "query", name = "politicalCode", dataType = "Integer", value = "政治面貌code:(1、团员，2、预备党员，3、党员)"),
            @ApiImplicitParam(paramType = "query", name = "scholarship", dataType = "Integer", value = "传1：优等生，传参过来，即可；或者不传"),
            @ApiImplicitParam(paramType = "query", name = "impoverish", dataType = "Integer", value = "传1：贫困生，传参过来，即可；或者不传"),
    })
    @RequestMapping("/dorm/latenessDetail")
    public void getLateToBedroomDetail(Params params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map> lateDetailList = ecardAccessInoutService.getLateDetail(params);
        ExcelUtils.addValue("list", lateDetailList);
        exportExcelBySheets(request, response, path + dormLateNessDetailFileName, dormLateNessDetailName, null);
    }

    @ApiOperation(value = "可能未归情况-导出", notes = "可能未归情况-导出", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code"),
            @ApiImplicitParam(paramType = "query", name = "facultyCode", dataType = "String", value = "院系code"),
            @ApiImplicitParam(paramType = "query", name = "majorCode", dataType = "String", value = "专业code"),
            @ApiImplicitParam(paramType = "query", name = "classSelect", dataType = "String", value = "班级code多选，用逗号隔开，班级只有code,没有名称"),
            @ApiImplicitParam(paramType = "query", name = "bdate", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "edate", dataType = "String", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "timeframe", dataType = "String", value = "查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年"),
            @ApiImplicitParam(paramType = "query", name = "politicalCode", dataType = "Integer", value = "政治面貌code:(1、团员，2、预备党员，3、党员)"),
            @ApiImplicitParam(paramType = "query", name = "scholarship", dataType = "Integer", value = "传1：优等生，传参过来，即可；或者不传"),
            @ApiImplicitParam(paramType = "query", name = "impoverish", dataType = "Integer", value = "传1：贫困生，传参过来，即可；或者不传"),
    })
    @RequestMapping("/dorm/possible")
    public void getMybeLateAOP(Params params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> list = ecardAccessInoutService.getMybeLate(params);
        ExcelUtils.addValue("list", list);
        exportExcelBySheets(request, response, path + dormPossibleFileName, dormPossibleName, null);
    }

    @ApiOperation(value = "晚归次数百分比-导出", notes = "晚归次数百分比-导出", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code"),
            @ApiImplicitParam(paramType = "query", name = "facultyCode", dataType = "String", value = "院系code"),
            @ApiImplicitParam(paramType = "query", name = "majorCode", dataType = "String", value = "专业code"),
            @ApiImplicitParam(paramType = "query", name = "classSelect", dataType = "String", value = "班级code多选，用逗号隔开，班级只有code,没有名称"),
            @ApiImplicitParam(paramType = "query", name = "bdate", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "edate", dataType = "String", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "timeframe", dataType = "String", value = "查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年"),
            @ApiImplicitParam(paramType = "query", name = "politicalCode", dataType = "Integer", value = "政治面貌code:(1、团员，2、预备党员，3、党员)"),
            @ApiImplicitParam(paramType = "query", name = "scholarship", dataType = "Integer", value = "传1：优等生，传参过来，即可；或者不传"),
            @ApiImplicitParam(paramType = "query", name = "impoverish", dataType = "Integer", value = "传1：贫困生，传参过来，即可；或者不传"),
    })
    @RequestMapping("/dorm/ratio")
    public void getLateRatioAOP(Params params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map> list = ecardAccessInoutService.getLateRatio(params);
        ExcelUtils.addValue("list", list);
        exportExcelBySheets(request, response, path + dormRatioFileName, dormRatioName, null);
    }

    /**
     * 打卡记录
     *
     * @param params
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "宿舍出入打卡记录-导出", notes = "宿舍出入打卡记录-导出", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code"),
            @ApiImplicitParam(paramType = "query", name = "facultyCode", dataType = "String", value = "院系code"),
            @ApiImplicitParam(paramType = "query", name = "majorCode", dataType = "String", value = "专业code"),
            @ApiImplicitParam(paramType = "query", name = "classSelect", dataType = "String", value = "班级code多选，用逗号隔开，班级只有code,没有名称"),
            @ApiImplicitParam(paramType = "query", name = "bdate", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "edate", dataType = "String", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "timeframe", dataType = "String", value = "查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年"),
            @ApiImplicitParam(paramType = "query", name = "politicalCode", dataType = "Integer", value = "政治面貌code:(1、团员，2、预备党员，3、党员)"),
            @ApiImplicitParam(paramType = "query", name = "scholarship", dataType = "Integer", value = "传1：优等生，传参过来，即可；或者不传"),
            @ApiImplicitParam(paramType = "query", name = "impoverish", dataType = "Integer", value = "传1：贫困生，传参过来，即可；或者不传"),
            @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", value = "学号"),
            @ApiImplicitParam(paramType = "query", name = "someCode", dataType = "String", value = "进出类型：0-进  1-出  2-全部")
    })
    @RequestMapping("/dorm/record")
    public void getRecordAOP(ParamsStatus params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //打卡记录，默认最多导出一万条
        if (params != null) {
            params.setStart(0);
            params.setLimit(10000);
        }
        List<Map> list = ecardAccessInoutService.getClockRecord(params);
        ExcelUtils.addValue("list", list);
        exportExcelBySheets(request, response, path + dormRecordFileName, dormRecordName, null);
    }


    /**
     * 日可能未归学生记录
     *
     * @param params
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "宿舍出入特征-群体-某天可能未归学生-导出", notes = "宿舍出入特征-群体-某天可能未归学生-导出", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code"),
            @ApiImplicitParam(paramType = "query", name = "facultyCode", dataType = "String", value = "院系code"),
            @ApiImplicitParam(paramType = "query", name = "majorCode", dataType = "String", value = "专业code"),
            @ApiImplicitParam(paramType = "query", name = "classSelect", dataType = "String", value = "班级code多选，用逗号隔开，班级只有code,没有名称"),
            @ApiImplicitParam(paramType = "query", name = "bdate", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "edate", dataType = "String", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "timeframe", dataType = "String", value = "查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年"),
            @ApiImplicitParam(paramType = "query", name = "politicalCode", dataType = "Integer", value = "政治面貌code:(1、团员，2、预备党员，3、党员)"),
            @ApiImplicitParam(paramType = "query", name = "scholarship", dataType = "Integer", value = "传1：优等生，传参过来，即可；或者不传"),
            @ApiImplicitParam(paramType = "query", name = "impoverish", dataType = "Integer", value = "传1：贫困生，传参过来，即可；或者不传"),
            @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", value = "学号"),
            @ApiImplicitParam(paramType = "query", name = "someCode", dataType = "String", value = "进出类型：0-进  1-出  2-全部"),
            @ApiImplicitParam(paramType = "query", name = "day", dataType = "String", value = "查询的日期，例：2018-06-19"),
            @ApiImplicitParam(paramType = "query", name = "sort", dataType = "String", value = "排序字段名：'OUTID','OPDT'"),
            @ApiImplicitParam(paramType = "query", name = "order", dataType = "String", value = "排序类型：0:升序;1:降序")
    })
    @RequestMapping("/dorm/noComeBackByDay")
    public void getNoComeBackByDayAOP(ParamsStatus params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> list = ecardAccessInoutService.excelNoComeBackByDay(params);
        ExcelUtils.addValue("list", list);
        exportExcelBySheets(request, response, path + dormNoBackFileName, dormNoBackName, null);
    }

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
            HSSFWorkbook wb = new HSSFWorkbook(getClass().getClassLoader().getResourceAsStream(templatePath));
            if (sheetIndexes == null) {
                ExcelUtils.parseWorkbook(ExcelUtils.getContext(), wb);
                //ExcelUtils.export(request.getSession().getServletContext(), templatePath, response.getOutputStream());
            } else {
                ExcelUtils.parseWorkbook(sheetIndexes, wb);
                //ExcelUtils.export(request.getSession().getServletContext(), templatePath, sheetIndexes, response.getOutputStream());
            }
            wb.write(response.getOutputStream());
        } catch (ExcelException | IOException e) {
            e.printStackTrace();
        }
    }

    private List recordLimit(List<?> list) {
        if (list == null) return new ArrayList();
        if (list.size() > limit) {
            list = list.subList(0, limit);
        }
        return list;
    }
}
