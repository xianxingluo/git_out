package com.ziyun.academic.controller;

import com.ziyun.academic.excel.MyExcelUtils;
import com.ziyun.academic.server.IAcademicServer;
import com.ziyun.academic.server.IEarlyWarningServer;
import com.ziyun.academic.vo.AcademicParams;
import com.ziyun.utils.common.StringUtils;
import net.sf.excelutils.ExcelException;
import net.sf.excelutils.ExcelUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
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
 * 学业微服务，下载，上传Controller
 */
@Controller
@RequestMapping("/v2/excel/export")
public class ExcelExportController {
    //绩点排名
    @Value("${excel.pointTop.path}")
    private String pointTopPath;

    @Value("${excel.pointTop.name}")
    private String pointTopFileName;

    //上课考勤打卡 --群体
    @Value("${excel.attendanceCard.path}")
    private String attendanceCardPath;

    @Value("${excel.attendanceCard.name}")
    private String attendanceCardFileName;

    //课程分类--弹窗列表
    @Value("${excel.course.category.studentList.path}")
    private String courseCatagoryStudentListPath;
    @Value("${excel.course.category.studentList.name}")
    private String courseCatagoryStudentListFileName;

    //课程性质--弹窗列表
    @Value("${excel.course.natures.studentList.path}")
    private String courseNaturesStudentListPath;
    @Value("${excel.course.natures.studentList.name}")
    private String courseNaturesStudentListFileName;
    //课程属性--弹窗列表
    @Value("${excel.course.properties.studentList.path}")
    private String coursePropertiesStudentListPath;
    @Value("${excel.course.properties.studentList.name}")
    private String coursePropertiesStudentListFileName;


    //选修课排名--弹窗一 下载
    @Value("${excel.electiveTopCoursenoList.path}")
    private String electiveTopCoursenoListPath;

    @Value("${excel.electiveTopCoursenoList.name}")
    private String electiveTopCoursenoListFileName;


    //学业预警
    //必修课挂科门数在2--4门之间
    @Value("${excel.early.warning.compulsoryCoursesCount2.name}")
    private String compulsoryCoursesCount2Name;
    @Value("${excel.early.warning.compulsoryCoursesCount2.path}")
    private String compulsoryCoursesCount2Path;
    //必修课挂科门数大于4门
    @Value("${excel.early.warning.compulsoryCoursesCount4.name}")
    private String compulsoryCoursesCount4Name;
    @Value("${excel.early.warning.compulsoryCoursesCount4.path}")
    private String compulsoryCoursesCount4Path;

    //学年总学分小于18学分
    @Value("${excel.early.warning.studentYearCreditList.name}")
    private String studentYearCreditListName;
    @Value("${excel.early.warning.studentYearCreditList.path}")
    private String studentYearCreditListPath;

    //记过以上处分
    @Value("${excel.early.warning.studentPulishList.name}")
    private String studentPulishListName;
    @Value("${excel.early.warning.studentPulishList.path}")
    private String studentPulishListPath;

    //英语四四级分数小于390
    @Value("${excel.early.warning.studentEnglishScoreList.name}")
    private String studentEnglishScoreListName;
    @Value("${excel.early.warning.studentEnglishScoreList.path}")
    private String studentEnglishScoreListPath;


    //班级名次下滑超过十名
    @Value("${excel.early.warning.classRankingSlide.name}")
    private String classRankingSlideName;
    @Value("${excel.early.warning.classRankingSlide.path}")
    private String classRankingSlidePath;


    //智能预警路径
    @Value("${excel.early.warning.intelligenceEarly.path}")
    private String intelligenceEarlyPath;


    //智能预警路径--记过处分
    @Value("${excel.early.warning.intelligenceEarly.path2}")
    private String intelligenceEarlyPath2;


    @Autowired
    private IAcademicServer academicServer;

    /**
     * 学业预警service
     */
    @Autowired
    private IEarlyWarningServer earlyWarningServer;

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
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
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

    /**
     * 绩点排名
     *
     * @param params
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/course/pointTop")
    public void exportPointTopCoursesAOP(AcademicParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> list = academicServer.pointTop(params);
        ExcelUtils.addValue("list", list);
        System.out.println("pointTopPath: " + pointTopPath);
        exportExcelBySheets(request, response, pointTopPath, pointTopFileName, null);
    }

    /**
     * 上课考勤打卡--群体
     *
     * @param params
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/course/attendanceCard")
    public void exportattendanceCardCoursesAOP(AcademicParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //打卡记录，默认最多答应一万条
        if (params != null) {
            params.setStart(0);
            params.setLimit(10000);

            String startTime = params.getStartTime();
            String endTime = params.getEndTime();
            params.setStartTime(startTime + " 00:00:00");
            params.setEndTime(endTime + " 23:59:59");
            //为了配合前端，前端传null,默认查询所有教室名
            if (com.ziyun.utils.common.StringUtils.isBlank(params.getClassName())) {
                params.setClassName(null);
            }
        }
        List<Map<String, Object>> list = academicServer.attendanceCard(params);
        ExcelUtils.addValue("list", list);
        exportExcelBySheets(request, response, attendanceCardPath, attendanceCardFileName, null);
    }

    /**
     * 课程分类弹窗列表下载
     *
     * @param params
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception
     */
    @RequestMapping("/course/courseCategoryStudentList")
    public void exportcourseCategoryStudentListAOP(AcademicParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<Map<String, Object>> list = academicServer.courseCategoryStudentList(params);
        ExcelUtils.addValue("list", list);
        exportExcelBySheets(request, response, courseCatagoryStudentListPath, courseCatagoryStudentListFileName, null);
    }

