package com.ziyun.academic.controller;


import com.ziyun.academic.server.IAcademicServer;
import com.ziyun.academic.server.IStudentServer;
import com.ziyun.academic.vo.AcademicParams;
import com.ziyun.academic.vo.Params;
import com.ziyun.utils.requests.CommResponse;
import com.ziyun.utils.requests.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: yk.tan
 * @since: 2017/5/17
 * @history:
 */
@Controller
@RequestMapping("/v2/academic")
@Api(tags = "学业微服务", description = "学业特征微服务相关api")
public class AcademicController {


    @Autowired
    private IAcademicServer academicServer;
    @Autowired
    private IStudentServer studentServer;

    @RequestMapping("/features")
    @ResponseBody
    @ApiOperation(value = "综合概述", notes = "综合概述--学业特征", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code: slg(苏理工)|jkd(江科大)"),
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
            @ApiImplicitParam(paramType = "query", name = "impoverish", dataType = "Integer", value = "传1：贫困生，传参过来，即可；或者不传"),
    })
    public CommResponse academicFeatures(AcademicParams para) throws Exception {

        //判断如果是否本科生
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }

        Map<String, Object> result = academicServer.getAcademicFeatures(para);
        if (null == result || result.isEmpty()) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }

    /**
     * @api {POST} /api/v2/academic/course/properties
     * @apiName courseProperties
     * @apiGroup academic
     * @apiVersion 2.0.0
     * @apiDescription 学业特征-课程选择-课程属性-图
     * @apiPermission course_property
     * @apiSampleRequest http://localhost:8080/api/v2/academic/course/properties
     * @apiParam {String} [schoolCode] 校区code
     * @apiParam {String} [facultyCode] 院系code
     * @apiParam {String} [majorCode] 专业code
     * @apiParam {String} [classSelect] 班级code多选，用逗号隔开，班级只有code,没有名称
     * @apiParam {String} [semester]  学期：本学期、上学期 格式：2016-2017-1 ; 上学年 格式：2016-2017
     * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
     * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
     * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
     * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
     * @apiParamExample {json} 请求例子:
     * {
     * "schoolCode": "jkd"
     * }
     * @apiSuccess (200) {String} message 操作成功
     * @apiSuccess (200) {int} statusCode 200
     * @apiSuccessExample {json} 返回样例:
     * {"statusCode":200,
     * "message":"操作成功",
     * "data":[{"num":1351,"course_properties":"必修"},{"num":549,"course_properties":"选修"},{"num":327,"course_properties":"同属必修选修"}]
     * }
     */
    @RequestMapping("/course/properties")
    @ResponseBody
    @ApiOperation(value = "群体--学业特征-学业分类-课程属性", notes = "课程属性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse coursePropertiesAOP(AcademicParams para) throws Exception {

        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        List<Map<String, Object>> result = academicServer.getCourseProperties(para);

        if (null == result || result.size() == 0) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }


    @RequestMapping("/course/coursePropertiesStudentList")
    @ResponseBody
    @ApiOperation(value = "群体--学业特征-学业分类-课程属性", notes = "课程属性-柱形图对应的学生列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse coursePropertiesStudentListAOP(AcademicParams para) throws Exception {

        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        //设置sql
        para.setSql(" AND eg.course_properties IS NOT NULL");
        List<Map<String, Object>> result = academicServer.courseCategoryStudentList(para);

        if (null == result || result.size() == 0) {
            return CommResponse.success(null);
        }

        return CommResponse.success(new PageResult(limitPage(result, para.getStart(), para.getLimit()), result.size()));
    }


    /**
     * @api {POST} /api/v2/academic/course/propertiesStudent
     * @apiName coursePropertiesStudent
     * @apiGroup academic
     * @apiVersion 2.0.0
     * @apiDescription 学业特征-课程选择-课程分类-图
     * @apiPermission course_property
     * @apiSampleRequest http://localhost:8080/api/v2/academic/course/propertiesStudent
     * @apiParam {String} [schoolCode] 校区code
     * @apiParam {String} [facultyCode] 院系code
     * @apiParam {String} [majorCode] 专业code
     * @apiParam {String} [classSelect] 班级code多选，用逗号隔开，班级只有code,没有名称
     * @apiParam {String} [semester]  学期：本学期、上学期 格式：2016-2017-1 ; 上学年 格式：2016-2017
     * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
     * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
     * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
     * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
     * @apiParamExample {json} 请求例子:
     * {
     * "schoolCode": "jkd"
     * }
     * @apiSuccess (200) {String} message 操作成功
     * @apiSuccess (200) {int} statusCode 200
     * @apiSuccessExample {json} 返回样例:
     * {"statusCode":200,
     * "message":"操作成功",
     * "data":[{"num":1351,"course_properties":"必修"},{"num":549,"course_properties":"选修"},{"num":327,"course_properties":"同属必修选修"}]
     * }
     */
    @RequestMapping("/course/propertiesStudent")
    @ResponseBody
    @ApiOperation(value = "个人--选课类型", notes = "选课类型")
    @ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")
    public CommResponse coursePropertiesStuAOP(AcademicParams para) throws Exception {

        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }

        List<Map<String, Object>> result = academicServer.getCoursePropertiesStudent(para);

        if (null == result || result.size() == 0) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }


    /**
     * @api {POST} /api/v2/academic/course/properties/all
     * @apiName coursePropertiesAll
     * @apiGroup academic
     * @apiVersion 2.0.0
     * @apiDescription 学业特征-课程选择-课程分类-列表
     * @apiPermission course_property_list
     * @apiSampleRequest http://localhost:8080/api/v2/academic/course/properties/all
     * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
     * @apiParam {String} [facultyCode] 院系code
     * @apiParam {String} [majorCode] 专业code
     * @apiParam {String} [classSelect] 班级code多选，用逗号隔开，班级只有code,没有名称
     * @apiParam {String} [semester] 学期：本学期、上学期 格式：2016-2017-1 ; 上学年 格式：2016-2017
     * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
     * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
     * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
     * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
     * @apiParam {String} [courseProperties] 课程属性 0-全部，1-必修，2-选修，3-同属必修选修
     * @apiParam {Integer} [start] 分页参数：从0开始
     * @apiParam {Integer} [limit] 分页参数：每页多少条数据
     * @apiParamExample {json} 请求例子:
     * {
     * "schoolCode": "jkd"
     * }
     * @apiSuccess (200) {String} message 操作成功
     * @apiSuccess (200) {int} statusCode 200
     * @apiSuccessExample {json} 返回样例:
     * {"statusCode":200,
     * "message":"操作成功",
     * "data":[{"course_properties_new":"选修","course_name":"材料力学","num":1,"course_no":"01N10003a","ratio":"0.00%"},
     * {"course_properties_new":"选修","course_name":"船舶与海洋工程导论","num":1,"course_no":"01N10055b","ratio":"100.00%"}
     * ]
     * }
     */
    @RequestMapping("/course/properties/all")
    @ResponseBody
    public CommResponse listCoursePropertiesAOP(AcademicParams para) throws Exception {

        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }

        List<Map<String, Object>> result = academicServer.listCourseProperties(para);

        if (null == result || result.size() == 0) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }

    /**
     * @api {POST} /api/v2/academic/course/properties/count
     * @apiName propertiesCount
     * @apiGroup academic
     * @apiVersion 2.0.0
     * @apiDescription 学业特征-课程选择-课程分类-列表 分页总条数
     * @apiPermission course_property_count
     * @apiSampleRequest http://localhost:8080/api/v2/academic/course/properties/count
     * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
     * @apiParam {String} [facultyCode] 院系code
     * @apiParam {String} [majorCode] 专业code
     * @apiParam {String} [classSelect] 班级code多选，用逗号隔开，班级只有code,没有名称
     * @apiParam {String} [semester] 学期：本学期、上学期 格式：2016-2017-1 ; 上学年 格式：2016-2017
     * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
     * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
     * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
     * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
     * @apiParam {String} [courseProperties] 课程属性 0-全部，1-必修，2-选修，3-同属必修选修
     * @apiParamExample {json} 请求例子:
     * {
     * "schoolCode": "jkd"
     * }
     * @apiSuccess (200) {String} message 操作成功
     * @apiSuccess (200) {int} statusCode 200
     * @apiSuccessExample {json} 返回样例:
     * {"statusCode":200,
     * "message":"操作成功",
     * "data":[2227]
     * }
     */
    @RequestMapping("/course/properties/count")
    @ResponseBody
    public CommResponse countCoursePropertiesAOP(AcademicParams para) throws Exception {

        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }

        return CommResponse.success(academicServer.getCoursePropertiesCount(para));
    }


    @RequestMapping("/course/natures")
    @ResponseBody
    @ApiOperation(value = "群体--学业特征-学业分类-课程性质", notes = "学业特征-学业分类-课程性质")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse courseNaturesAOP(AcademicParams para) throws Exception {

        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }

        List<Map<String, Object>> result = academicServer.courseNatures(para);

        if (null == result || result.size() == 0) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }


    @RequestMapping("/course/courseNaturesStudentList")
    @ResponseBody
    @ApiOperation(value = "群体--学业特征-学业分类-课程性质", notes = "课程性质-柱形图对应的详细的学生列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse courseNaturesStudentListAOP(AcademicParams para) throws Exception {

        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        //设置sql
        para.setSql(" AND eg.course_natures IS NOT NULL");
        List<Map<String, Object>> result = academicServer.courseCategoryStudentList(para);

        if (null == result || result.size() == 0) {
            return CommResponse.success(null);
        }

        return CommResponse.success(new PageResult(limitPage(result, para.getStart(), para.getLimit()), result.size()));
    }


    /**
     * @api {POST} /api/v2/academic/course/natures/all
     * @apiName courseNaturesAll
     * @apiGroup academic
     * @apiVersion 2.0.0
     * @apiDescription 学业特征-课程选择-课程分类-列表
     * @apiPermission course_natures_list
     * @apiSampleRequest http://localhost:8080/api/v2/academic/course/natures/all
     * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
     * @apiParam {String} [facultyCode] 院系code
     * @apiParam {String} [majorCode] 专业code
     * @apiParam {String} [classSelect] 班级code多选，用逗号隔开，班级只有code,没有名称
     * @apiParam {String} [semester] 学期：本学期、上学期 格式：2016-2017-1 ; 上学年 格式：2016-2017
     * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
     * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
     * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
     * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
     * @apiParam {String} [courseNatures] 课程性质
     * @apiParam {Integer} [start] 分页参数：从0开始
     * @apiParam {Integer} [limit] 分页参数：每页多少条数据
     * @apiParamExample {json} 请求例子:
     * {
     * "schoolCode": "jkd"
     * }
     * @apiSuccess (200) {String} message 操作成功
     * @apiSuccess (200) {int} statusCode 200
     * @apiSuccessExample {json} 返回样例:
     * {"statusCode":200,
     * "message":"操作成功",
     * "data":[{"course_name":"船舶与海洋工程导论","num":1,"course_no":"01N10055b","course_natures":"其他","ratio":"100.00%"},
     * {"course_name":"工程力学","num":1,"course_no":"01N10095a","course_natures":"其他","ratio":"100.00%"}
     * ]
     * }
     */
    @RequestMapping("/course/natures/all")
    @ResponseBody
    public CommResponse listCourseNaturesAOP(AcademicParams para) throws Exception {

        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }

        List<Map<String, Object>> result = academicServer.listCourseNatures(para);

        if (null == result || result.size() == 0) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }

    /**
     * @api {POST} /api/v2/academic/course/natures/count
     * @apiName naturesCount
     * @apiGroup academic
     * @apiVersion 2.0.0
     * @apiDescription 学业特征-课程选择-课程性质-列表 分页总条数
     * @apiPermission course_property_count
     * @apiSampleRequest http://localhost:8080/api/v2/academic/course/natures/count
     * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
     * @apiParam {String} [facultyCode] 院系code
     * @apiParam {String} [majorCode] 专业code
     * @apiParam {String} [classSelect] 班级code多选，用逗号隔开，班级只有code,没有名称
     * @apiParam {String} [semester] 学期：本学期、上学期 格式：2016-2017-1 ; 上学年 格式：2016-2017
     * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
     * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
     * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
     * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
     * @apiParam {String} [courseNatures] 课程性质
     * @apiParamExample {json} 请求例子:
     * {
     * "schoolCode": "jkd"
     * }
     * @apiSuccess (200) {String} message 操作成功
     * @apiSuccess (200) {int} statusCode 200
     * @apiSuccessExample {json} 返回样例:
     * {"statusCode":200,
     * "message":"操作成功",
     * "data":[2194]
     * }
     */
    @RequestMapping("/course/natures/count")
    @ResponseBody
    public CommResponse countCourseNaturesAOP(AcademicParams para) throws Exception {

        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }

        return CommResponse.success(academicServer.getCourseNaturesCount(para));
    }

    /**
     * @api {POST} /api/v2/academic/course/categories
     * @apiName courseCategories
     * @apiGroup academic
     * @apiVersion 2.0.0
     * @apiDescription 学业特征-课程选择-课程分类-图
     * @apiPermission course_category
     * @apiSampleRequest http://localhost:8080/api/v2/academic/course/categories
     * @apiParam {String} [schoolCode] 校区code
     * @apiParam {String} [facultyCode] 院系code
     * @apiParam {String} [majorCode] 专业code
     * @apiParam {String} [classSelect] 班级code多选，用逗号隔开，班级只有code,没有名称
     * @apiParam {String} [semester]  学期：本学期、上学期 格式：2016-2017-1 ; 上学年 格式：2016-2017
     * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
     * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
     * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
     * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
     * @apiParamExample {json} 请求例子:
     * {
     * "schoolCode": "jkd"
     * }
     * @apiSuccess (200) {String} message 操作成功
     * @apiSuccess (200) {int} statusCode 200
     * @apiSuccessExample {json} 返回样例:
     * {"statusCode":200,
     * "message":"操作成功",
     * "data":[{"num":1675,"course_category":"理论课（不含实践）"},{"num":500,"course_category":"集中性实践环节"}]
     * }
     */
    @RequestMapping("/course/categories")
    @ResponseBody
    @ApiOperation(value = "群体--学业特征-学业分类-课程选择", notes = "学业特征-学业分类-课程选择")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse courseCategoryAOP(AcademicParams para) throws Exception {

        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }

        List<Map<String, Object>> result = academicServer.getCourseCategories(para);

        if (null == result || result.size() == 0) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }

    @RequestMapping("/course/courseCategoryStudentList")
    @ResponseBody
    @ApiOperation(value = "群体--学业特征-学业分类-课程选择", notes = "学业分类-柱形图学生列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse courseCategoryStudentListAOP(AcademicParams para) throws Exception {
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        //设置sql
        para.setSql(" AND eg.course_category IS NOT NULL");
        List<Map<String, Object>> result = academicServer.courseCategoryStudentList(para);

        if (null == result || result.size() == 0) {
            return CommResponse.success(null);
        }

        return CommResponse.success(new PageResult(limitPage(result, para.getStart(), para.getLimit()), result.size()));
    }

    /**
     * @api {POST} /api/v2/academic/course/categories/all
     * @apiName courseCategoriesAll
     * @apiGroup academic
     * @apiVersion 2.0.0
     * @apiDescription 学业特征-课程选择-课程分类-列表
     * @apiPermission course_category_list
     * @apiSampleRequest http://localhost:8080/api/v2/academic/course/categories/all
     * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
     * @apiParam {String} [facultyCode] 院系code
     * @apiParam {String} [majorCode] 专业code
     * @apiParam {String} [classSelect] 班级code多选，用逗号隔开，班级只有code,没有名称
     * @apiParam {String} [semester] 学期：本学期、上学期 格式：2016-2017-1 ; 上学年 格式：2016-2017
     * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
     * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
     * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
     * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
     * @apiParam {String} [courseNatures] 课程性质
     * @apiParam {Integer} [start] 分页参数：从0开始
     * @apiParam {Integer} [limit] 分页参数：每页多少条数据
     * @apiParamExample {json} 请求例子:
     * {
     * "schoolCode": "jkd"
     * }
     * @apiSuccess (200) {String} message 操作成功
     * @apiSuccess (200) {int} statusCode 200
     * @apiSuccessExample {json} 返回样例:
     * {"statusCode":200,
     * "message":"操作成功",
     * "data":[{"course_name":"船舶与海洋工程导论","num":1,"course_no":"01N10055b","course_natures":"其他","ratio":"100.00%"},
     * {"course_name":"工程力学","num":1,"course_no":"01N10095a","course_natures":"其他","ratio":"100.00%"}
     * ]
     * }
     */
    @RequestMapping("/course/categories/all")
    @ResponseBody
    public CommResponse listCourseCategoryAOP(AcademicParams para) throws Exception {

        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }

        List<Map<String, Object>> result = academicServer.listCourseCategory(para);

        if (null == result || result.size() == 0) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }

    /**
     * @api {POST} /api/v2/academic/course/categories/count
     * @apiName categoriesCount
     * @apiGroup academic
     * @apiVersion 2.0.0
     * @apiDescription 学业特征-课程选择-课程性质-列表 分页总条数
     * @apiPermission course_categories_count
     * @apiSampleRequest http://localhost:8080/api/v2/academic/course/categories/count
     * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
     * @apiParam {String} [facultyCode] 院系code
     * @apiParam {String} [majorCode] 专业code
     * @apiParam {String} [classSelect] 班级code多选，用逗号隔开，班级只有code,没有名称
     * @apiParam {String} [semester] 学期：本学期、上学期 格式：2016-2017-1 ; 上学年 格式：2016-2017
     * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
     * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
     * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
     * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
     * @apiParam {String} [courseCategory] 课程分类
     * @apiParamExample {json} 请求例子:
     * {
     * "schoolCode": "jkd"
     * }
     * @apiSuccess (200) {String} message 操作成功
     * @apiSuccess (200) {int} statusCode 200
     * @apiSuccessExample {json} 返回样例:
     * {"statusCode":200,
     * "message":"操作成功",
     * "data":{2226}
     * }
     */
    @RequestMapping("/course/categories/count")
    @ResponseBody
    public CommResponse countCourseCategoryAOP(AcademicParams para) throws Exception {

        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }

        return CommResponse.success(academicServer.getCourseCategoryCount(para));
    }


    @RequestMapping("/credit/scorePoints")
    @ResponseBody
    @ApiOperation(value = "群体--学分绩点统计", notes = "学分绩点统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse listScorePointAOP(AcademicParams para) throws Exception {

        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        //学历选择研究生，返回null
        if (para.getEducation() != null && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        List<Map<String, Object>> result = academicServer.listScorePoint(para);

        if (CollectionUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }


    @RequestMapping("/credit/scorePointList")
    @ResponseBody
    @ApiOperation(value = "群体--学分绩点统计弹窗列表", notes = "学业特征-学分绩点统计弹窗列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse scorePointListAOP(AcademicParams para) throws Exception {

        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        //学历选择研究生，返回null
        if (para.getEducation() != null && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        List<Map<String, Object>> result = academicServer.scorePointList(para);

        if (CollectionUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(new PageResult(limitPage(result, para.getStart(), para.getLimit()), result.size()));
    }


    /**
     * @api {POST} /api/v2/academic/exam/passRatiosStudent
     * @apiName passRatiosStudent
     * @apiGroup academic
     * @apiVersion 2.0.0
     * @apiDescription 学业特征-成绩合格率
     * @apiPermission score_pass_ratio
     * @apiSampleRequest http://localhost:8080/api/v2/academic/exam/passRatiosStudent
     * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
     * @apiParam {String} [facultyCode] 院系code
     * @apiParam {String} [majorCode] 专业code
     * @apiParam {String} [classSelect] 班级code多选，用逗号隔开，班级只有code,没有名称
     * @apiParam {String} [semester] 学期：本学期、上学期 格式：2016-2017-1 ; 上学年 格式：2016-2017
     * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
     * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
     * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
     * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
     * @apiParam {String} [courseProperties] 课程属性 0-全部，1-必修，2-选修，3-同属必修选修
     * @apiParamExample {json} 请求例子:
     * {
     * "schoolCode": "jkd"
     * }
     * @apiSuccess (200) {String} message 操作成功
     * @apiSuccess (200) {int} statusCode 200
     * @apiSuccessExample {json} 返回样例:
     * {"statusCode":200,
     * "message":"操作成功",
     * "data":{"ratio":92.46,"passNum":408472,"allNum":441790}
     * }
     */
    @RequestMapping("/exam/passRatiosStudent")
    @ResponseBody
    @ApiOperation(value = "个人--成绩合格率", notes = "成绩合格率")
    @ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")
    public CommResponse getPassRatiosStuAOP(AcademicParams para) throws Exception {

        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }

        Map<String, Object> result = academicServer.getPassRatios(para);

        if (MapUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }


    @RequestMapping("/exam/passRatios")
    @ResponseBody
    @ApiOperation(value = "群体--成绩合格率", notes = "学业特征-成绩合格率")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse getPassRatiosAOP(AcademicParams para) throws Exception {

        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }

        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        Map<String, Object> result = academicServer.getPassRatios(para);

        if (MapUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }


    @RequestMapping("/exam/failCounts")
    @ResponseBody
    @ApiOperation(value = "群体--挂科统计", notes = "学业特征-挂科统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse listExamFailCountAOP(AcademicParams para) throws Exception {
        //学历选择研究生，返回null
        if (para.getEducation() != null && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }

        List<Map<String, Object>> result = academicServer.listExamFailCount(para);

        if (CollectionUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }


    @RequestMapping("/failCourseStudentList")
    @ResponseBody
    @ApiOperation(value = "挂科统计弹窗", notes = "学业特征-挂科统计弹窗")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse failCourseStudentListAOP(AcademicParams params) {
        List<Map<String, Object>> list = academicServer.failCourseStudentList(params);
        if (CollectionUtils.isNotEmpty(list)) {
            return CommResponse.success(new PageResult(limitPage(list, params.getStart(), params.getLimit()), list.size()));
        }

        return CommResponse.success(list);
    }


    @RequestMapping("/exam/passCourses")
    @ResponseBody
    @ApiOperation(value = "群体--考试通过情况", notes = "学业特征-考试通过情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse listExamPassCourseAOP(AcademicParams para) throws Exception {
        //学历选择研究生，返回null
        if (para.getEducation() != null && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }

        List<Map<String, Object>> result = academicServer.listExamPassCourse(para);

        if (CollectionUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }


    @RequestMapping("/exam/passFailCourseList")
    @ResponseBody
    @ApiOperation(value = "考试通过情况弹窗", notes = "考试通过情况弹窗")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse passFailCourseListAOP(AcademicParams para) throws Exception {
        //学历选择研究生，返回null
        if (para.getEducation() != null && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }

        List<Map<String, Object>> result = academicServer.passFailCourseList(para);

        if (CollectionUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(new PageResult(limitPage(result, para.getStart(), para.getLimit()), result.size()));
    }


    @RequestMapping("/credit/completion")
    @ResponseBody
    @ApiOperation(value = "群体--修学分情况", notes = "学业特征-修学分情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse listCreditSituationAOP(AcademicParams para) {

        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        List<Map<String, Object>> result = academicServer.listCreditSituation(para);

        if (CollectionUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }


    @RequestMapping("/credit/completionCourseList")
    @ResponseBody
    @ApiOperation(value = "修学分情况弹窗", notes = "修学分情况弹窗")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse completionCourseListAOP(AcademicParams para) {

        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        List<Map<String, Object>> result = academicServer.completionCourseList(para);

        if (CollectionUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(new PageResult(limitPage(result, para.getStart(), para.getLimit()), result.size()));
    }


    @RequestMapping("/credit/completionStudent")
    @ResponseBody
    @ApiOperation(value = "个人--修学分情况", notes = "修学分情况")
    @ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")
    public CommResponse listCreditSituationStu(AcademicParams para) {

        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }

        List<Map<String, Object>> result = academicServer.listCreditSituation(para);

        if (CollectionUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }


    @RequestMapping("/credit/completionRatio")
    @ResponseBody
    @ApiOperation(value = "群体--毕业所需总学分完成率", notes = "学业特征-毕业所需总学分完成率")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse getGraduationCreditAOP(AcademicParams para) {

        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        Map<String, Object> result = academicServer.getGraduationCredit(para);

        if (MapUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }

    /**
     * @api {POST} /api/v2/academic/credit/completionRatioStudent
     * @apiName creditompletionRatioStudent
     * @apiGroup academic
     * @apiVersion 2.0.0
     * @apiDescription (个人画像)学业特征-毕业所需学分完成率
     * @apiPermission gradu_credit
     * @apiSampleRequest http://localhost:8080/api/v2/academic/credit/completionRatioStudent
     * @apiParam {String} [schoolCode] 校区code
     * @apiParam {String} [facultyCode] 院系code
     * @apiParam {String} [majorCode] 专业code
     * @apiParam {String} [classSelect] 班级code多选，用逗号隔开，班级只有code,没有名称
     * @apiParam {String} [semester]  学期：本学期、上学期 格式：2016-2017-1 ; 上学年 格式：2016-2017
     * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
     * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
     * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
     * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
     * @apiParamExample {json} 请求例子:
     * {
     * "schoolCode": "jkd"
     * }
     * @apiSuccess (200) {String} message 操作成功
     * @apiSuccess (200) {int} statusCode 200
     * @apiSuccessExample {json} 返回样例:
     * {"statusCode":200,
     * "message":"操作成功",
     * "data":{"pass_credit_sum":540676.3,"graduation_credit_sum":930623.8,"ratio":"58.10%"}
     * }
     */
    @RequestMapping("/credit/completionRatioStudent")
    @ResponseBody
    @ApiOperation(value = "个人--总学分完成率", notes = "总学分完成率")
    @ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")
    public CommResponse getGraduationCreditStuAOP(AcademicParams para) {

        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }

        Map<String, Object> result = academicServer.getGraduationCredit(para);

        if (MapUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }


    @RequestMapping("/exam/retakeCourses")
    @ResponseBody
    @ApiOperation(value = "群体--重修课程", notes = "学业特征-重修课程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse retakeCoursesTopAOP(AcademicParams para) throws IOException {

        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }

        Map<String, Object> result = academicServer.retakeCoursesTop(para);

        if (MapUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }

    @RequestMapping("/exam/retakeStudent")
    @ResponseBody
    @ApiOperation(value = "重修课程弹窗", notes = "重修课程弹窗")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse retakeStudentListAOP(AcademicParams para) throws IOException {
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        if (null != para.getStart()) {
            Map map = new HashedMap();
            List<Map<String, Object>> studentList = academicServer.retakeStudentMessage(para);
            map.put("data", limitPage(studentList, para.getStart(), para.getLimit()));
            map.put("total", studentList.size());
            return CommResponse.success(map);
        } else {
            return CommResponse.success(null);
        }
    }

    //获取重修课程信息（对应重修课程人数最多的TOP10）
    @RequestMapping("/exam/listRetakeCourses")
    @ResponseBody
    public List<Map<String, Object>> listRetakeCoursesByGradeLevel(AcademicParams para) {

        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return null;
        }

        List<Map<String, Object>> result = academicServer.listRetakeCourses(para);
        return result;
    }

    //http://localhost:8080/academic/student/getTopCategory?orgCode=11
    //组织机构分类
    @RequestMapping("/student/getTopCategory")
    @ResponseBody
    public Map getTopCategory(AcademicParams paras) throws IOException {

        Map result = academicServer.getTopCategory(paras);
        return result;
    }


    @RequestMapping("/label/getAcademicLabel")
    @ResponseBody
    public Map<String, Object> getAcademicLabelAOP(AcademicParams params) throws IOException {

        return academicServer.getAcademicLabel(params);
    }

    /******************顶部条件查询*************************/
    @RequestMapping("/student/getTopParent")
    @ResponseBody
    public List<String> getTopParent(AcademicParams params) throws IOException {
        return academicServer.getParentcode(params);
    }

    @RequestMapping("/student/getTopChildren")
    @ResponseBody
    public List<Map> getTopChildren(AcademicParams params) throws IOException {
        return academicServer.getChildrenList(params);
    }

    //手动分页的方法
    private List<Map<String, Object>> limitPage(List<Map<String, Object>> list, int start, int length) {
        int end = 0;
        //null值判断
        if (list == null) {
            return null;
        }
        //如果start大于list的长度则从0开始
        if (list.size() < start) {
            start = 0;
        }
        //如果截取的长度大于list的size则为size
        end = start + length > list.size() ? list.size() : start + length;
        return list.subList(start, end);
    }


    @RequestMapping("/course/pointTop")
    @ResponseBody
    @ApiOperation(value = "群体--绩点排名", notes = "学业特征-绩点排名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse pointTopAOP(AcademicParams para) {
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        List<Map<String, Object>> result = academicServer.pointTop(para);

        if (CollectionUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(new PageResult(limitPage(result, para.getStart(), para.getLimit()), result.size()));

    }


    @RequestMapping("/course/electiveTop")
    @ResponseBody
    @ApiOperation(value = "群体--选修课排名", notes = "学业特征-选修课排名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse electiveTopAOP(AcademicParams para) {
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }

        List<Map<String, Object>> result = academicServer.electiveTop(para);

        if (CollectionUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);

    }

    /**
     * @api {POST} /api/v2/academic/course/electiveTopCoursenoList
     * @apiName electiveTop
     * @apiGroup academic
     * @apiVersion 2.0.0
     * @apiDescription 学业特征-选修课排名--柱形图弹窗一，统计课程选课人数
     * @apiPermission course_electiveTop
     * @apiSampleRequest http://localhost:8080/api/v2/academic/course/electiveTop
     * @apiParam {String} [schoolCode] 校区code
     * @apiParam {String} [facultyCode] 院系code
     * @apiParam {String} [majorCode] 专业code
     * @apiParam {String} [classSelect] 班级code多选，用逗号隔开，班级只有code,没有名称
     * @apiParam {String} [semester]  学期：本学期、上学期 格式：2016-2017-1 ; 上学年 格式：2016-2017
     * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
     * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
     * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
     * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
     * @apiParamExample {json} 请求例子:
     * {
     * "schoolCode": "jkd"
     * }
     * @apiSuccess (200) {String} message 操作成功
     * @apiSuccess (200) {int} statusCode 200
     * @apiSuccessExample {json} 返回样例:
     * {"statusCode":200,
     * "message":"操作成功",
     * "data":[{"sum":1532,"course_name":"综合技能实训1"},{"num":429,"course_natures":"学科平台基础课程"}]
     * }
     */
    @RequestMapping("/course/electiveTopCoursenoList")
    @ResponseBody
    public CommResponse electiveTopCoursenoListAOP(AcademicParams para) {
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }

        List<Map<String, Object>> result = academicServer.electiveTopCoursenoList(para);

        if (CollectionUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(new PageResult(limitPage(result, para.getStart(), para.getLimit()), result.size()));

    }


    /**
     * @api {POST} /api/v2/academic/course/electiveTopCoursenoSemester
     * @apiName electiveTop
     * @apiGroup academic
     * @apiVersion 2.0.0
     * @apiDescription 学业特征-选修课排名--柱形图弹窗二，统计某门课程，每一学期变化情况
     * @apiPermission course_electiveTop
     * @apiSampleRequest http://localhost:8080/api/v2/academic/course/electiveTop
     * @apiParam {String} [schoolCode] 校区code
     * @apiParam {String} [facultyCode] 院系code
     * @apiParam {String} [majorCode] 专业code
     * @apiParam {String} [classSelect] 班级code多选，用逗号隔开，班级只有code,没有名称
     * @apiParam {String} [semester]  学期：本学期、上学期 格式：2016-2017-1 ; 上学年 格式：2016-2017
     * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
     * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
     * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
     * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
     * @apiParamExample {json} 请求例子:
     * {
     * "schoolCode": "jkd"
     * }
     * @apiSuccess (200) {String} message 操作成功
     * @apiSuccess (200) {int} statusCode 200
     * @apiSuccessExample {json} 返回样例:
     * {"statusCode":200,
     * "message":"操作成功",
     * "data":[{"sum":1532,"course_name":"综合技能实训1"},{"num":429,"course_natures":"学科平台基础课程"}]
     * }
     */
    @RequestMapping("/course/electiveTopCoursenoSemester")
    @ResponseBody
    public CommResponse electiveTopCoursenoSemesterAOP(AcademicParams para) {
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }

        List<Map<String, Object>> result = academicServer.electiveTopCoursenoSemester(para);

        if (CollectionUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }
        return CommResponse.success(result);


    }


    @RequestMapping("/course/electiveTopStudentList")
    @ResponseBody
    @ApiOperation(value = "选修课排名弹窗", notes = "选修课排名弹窗")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse electiveTopStudentListAOP(AcademicParams para) {
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }

        List<Map<String, Object>> result = academicServer.electiveTopStudentList(para);

        if (CollectionUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(new PageResult(limitPage(result, para.getStart(), para.getLimit()), result.size()));

    }


    @RequestMapping("/course/ranking")
    @ResponseBody
    @ApiOperation(value = "个人--绩点名次情况", notes = "绩点名次情况")
    @ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")
    public CommResponse getStudentRanking(AcademicParams para) {
        if (StringUtils.isNotBlank(para.getOutid())) {
            return CommResponse.success(academicServer.getStudentRanking(para));
        } else {
            return CommResponse.failure("need outid");
        }
    }

    @RequestMapping("/student/getScoreCredit")
    @ResponseBody
    @ApiOperation(value = "个人--课程成绩情况", notes = "课程成绩情况")
    @ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")
    public CommResponse getScoreCredit(AcademicParams paras) throws IOException {
        Map<String, Object> result = academicServer.getScoreCredit(paras);
        return CommResponse.success(result);
    }


    @RequestMapping("/course/countryGrade")
    @ResponseBody
    @ApiOperation(value = "群体--等级考试情况", notes = "学业特征-等级考试情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse countryGradeTestAOP(AcademicParams para) {
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }

        List<Map<String, Object>> result = academicServer.countryGrade(para);

        if (CollectionUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);

    }


    @RequestMapping("/course/countryGradeStudentList")
    @ResponseBody
    @ApiOperation(value = "等级考试弹窗", notes = "等级考试弹窗")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse countryGradeStudentListAOP(AcademicParams para) {
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }

        List<Map<String, Object>> result = academicServer.countryGradeStudentList(para);

        if (CollectionUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(new PageResult(limitPage(result, para.getStart(), para.getLimit()), result.size()));

    }

    /**
     * 等级考试--下拉框数据展示
     *
     * @param para
     * @return
     */
    @RequestMapping("/course/organ")
    @ResponseBody
    @ApiOperation(value = "群体--等级考试情况", notes = "获取等级考试课程名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse organAOP(AcademicParams para) {
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }

        List<Map<Object, Object>> result = academicServer.organ(para);

        if (CollectionUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }

    /**
     * 学业特征时间，返回给前端
     *
     * @param para
     * @return
     */
    @RequestMapping("/course/semesterTime")
    @ResponseBody
    public CommResponse semesterTimeAOP(AcademicParams para) {
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }

        List<Map<Object, Object>> result = academicServer.semesterTime(para);

        if (CollectionUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }


    @RequestMapping("/course/attendanceCard")
    @ResponseBody
    @ApiOperation(value = "群体--上课考勤打卡", notes = "上课考勤打卡-学生列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse attendanceCardAOP(AcademicParams para) {
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        String startTime = para.getStartTime();
        String endTime = para.getEndTime();
        if (!com.ziyun.utils.common.StringUtils.isNotBlank(startTime) || !com.ziyun.utils.common.StringUtils.isNotBlank(endTime)) {
            return CommResponse.failure();
        }
        para.setStartTime(startTime + " 00:00:00");
        para.setEndTime(endTime + " 23:59:59");
        //为了配合前端，前端传null,默认查询所有教室名
        if (com.ziyun.utils.common.StringUtils.isBlank(para.getClassName())) {
            para.setClassName(null);
        }
        List<Map<String, Object>> result = academicServer.attendanceCard(para);

        if (CollectionUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);

    }


    @RequestMapping("/course/attendanceCardSize")
    @ResponseBody
    @ApiOperation(value = "群体--上课考勤打卡", notes = "统计总条数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "startTime", paramType = "query", dataType = "String[]", required = false, value = " 开始时间"),
            @ApiImplicitParam(name = "endTime", paramType = "query", dataType = "String[]", required = false, value = " 结束时间"),
    })
    public CommResponse attendanceCardSizeAOP(AcademicParams para) {
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        String startTime = para.getStartTime();
        String endTime = para.getEndTime();
        if (!com.ziyun.utils.common.StringUtils.isNotBlank(startTime) || !com.ziyun.utils.common.StringUtils.isNotBlank(endTime)) {
            return CommResponse.failure();
        }
        para.setStartTime(startTime + " 00:00:00");
        para.setEndTime(endTime + " 23:59:59");
        if (com.ziyun.utils.common.StringUtils.isBlank(para.getClassName())) {
            para.setClassName(null);
        }
        int result = academicServer.attendanceCardSize(para);


        return CommResponse.success(result);

    }


    @RequestMapping("/course/classNums")
    @ResponseBody
    @ApiOperation(value = "群体--上课考勤打卡", notes = "获取教室名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse classNumsAOP(AcademicParams para) {
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        List<Map<String, Object>> list = academicServer.classNums(para);
        return CommResponse.success(list);

    }


    @RequestMapping("/course/personAttendanceCard")
    @ResponseBody
    @ApiOperation(value = "个人--上课考勤打卡列表", notes = "上课考勤打卡列表")
    @ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")
    public CommResponse personAttendanceCard(AcademicParams para) {
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        String startTime = para.getStartTime();
        String endTime = para.getEndTime();
        if (!com.ziyun.utils.common.StringUtils.isNotBlank(startTime) || !com.ziyun.utils.common.StringUtils.isNotBlank(endTime)) {
            return CommResponse.failure();
        }
        para.setStartTime(startTime + " 00:00:00");
        para.setEndTime(endTime + " 23:59:59");
        if (StringUtils.isBlank(para.getClassName())) {
            para.setClassName(null);
        }
        List<Map<String, Object>> result = academicServer.personAttendanceCard(para);

        if (CollectionUtils.isEmpty(result)) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);

    }

    @RequestMapping("/course/personAttendanceCardSize")
    @ResponseBody
    @ApiOperation(value = "个人--上课考勤打卡总长度", notes = "上课考勤打卡总长度")
    @ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")
    public CommResponse personAttendanceCardSizeAOP(AcademicParams para) {
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        String startTime = para.getStartTime();
        String endTime = para.getEndTime();
        if (!com.ziyun.utils.common.StringUtils.isNotBlank(startTime) || !com.ziyun.utils.common.StringUtils.isNotBlank(endTime)) {
            return CommResponse.failure();
        }
        para.setStartTime(startTime + " 00:00:00");
        para.setEndTime(endTime + " 23:59:59");
        if (com.ziyun.utils.common.StringUtils.isBlank(para.getClassName())) {
            para.setClassName(null);
        }
        int result = academicServer.personAttendanceCardSize(para);


        return CommResponse.success(result);

    }

    @ApiOperation(value = "获取学生列表", notes = "获取学生列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    @RequestMapping(value = "/pagestudent")
    @ResponseBody
    public CommResponse getPageStudentAOP(Params para) throws Exception {

        return studentServer.getPageStudent(para);
    }

    @ApiOperation(value = "获取学生列表总长度", notes = "获取学生列表总长度")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    @RequestMapping("/studentsCount")
    @ResponseBody
    public CommResponse studentsCount(Params params) {
        return studentServer.studentsCount(params);
    }

    @RequestMapping(value = "/common/getAcademicLabel")
    @ResponseBody
    @ApiOperation(value = "基本信息--学生列表总长度", notes = "学生列表总长度")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public Map<String, Object> getAcademicLabel(@RequestBody AcademicParams params) {
        Map<String, Object> academicLabel = academicServer.getAcademicLabel(params);
        return academicLabel;

    }


    @RequestMapping("/common/featuresAcademic")
    @ResponseBody
    @ApiOperation(value = "个人画像-学业特征", notes = "学业特征")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public Map<String, Object> featureAcademic(@RequestBody AcademicParams para) throws IOException {
        return academicServer.getAcademicLabel(para);
    }


    @RequestMapping("/course/attendanceCardStudentList")
    @ResponseBody
    @ApiOperation(value = "上课考勤打卡", notes = "对应的打卡明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse attendanceCardStudentListAOP(AcademicParams para) {
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        String startTime = para.getStartTime();
        String endTime = para.getEndTime();
        if (!com.ziyun.utils.common.StringUtils.isNotBlank(startTime) || !com.ziyun.utils.common.StringUtils.isNotBlank(endTime)) {
            return CommResponse.failure();
        }
        para.setStartTime(startTime + " 00:00:00");
        para.setEndTime(endTime + " 23:59:59");
        if (com.ziyun.utils.common.StringUtils.isBlank(para.getClassName())) {
            para.setClassName(null);
        }
        // 获取开始条数，每页显示记录数
        Integer start = null;
        Integer limit = null;
        if (com.ziyun.utils.common.StringUtils.isNotBlank(para.getStart() + "")) {
            start = para.getStart();
            //开始页数设置为null
            para.setStart(null);
            limit = para.getLimit();
            //limit设置为null
            para.setLimit(null);
        }
        List<Map<String, Object>> result = academicServer.personAttendanceCard(para);
        if (CollectionUtils.isEmpty(result)){
            return CommResponse.success(null);
        }
        return CommResponse.success(new PageResult(limitPage(result, start, limit), result.size()));

    }


    @RequestMapping("/course/compulsoryCoursesList")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "必修课挂科2-4门的学生")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse compulsoryCoursesListAOP(AcademicParams para) {
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        List<Map<String, Object>> list = academicServer.compulsoryCoursesList(para);
        return CommResponse.success(list);
    }


    @RequestMapping("/course/compulsoryCoursesCount")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "必修课挂科2-4门学生的长度")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse compulsoryCoursesCountAOP(AcademicParams para) {
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        int size = academicServer.compulsoryCoursesCount(para);
        return CommResponse.success(size);
    }


    /**
     * 学业预警 -- 该接口获取所有学生的班级排名
     *
     * @param para
     * @return
     */
    @RequestMapping("/course/getStudentClassRankin")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "该接口获取所有学生的班级排名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse getStudentClassRankingAOP(AcademicParams para) {
        try {
            academicServer.getStudentClassRankin(para);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommResponse.success();
    }

    /**
     * 学业预警 -- 获取班级排名下滑超过十名的学生列表
     *
     * @param para
     * @return
     */
    @RequestMapping("/course/getStudentClassRankingSlideLis")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "获取班级排名下滑超过十名的学生列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse getStudentClassRankingSlideListAOP(AcademicParams para) {

        List<Map<String, Object>> list = academicServer.getStudentClassRankingSlideList(para);
        return CommResponse.success(list);
    }

    /**
     * 学业预警 -- 获取班级排名下滑超过十名的学生列表总长度
     *
     * @param para
     * @return
     */
    @RequestMapping("/course/getStudentClassRankingSlideSize")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "获取班级排名下滑超过十名的学生列表总长度")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse getStudentClassRankingSlideSizeAOP(AcademicParams para) {

        int size = academicServer.getStudentClassRankingSlideSize(para);
        return CommResponse.success(size);
    }

    /**
     * 学业预警 -- 获取整学年所获得学分小于 18分的学生列表
     *
     * @param para
     * @return
     */
    @RequestMapping("/course/getStudentYearCreditList")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "获取整学年所获得学分小于 18分的学生列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse getStudentYearCreditListAOP(AcademicParams para) {
        //由于是统计一学年 包含两个学期
        if (para != null && para.getSemesterArr() != null && para.getSemesterArr().length > 0) {
            String[] semesterArr = {para.getSemesterArr()[0].substring(0, 9) + "-1", para.getSemesterArr()[0].substring(0, 9) + "-2"};
            para.setSemesterArr(semesterArr);
        }
        List<Map<String, Object>> list = academicServer.getStudentYearCreditList(para);
        return CommResponse.success(list);
    }

    /**
     * 学业预警 -- 获取整学年所获得学分小于 18分的学生列表总长度
     *
     * @param para
     * @return
     */
    @RequestMapping("/course/getStudentYearCreditSize")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "获取整学年所获得学分小于 18分的学生列表总长度")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse getStudentYearCreditSizeAOP(AcademicParams para) {
        //由于是统计一学年 包含两个学期
        if (para != null && com.ziyun.utils.common.StringUtils.isNotBlank(para.getSemester())) {
            String[] semesterArr = {para.getSemester().substring(0, 9) + "-1", para.getSemester().substring(0, 9) + "-2"};
            para.setSemesterArr(semesterArr);
        }
        int size = academicServer.getStudentYearCreditSize(para);
        return CommResponse.success(size);
    }

    /**
     * 学业预警 -- 统计受过处分的学生列表（处分类型：严重警告","警告处分"除外）
     *
     * @param para
     * @return
     */
    @RequestMapping("/course/getStudentPulishList")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "统计受过处分的学生列表（处分类型：严重警告\",\"警告处分\"除外）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse getStudentPublishListAOP(AcademicParams para) {
        List<Map<String, Object>> list = academicServer.getStudentPulishList(para);
        return CommResponse.success(list);
    }

    /**
     * 学业预警 -- 统计受过处分的学生列表总长度（处分类型：严重警告","警告处分"除外）
     *
     * @param para
     * @return
     */
    @RequestMapping("/course/getStudentPulishSize")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "统计受过处分的学生列表总长度（处分类型：严重警告\",\"警告处分\"除外）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse getStudentPulishSizeAOP(AcademicParams para) {
        int size = academicServer.getStudentPulishSize(para);
        return CommResponse.success(size);
    }

    /**
     * 学业预警 -- 获取指定学期英语成绩小于390分的学生列表
     *
     * @param para
     * @return
     */
    @RequestMapping("/course/getStudentEnglishScoreList")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "获取指定学期英语成绩小于390分的学生列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse getStudentEnglishScoreListAOP(AcademicParams para) {
        List<Map<String, Object>> list = academicServer.getStudentEnglishScoreList(para);
        return CommResponse.success(list);
    }

    /**
     * 学业预警 -- 获取指定学期英语成绩小于390分的学生列表总长度
     *
     * @param para
     * @return
     */
    @RequestMapping("/course/getStudentEnglishScoreSize")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "获取指定学期英语成绩小于390分的学生列表总长度")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public CommResponse getStudentEnglishScoreSizeAOP(AcademicParams para) {
        int size = academicServer.getStudentEnglishScoreSize(para);
        return CommResponse.success(size);
    }

}
