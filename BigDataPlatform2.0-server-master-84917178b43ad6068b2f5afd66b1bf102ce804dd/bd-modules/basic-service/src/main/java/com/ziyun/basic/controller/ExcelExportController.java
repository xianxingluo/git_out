package com.ziyun.basic.controller;

import com.ziyun.basic.server.StudentServer;
import com.ziyun.basic.vo.Params;
import com.ziyun.basic.vo.ParamsStatus;
import com.ziyun.utils.common.StringUtils;
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
import java.util.LinkedHashMap;
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

    //--------基础信息-------
    @Value("${excel.basics.source.name}")
    private String basicsSourceName;
    @Value("${excel.basics.source.fileName}")
    private String basicsSourceFileName;

    //年龄分布
    @Value("${excel.basics.countbybrithdry.name}")
    private String countByBrithdryName;
    @Value("${excel.basics.countbybrithdry.fileName}")
    private String countByBrithdryFileName;

    //男女比例
    @Value("${excel.basics.basicsexratio.name}")
    private String basicSexratioName;
    @Value("${excel.basics.basicsexratio.fileName}")
    private String basicSexratioFileName;

    //---------奖惩特征--------
    //获奖类型
    @Value("${excel.reward.scholarshiplist.name}")
    private String scholarshipListName;
    @Value("${excel.reward.scholarshiplist.fileName}")
    private String scholarshipListFileName;

    //获奖次数
    @Value("${excel.reward.scholarshipsex.name}")
    private String scholarshipSexName;
    @Value("${excel.reward.scholarshipsex.fileName}")
    private String scholarshipSexFileName;

    //处罚人员
    @Value("${excel.punish.punishlist.fileName}")
    private String punishpunishListFileName;
    @Value("${excel.punish.punishlist.name}")
    private String punishpunishListName;

    //活动积分
    @Value("${excel.punish.activeRanking.path}")
    private String activeRankingPath;
    @Value("${excel.punish.activeRanking.name}")
    private String activeRankingName;

    @Autowired
	private StudentServer studentServer;

    //基础信息的生源地
    @ApiOperation(value = "基本信息-生源地分布-导出", notes = "基本信息-生源地分布-导出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", value = " 学业特征时间"),
    })
    @RequestMapping("/basics/source")
    public void getBasicsSourceExclAOP(Params para,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	List<LinkedHashMap<String, Object>> data = studentServer.getSource(para);
    	List<Map<String, Object>> rlt = new ArrayList<>();
    	rlt.addAll(data);
    	ExcelUtils.addValue("list", rlt);
    	exportExcelBySheets(request, response, path + basicsSourceFileName, basicsSourceName, null);
    }
    //基础信息的年龄分布
    @ApiOperation(value = "基本信息-年龄分布-导出", notes = "年龄分布")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", value = " 学业特征时间"),
    })
    @RequestMapping("/basics/countByBrithdry")
    public void getBasicsCountBySexExclAOP(ParamsStatus para,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	List<LinkedHashMap<String, Object>> data = studentServer.countByBirthday(para);
    	List<Map<String, Object>> rlt = new ArrayList<>();
    	rlt.addAll(data);
    	ExcelUtils.addValue("list", rlt);
    	exportExcelBySheets(request, response, path + countByBrithdryFileName, countByBrithdryName, null);
    }
    //基础信息的男女比例
    @ApiOperation(value = "基本信息-男女比例-导出", notes = "男女比例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", value = " 学业特征时间"),
    })
    @RequestMapping("/basics/sexRatio")
    public void getBasicsSexRatioExclAOP(Params para,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	List<Map<String, Object>> data = studentServer.getCountBySex(para);
    	ExcelUtils.addValue("list", data);
    	exportExcelBySheets(request, response, path + basicSexratioFileName, basicSexratioName, null);
    }

    //奖惩特征 获奖类型
    @RequestMapping("/reward/scholarshipList")
    public void getRewardScholarshipListAOP(ParamsStatus para,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if(StringUtils.isNotBlank(para.getEdate()) && StringUtils.isNotBlank(para.getBdate())){
			para.setEdate(para.getEdate().substring(0, 4));
			para.setBdate(para.getBdate().substring(0, 4));
		}
		List<LinkedHashMap<String, Object>> data = studentServer.scholarshipPageList(para);
		List<Map<String, Object>> rlt = new ArrayList<>();
    	rlt.addAll(data);
    	ExcelUtils.addValue("list", rlt);
    	exportExcelBySheets(request, response, path + scholarshipListFileName, scholarshipListName, null);
    }

    //奖惩特征 奖励次数
    @RequestMapping("/reward/scholarshipSex")
    public void getRewardScholarshipSexAOP(ParamsStatus para,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	List<Map<String, Object>> data = studentServer.getScholarshipCountBysex(para);
    	ExcelUtils.addValue("list", data);
    	exportExcelBySheets(request, response, path + scholarshipSexFileName, scholarshipSexName, null);
    }

    //奖励特征 处罚人员
    @RequestMapping("/punish/punishList")
    public void getPunishPunishListAOP(ParamsStatus para,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	List<LinkedHashMap<String, Object>> data = studentServer.punishPageList(para);
    	List<Map<String, Object>> rlt = new ArrayList<>();
    	rlt.addAll(data);
    	ExcelUtils.addValue("list", rlt);
    	exportExcelBySheets(request, response, path + punishpunishListFileName, punishpunishListName, null);
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


    @RequestMapping("/student/activeRanking")
    @ApiOperation(value = "奖惩特征--活动积分排名", notes = "活动积分排名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", value = " 学业特征时间"),
    })
    public void getActiveRankingAOP(ParamsStatus params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (params != null && (StringUtils.isBlank(params.getSort()) || StringUtils.isBlank(params.getOrder()))) {
            params.setSort("integration");
            params.setOrder("descending");
        }
        List<Map<String, Object>> list = studentServer.getActiveRanking(params);
        ExcelUtils.addValue("list", list);
        exportExcelBySheets(request, response, activeRankingPath, activeRankingName, null);
    }
}