    /**
     * 课程性质弹窗列表下载
     *
     * @param params
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception
     */
    @RequestMapping("/course/courseNaturesStudentList")
    public void exportcourseNaturesStudentListAOP(AcademicParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<Map<String, Object>> list = academicServer.courseCategoryStudentList(params);
        ExcelUtils.addValue("list", list);
        exportExcelBySheets(request, response, courseNaturesStudentListPath, courseNaturesStudentListFileName, null);
    }

    /**
     * 课程属性弹窗列表下载
     *
     * @param params
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception
     */
    @RequestMapping("/course/coursePropertiesStudentList")
    public void exportcoursePropertiesStudentListAOP(AcademicParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<Map<String, Object>> list = academicServer.courseCategoryStudentList(params);
        ExcelUtils.addValue("list", list);
        exportExcelBySheets(request, response, coursePropertiesStudentListPath, coursePropertiesStudentListFileName, null);
    }


    /**
     * 学业预警 导出人员列表数据
     */

    @RequestMapping("/academic/early/warning/students")
    public void getAcademicEarlyWarningStudentsAOP(AcademicParams params, HttpServletRequest request, HttpServletResponse response, String type, String schoolName, String facultyName, String majorName, String classCode, String year, String lastSemester) throws Exception {
        List<Map<String, Object>> list = null;
        String downPath = null;
        String downFileName = null;
        switch (type) {
            case "1":
                //必修课挂科门数在2--4门之间
                list = academicServer.compulsoryCoursesList(params);
                downFileName = compulsoryCoursesCount4Name;
                downPath = compulsoryCoursesCount4Path;
                break;
            case "2":
                //学年总学分小于18学分
                if (params != null && params.getSemesterArr() != null && params.getSemesterArr().length > 0) {
                    String[] semesterArr = {params.getSemesterArr()[0].substring(0, 9) + "-1", params.getSemesterArr()[0].substring(0, 9) + "-2"};
                    params.setSemesterArr(semesterArr);
                }
                list = academicServer.getStudentYearCreditList(params);
                downFileName = studentYearCreditListName;
                downPath = studentYearCreditListPath;
                break;
            case "3":
                //记过以上处分
                list = academicServer.getStudentPulishList(params);
                downFileName = studentPulishListName;
                downPath = studentPulishListPath;
                break;
            case "4":
                //英语四四级分数小于390
                list = academicServer.getStudentEnglishScoreList(params);
                downFileName = studentEnglishScoreListName;
                downPath = studentEnglishScoreListPath;
                break;
            case "6":
                //班级名次下滑超过十名
                list = academicServer.getStudentClassRankingSlideList(params);
                downFileName = classRankingSlideName;
                downPath = classRankingSlidePath;
                ExcelUtils.addValue("lastSemester", lastSemester);
                break;
            case "7":
                //必修课挂科门数在2--4门之间
                list = academicServer.compulsoryCoursesList(params);
                downFileName = compulsoryCoursesCount2Name;
                downPath = compulsoryCoursesCount2Path;
                break;
            default:break;
        }
        if (StringUtils.isBlank(schoolName) && StringUtils.isBlank(facultyName) && StringUtils.isBlank(majorName) && StringUtils.isBlank(classCode) && StringUtils.isBlank(year)) {
            ExcelUtils.addValue("schoolName", "全部");
        } else {
            ExcelUtils.addValue("schoolName", schoolName);
            ExcelUtils.addValue("facultyName", facultyName);
            ExcelUtils.addValue("majorName", majorName);
            ExcelUtils.addValue("classCode", classCode);
            ExcelUtils.addValue("year", year);
        }
        if (null != params && ArrayUtils.isNotEmpty(params.getSemesterArr())) {
            ExcelUtils.addValue("semester", params.getSemesterArr()[0]);
        }
        ExcelUtils.addValue("list", list);

        exportExcelBySheets(request, response, downPath, downFileName, null);
    }


    @RequestMapping("/academic/early/warning/targetStudent")
    public void targetStudentAOP(AcademicParams params, HttpServletRequest request, HttpServletResponse response, String type, String schoolName, String facultyName, String majorName, String classInfo, String year, String lastSemester, String fileName, String paramName, String paramValue, String semesterName) throws Exception {
        List<Map<String, Object>> list = earlyWarningServer.getTargetStudent(params);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(r -> {
                Object value = r.get(paramValue);
                r.put("paramValue", value);
            });
        }
        ExcelUtils.addValue("paramName", paramName);
        if (StringUtils.isBlank(schoolName) && StringUtils.isBlank(facultyName) && StringUtils.isBlank(majorName) && StringUtils.isBlank(classInfo) && StringUtils.isBlank(year)) {
            ExcelUtils.addValue("schoolName", "全部");
        } else {
            ExcelUtils.addValue("schoolName", schoolName);
            ExcelUtils.addValue("facultyName", facultyName);
            ExcelUtils.addValue("majorName", majorName);
            ExcelUtils.addValue("classInfo", classInfo);
            ExcelUtils.addValue("year", year);
        }
        if (params.getSemesterArr() != null && params.getSemesterArr().length > 0) {
            ExcelUtils.addValue("semester", semesterName);
        }
        ExcelUtils.addValue("list", list);
        ExcelUtils.addValue("fileName", fileName);
        if (StringUtils.isNotBlank(fileName) && "记过处分".equals(fileName)) {
            //记过处分下载
            exportExcelBySheets(request, response, intelligenceEarlyPath2, fileName, null);
        } else {
            //学业模块下载
            exportExcelBySheets(request, response, intelligenceEarlyPath, fileName, null);
        }
    }



}
