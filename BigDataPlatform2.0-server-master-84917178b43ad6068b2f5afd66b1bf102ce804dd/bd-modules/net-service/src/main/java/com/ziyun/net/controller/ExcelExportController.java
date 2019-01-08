package com.ziyun.net.controller;

import com.ziyun.net.enums.SexEnum;
import com.ziyun.net.server.IA3RadacctTimeServer;
import com.ziyun.net.vo.NetParams;
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
import java.math.BigDecimal;
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

    /**
     * 上网总时长分布： 模板路径 & 名称
     */
    @Value("${excel.net.durationDist.fileName}")
    private String netDurationDistFileName;

    @Value("${excel.net.durationDist.name}")
    private String netDurationDistName;

    /**
     * 上网访问内容排名： 模板路径 & 名称
     */
    @Value("${excel.net.visitContext.fileName}")
    private String netVisitContextFileName;

    @Value("${excel.net.visitContext.name}")
    private String netVisitContextName;

    /**
     * 上网时长排名： 模板路径 & 名称
     */
    @Value("${excel.net.durationTop.fileName}")
    private String netDurationTopFileName;

    @Value("${excel.net.durationTop.name}")
    private String netDurationTopName;

    /**
     * 上网终端类型： 模板路径 & 名称
     */
    @Value("${excel.net.terminalType.fileName}")
    private String netTerminalTypeFileName;

    @Value("${excel.net.terminalType.name}")
    private String netTerminalTypeName;

    /**
     * 上网人群分析： 模板路径 & 名称
     */
    @Value("${excel.net.crowdAnalysis.fileName}")
    private String netCrowdAnalysisFileName;

    @Value("${excel.net.crowdAnalysis.name}")
    private String netCrowdAnalysisName;

    /**
     * 上网时段： 模板路径 & 名称
     */
    @Value("${excel.net.period.fileName}")
    private String netPeriodFileName;

    @Value("${excel.net.period.name}")
    private String getNetPeriodName;

    /**
     * 上网内容热度： 模板路径 & 名称
     */
    @Value("${excel.net.contentHeat.fileName}")
    private String netContentHeatFileName;

    @Value("${excel.net.contentHeat.name}")
    private String netContentHeatName;

    /**
     * 上网流量： 模板路径 & 名称
     */
    @Value("${excel.net.netFlux.fileName}")
    private String netFluxFileName;

    @Value("${excel.net.netFlux.name}")
    private String netFluxName;

    @Autowired
    private IA3RadacctTimeServer radacctTimeServer;

    /**
     * 上网总时长分布
     * @param params
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "上网总时长分布-导出", notes = "上网总时长分布-导出", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code: slg(苏理工)|jkd(江科大)"),
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
    @RequestMapping("/net/durationDist")
    public void exportNetDurationDistAOP(NetParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> result = radacctTimeServer.getDurationDistTable(params);
        int count = radacctTimeServer.getTotalPeople(params);
        result.stream().forEach(b -> {
            BigDecimal totalDuration = (BigDecimal) b.get("totalDuration");
            BigDecimal allAvg = totalDuration.divide(new BigDecimal(count), 2, BigDecimal.ROUND_HALF_UP);
            b.put("allAvg", allAvg);
        });
        ExcelUtils.addValue("list", result);
        exportExcelBySheets(request, response, path + netDurationDistFileName, netDurationDistName, null);
    }

    /**
     * 上网人群分析
     * @param params
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/net/crowdAnalysis")
    public void exportNetCrowdAnalysisAOP(NetParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> result = radacctTimeServer.getCrowdAnalysisTable(params);
        int totalPeopleMale = radacctTimeServer.getTotalPeopleMale(params);
        int totalPeopleFemale = radacctTimeServer.getTotalPeopleFemale(params);
        result.stream().forEach(b -> {
            BigDecimal totalDuration = (BigDecimal) b.get("totalDuration");
            int count = b.get("sex").equals(SexEnum.MALE.getValue()) ? totalPeopleMale : totalPeopleFemale;
            BigDecimal allAvg = totalDuration.divide(new BigDecimal(count), 2, BigDecimal.ROUND_HALF_UP);
            b.put("allAvg", allAvg);
        });
        ExcelUtils.addValue("list", result);
        exportExcelBySheets(request, response, path + netCrowdAnalysisFileName, netCrowdAnalysisName, null);
    }

    /**
     * 上网时长TOP10
     * @param params
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "上网时长排名-导出", notes = "上网时长排名-导出", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code: slg(苏理工)|jkd(江科大)"),
            @ApiImplicitParam(paramType = "query", name = "facultyCode", dataType = "String", value = "院系code"),
            @ApiImplicitParam(paramType = "query", name = "majorCode", dataType = "String", value = "专业code"),
            @ApiImplicitParam(paramType = "query", name = "classSelect", dataType = "String", value = "班级code多选，用逗号隔开，班级只有code,没有名称"),
            @ApiImplicitParam(paramType = "query", name = "bdate", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "edate", dataType = "String", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "timeframe", dataType = "String", value = "查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年"),
            @ApiImplicitParam(paramType = "query", name = "politicalCode", dataType = "Integer", value = "政治面貌code:(1、团员，2、预备党员，3、党员)"),
            @ApiImplicitParam(paramType = "query", name = "scholarship", dataType = "Integer", value = "传1：优等生，传参过来，即可；或者不传"),
            @ApiImplicitParam(paramType = "query", name = "impoverish", dataType = "Integer", value = "传1：贫困生，传参过来，即可；或者不传"),
            @ApiImplicitParam(paramType = "query", name = "start", dataType = "Integer", value = "分页下标（0, 10, 20...）"),
            @ApiImplicitParam(paramType = "query", name = "sort", dataType = "String", value = "排序字段名：'sum','avg','avg1'"),
            @ApiImplicitParam(paramType = "query", name = "order", dataType = "String", value = "排序类型：0:升序;1:降序")
    })
    @RequestMapping("/net/durationTop")
    public void exportNetDurationTopAOP(NetParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> result = radacctTimeServer.getDurationTopTable(params);
        ExcelUtils.addValue("list", result);
        exportExcelBySheets(request, response, path + netDurationTopFileName, netDurationTopName, null);
    }

    /**
     * 上网终端
     * @param params
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/net/terminalType")
    public void exportNetTerminalTypeAOP(NetParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> result = radacctTimeServer.getTerminalTypeTable(params);
        ExcelUtils.addValue("list", result);
        exportExcelBySheets(request, response, path + netTerminalTypeFileName, netTerminalTypeName, null);
    }

    /**
     * 访问内容
     * @param params
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "上网访问内容-导出", notes = "上网访问内容-导出", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code: slg(苏理工)|jkd(江科大)"),
            @ApiImplicitParam(paramType = "query", name = "facultyCode", dataType = "String", value = "院系code"),
            @ApiImplicitParam(paramType = "query", name = "majorCode", dataType = "String", value = "专业code"),
            @ApiImplicitParam(paramType = "query", name = "classSelect", dataType = "String", value = "班级code多选，用逗号隔开，班级只有code,没有名称"),
            @ApiImplicitParam(paramType = "query", name = "bdate", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "edate", dataType = "String", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "timeframe", dataType = "String", value = "查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年"),
            @ApiImplicitParam(paramType = "query", name = "politicalCode", dataType = "Integer", value = "政治面貌code:(1、团员，2、预备党员，3、党员)"),
            @ApiImplicitParam(paramType = "query", name = "scholarship", dataType = "Integer", value = "传1：优等生，传参过来，即可；或者不传"),
            @ApiImplicitParam(paramType = "query", name = "impoverish", dataType = "Integer", value = "传1：贫困生，传参过来，即可；或者不传"),
            @ApiImplicitParam(paramType = "query", name = "start", dataType = "Integer", value = "分页下标（0, 10, 20...）")
    })
    @RequestMapping("/net/visitContext")
    public void exportNetVisitContextAOP(NetParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> result = radacctTimeServer.getVisitContextTopTable(params);
        ExcelUtils.addValue("list", result);
        exportExcelBySheets(request, response, path + netVisitContextFileName, netVisitContextName, null);
    }

    /**
     * 上网时段
     * @param params
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "上网时段-导出", notes = "上网时段-导出", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code: slg(苏理工)|jkd(江科大)"),
            @ApiImplicitParam(paramType = "query", name = "facultyCode", dataType = "String", value = "院系code"),
            @ApiImplicitParam(paramType = "query", name = "majorCode", dataType = "String", value = "专业code"),
            @ApiImplicitParam(paramType = "query", name = "classSelect", dataType = "String", value = "班级code多选，用逗号隔开，班级只有code,没有名称"),
            @ApiImplicitParam(paramType = "query", name = "bdate", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "edate", dataType = "String", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "timeframe", dataType = "String", value = "查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年"),
            @ApiImplicitParam(paramType = "query", name = "politicalCode", dataType = "Integer", value = "政治面貌code:(1、团员，2、预备党员，3、党员)"),
            @ApiImplicitParam(paramType = "query", name = "scholarship", dataType = "Integer", value = "传1：优等生，传参过来，即可；或者不传"),
            @ApiImplicitParam(paramType = "query", name = "impoverish", dataType = "Integer", value = "传1：贫困生，传参过来，即可；或者不传"),
            @ApiImplicitParam(paramType = "query", name = "weekday", dataType = "Integer", value = "8:周一~周五|9:周六~周日"),
    })
    @RequestMapping("/net/period")
    public void exportNetPeriodAOP(NetParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> result = radacctTimeServer.getPeriodTable(params);
        ExcelUtils.addValue("list", result);
        exportExcelBySheets(request, response, path + netPeriodFileName, getNetPeriodName, null);
    }

    /**
     * 上网内容热度: 表格下载
     * @param params
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "上网内容热度-导出", notes = "上网内容热度-导出", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", value = "学号id，查个人时必须"),
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code: slg(苏理工)|jkd(江科大)"),
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
    @RequestMapping("/net/contentHeat")
    public void exportNetContentHeatAOP(NetParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> result = radacctTimeServer.getContentHeatTable(params);
        ExcelUtils.addValue("list", result);
        exportExcelBySheets(request, response, path + netContentHeatFileName, netContentHeatName, null);
    }

    /**
     * 上网流量: 表格下载
     * @param params
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "上网流量-导出", notes = "上网流量-导出", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", value = "学号id，查个人时必须"),
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code: slg(苏理工)|jkd(江科大)"),
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
    @RequestMapping("/net/flux")
    public void exportNetFluxAOP(NetParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> result = radacctTimeServer.getFluxTable(params);
        ExcelUtils.addValue("list", result);
        exportExcelBySheets(request, response, path + netFluxFileName, netFluxName, null);
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
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
        } else {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
        try {
//            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(ResourceUtils.getFile(templatePath)));
//            InputStream stream = getClass().getClassLoader().getResourceAsStream(templatePath);
            HSSFWorkbook wb = new HSSFWorkbook(getClass().getClassLoader().getResourceAsStream(templatePath));
//            File targetFile = new File(fileName);
//            FileUtils.copyInputStreamToFile(stream,targetFile);
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
}
