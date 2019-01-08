package com.ziyun.report.controller;

import com.ziyun.report.constant.Constants;
import com.ziyun.report.enums.EndingsEnum;
import com.ziyun.report.model.*;
import com.ziyun.report.response.CommonResponse;
import com.ziyun.report.service.IReportService;
import com.ziyun.report.util.EncryptUtil;
import com.ziyun.utils.date.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.http.util.TextUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 毕业报告Controller
 */
@RestController
@RequestMapping("/api/v2/report")
@Api(tags = "毕业报告", description = "毕业报告相关api")
public class ReportController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DateUtils.class);

    @Autowired
    private IReportService reportService;

    @Value("${front.signUrl}")
    private String reportH5Url;

    /**
     * 根据学号，查询必修课，选修课各多少门
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/academic/getAcademicCourse", method = RequestMethod.GET)
    @ApiOperation(value = "获取个人必修课，选修课各多少门")
    @ApiImplicitParam(paramType = "query", name = "outid", value = "学号id", required = true, dataType = "String")
    public CommonResponse getAcademicCourse(Param param) {
        List<Map<String, Object>> list = reportService.getAcademicCourse(param.getOutid());
        return CommonResponse.success(list);
    }

    /**
     * 获取个人学业成绩合格率
     */
    @RequestMapping(value = "/academic/getPassRations", method = RequestMethod.GET)
    @ApiOperation(value = "获取个人学业成绩合格率")
    @ApiImplicitParam(paramType = "query", name = "outid", value = "学号id", required = true, dataType = "String")
    public CommonResponse getPassRations(Param param) {
        String passRations = null;
        try {
            passRations = reportService.getPassRations(param.getOutid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResponse.success(passRations);
    }

    /**
     * 获取专业课成绩最高分的课程
     */
    @RequestMapping(value = "/academic/getHighScore", method = RequestMethod.GET)
    @ApiOperation(value = "获取专业课成绩最高分的课程")
    @ApiImplicitParam(paramType = "query", name = "outid", value = "学号id", required = true, dataType = "String")
    public CommonResponse getHighScore(Param param) {
        List<Map<String, Object>> resultList = reportService.getHighScore(param.getOutid());
        return CommonResponse.success(resultList);
    }

    /**
     * 获取该学生，打卡次数最多的教室
     */
    @RequestMapping(value = "/academic/getBestTimesClass", method = RequestMethod.GET)
    @ApiOperation(value = "获取该学生，打卡次数最多的教室")
    @ApiImplicitParam(paramType = "query", name = "outid", value = "学号id", required = true, dataType = "String")
    public CommonResponse getBestTimesClass(Param param) {
        String beatTimesClass = reportService.getBestTimesClass(param.getOutid());
        return CommonResponse.success(beatTimesClass);
    }

    /**
     * 获取个人消费基本详情
     */
    @RequestMapping(value = "/consume/getConsumeDeatails", method = RequestMethod.GET)
    @ApiOperation(value = "获取个人消费基本详情")
    @ApiImplicitParam(paramType = "query", name = "outid", value = "学号id", required = true, dataType = "String")
    public CommonResponse getConsumeDeatails(Param param) {
        Map<String, Object> resultMap = reportService.getConsumeDeatails(param.getOutid());
        return CommonResponse.success(resultMap);
    }

    @RequestMapping(value = "/preferencelistnot", method = RequestMethod.GET)
    @ApiOperation(value = "个人消费类目百分比")
    @ApiImplicitParam(paramType = "query", name = "outid", value = "学号id", required = true, dataType = "String")
    public CommonResponse preferenceListNot(String outid) {
        List<Map<String, Object>> list = reportService.preferenceListNot(outid);
        return CommonResponse.success(list);
    }

    /**
     * 获取借阅的基本详情
     * "data": {
     * "days": 4066,  借阅总天数
     * "BOOK_NAME": "机器人学、机器视觉与控制:MATLAB算法基础:fundamental algorithms in MATLAB", 喜欢看的书籍
     * "day": 439,  该书籍借阅时长
     * "bookNums": 55,  共借阅的书籍本数
     * "bookType": "工业技术"   最喜欢的书籍类型
     * }
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/borrow/getBorrowDetails", method = RequestMethod.GET)
    @ApiOperation(value = "获取个人借阅基本详情")
    @ApiImplicitParam(paramType = "query", name = "outid", value = "学号id", required = true, dataType = "String")
    public CommonResponse getBorrowDetails(Param param) {
        Map<String, Object> resultMap;
        try {
            resultMap = reportService.getBorrowDetails(param.getOutid());
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResponse.failure(e);
        }
        return CommonResponse.success(resultMap);
    }

    /**
     * 学生概述 -- 毕业生标签
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/overviewOfStudents", method = RequestMethod.GET)
    @ApiOperation(value = "获取学生概述标签信息")
    @ApiImplicitParam(paramType = "query", name = "outid", value = "学号id", required = true, dataType = "String")
    public CommonResponse overviewOfStudents(Param param) {
        //查询标签
        GraduateLabel label;
        try {
            label = reportService.queryGraduateLabel(param.getOutid());
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResponse.failure(e);
        }
        return CommonResponse.success(label);
    }

    /**
     * 详情展示 -- 学业
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/overviewOfAcademic", method = RequestMethod.GET)
    @ApiOperation(value = "获取学生学业信息")
    @ApiImplicitParam(paramType = "query", name = "outid", value = "学号id", required = true, dataType = "String")
    public CommonResponse overviewOfAcademic(Param param) {
        Map<String, Object> resultMap;
        try {
            resultMap = reportService.getAcademicOfStudent(param.getOutid());
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResponse.failure(e);
        }
        return CommonResponse.success(resultMap);
    }

    /**
     * 详情展示 -- 消费
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/overviewOfConsume", method = RequestMethod.GET)
    @ApiOperation(value = "获取学生消费信息")
    @ApiImplicitParam(paramType = "query", name = "outid", value = "学号id", required = true, dataType = "String")
    public CommonResponse overviewOfConsume(Param param) {
        Map<String, Object> resultMap;
        try {
            resultMap = reportService.getConsumeOfStudent(param.getOutid());
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResponse.failure(e);
        }
        return CommonResponse.success(resultMap);
    }

    /**
     * 详情展示 -- 上网
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/detailViewOfOnline", method = RequestMethod.GET)
    @ApiOperation(value = "获取个人上网基本详情")
    @ApiImplicitParam(paramType = "query", name = "outid", value = "学号id", required = true, dataType = "String")
    public CommonResponse detailViewOfOnline(Param param) {
        Map<String, Object> resultMap;
        try {
            resultMap = reportService.getOnlineOfStudent(param.getOutid());
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResponse.failure(e);
        }
        return CommonResponse.success(resultMap);
    }

    /**
     * 详情展示 -- 校园活动
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/detailViewOfActivity", method = RequestMethod.GET)
    @ApiOperation(value = "获取个人校园活动基本详情")
    @ApiImplicitParam(paramType = "query", name = "outid", value = "学号id", required = true, dataType = "String")
    public CommonResponse detailViewOfActivity(Param param) {
        Map<String, Object> map;
        try {
            map = reportService.getActivityOfStudent(param.getOutid());
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResponse.failure(e);
        }
        return CommonResponse.success(map);
    }

    /**
     * 详细展示 - 获奖情况
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/detailViewOfScholarship", method = RequestMethod.GET)
    @ApiOperation(value = "获取个人获奖情况")
    @ApiImplicitParam(paramType = "query", name = "outid", value = "学号id", required = true, dataType = "String")
    public CommonResponse detailViewOfScholarship(Param param) {
        Map<String, Object> resultMap;
        try {
            resultMap = reportService.getScholarshipOfStudent(param.getOutid());
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResponse.failure(e);
        }
        return CommonResponse.success(resultMap);
    }

    /**
     * 详细展示 - 学生会会长
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/detailViewOfAssociation", method = RequestMethod.GET)
    @ApiOperation(value = "获取学生会会长信息")
    @ApiImplicitParam(paramType = "query", name = "outid", value = "学号id", required = true, dataType = "String")
    public CommonResponse detailViewOfAssociation(Param param) {
        Map<String, Object> resultMap;
        try {
            resultMap = reportService.getAssociationOfStudent(param.getOutid());
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResponse.failure(e);
        }
        return CommonResponse.success(resultMap);
    }

    /**
     * 结尾寄语
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/endingsViewOfStudent", method = RequestMethod.GET)
    @ApiOperation(value = "获取报告结尾寄语")
    @ApiImplicitParam(paramType = "query", name = "outid", value = "学号id", required = true, dataType = "String")
    public CommonResponse endingsViewOfStudent(Param param) {
        List<String> dataList = new ArrayList<>();
        try {
            GraduateEndings endings = reportService.queryGraduateEndings(param.getOutid());
            if (endings == null) {
                return CommonResponse.success(dataList);
            }

            if (endings.getEndings() != null && endings.getEndings() != "") {
                String[] arrEndings = endings.getEndings().split(",");
                Arrays.stream(arrEndings).forEach(e -> {
                    dataList.add(EndingsEnum.get(e).getEndings());
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResponse.failure(e);
        }
        return CommonResponse.success(dataList);
    }

    /**
     * 生成毕业生标签寄语
     *
     * @return
     */
    @RequestMapping(value = "/graduateStat", method = RequestMethod.GET)
    @ApiOperation(value = "生成毕业生标签寄语")
    public CommonResponse graduateStat() {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            //标签
            reportService.graduateLabelStat();
            //寄语
            reportService.graduateEndingsStat();
            resultMap.put("success", "执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResponse.failure(e);
        }
        return CommonResponse.success(resultMap);
    }

    /**
     * 数据预存redis
     *
     * @return
     */
    @RequestMapping(value = "/dataSaveRedis", method = RequestMethod.GET)
    @ApiOperation(value = "毕业报告数据存入Redis")
    public CommonResponse dataSaveRedis() {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<Student> studentList = reportService.getGraduate();
            studentList.stream().forEach(s -> {
                //1、学生概述
                reportService.queryGraduateLabel(s.getOutid());
                //2、学业
                reportService.getAcademicOfStudent(s.getOutid());
                //3、消费
                reportService.getConsumeOfStudent(s.getOutid());
                //4、借阅
                reportService.getBorrowDetails(s.getOutid());
                //5、上网
                reportService.onlineOfStudent(s);
                //6、活动
                reportService.getActivityOfStudent(s.getOutid());
                //7、获奖
                reportService.getScholarshipOfStudent(s.getOutid());
                //8、结尾寄语
                reportService.queryGraduateEndings(s.getOutid());
                //9、学生会会长
                reportService.getAssociationOfStudent(s.getOutid());
            });
            resultMap.put("success", "执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResponse.failure(e);
        }
        return CommonResponse.success(resultMap);
    }

    @RequestMapping(value = "/getStudentSchoolType", method = RequestMethod.GET)
    @ApiOperation(value = "获取该学生校区名称")
    @ApiImplicitParam(paramType = "query", name = "outid", value = "学号id", required = true, dataType = "String")
    public CommonResponse getStudentSchoolType(Param param) {
        return CommonResponse.success(reportService.getStudentSchoolType(param));
    }


    @RequestMapping(value = "/isGraduationStudent", method = RequestMethod.GET)
    @ApiOperation(value = "判断该学生是否是毕业生")
    @ApiImplicitParam(paramType = "query", name = "outid", value = "学号id", required = true, dataType = "String")
    public void isGraduationStudent(RedirectParam param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String outId = param.getOutid();
        logger.debug("outid = " + outId);
        if (TextUtils.isEmpty(outId)) {
            return;
        }
        String redirectUrl = param.getRedirectUrl();
        logger.debug("redirectUrl = " + redirectUrl);
        Map<String, Integer> result = reportService.isGraduationStudent(param.getOutid());
        int code = result.get("code");
        int visitCount = result.get("visits");
        // 非毕业生，跳转重定向地址
        if (code != 1) {
            response.sendRedirect(redirectUrl);
            return;
        }
        String encryptURL = reportH5Url + Constants.NAME_OUT_ID + handleURLEncrypt(outId);
        logger.debug("encryptURL = " + encryptURL);
        // redirectUrl为空，视为【点击按钮】请求, 弹出报告H5
        if (TextUtils.isEmpty(redirectUrl)) {
            response.sendRedirect(encryptURL);
            return;
        }

        // redirectUrl不为空，视为【登录】请求，如果是第一次登陆，弹出报告H5；
        if (!TextUtils.isEmpty(redirectUrl) && visitCount == 0) {
            response.sendRedirect(encryptURL);
        }else  {
            response.sendRedirect(redirectUrl);
        }
    }

    /**
     * 请求毕业报告首页，访问次数+1
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/homePageView", method = RequestMethod.GET)
    @ApiOperation(value = "请求毕业报告首页，访问量+1")
    @ApiImplicitParam(paramType = "query", name = "outid", value = "学号id", required = true, dataType = "String")
    public CommonResponse homePageView(Param param) {
        reportService.visitsIncrease(param.getOutid());
        return CommonResponse.success();
    }

    private String handleURLEncrypt(String outid) {
        //第一次加密
        String encoderStr = Base64.getEncoder().encodeToString(outid.getBytes());
        String salt = EncryptUtil.getCharAndNumr(Constants.OFFSET_SALT_FIRST);
        //第二次加密，先加盐
        encoderStr = Base64.getEncoder().encodeToString((salt + encoderStr).getBytes());
        salt = EncryptUtil.getCharAndNumr(Constants.OFFSET_SALT_SECOND);
        //第二次加密完，再加盐
        return salt + encoderStr;
    }

}