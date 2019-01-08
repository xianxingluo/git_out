package com.ziyun.academic.controller;

import com.ziyun.academic.server.IEarlyWarningServer;
import com.ziyun.academic.vo.AcademicParams;
import com.ziyun.utils.requests.CommResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 学业预警Controller
 */
@Controller
@RequestMapping("/v2/academic/warning")
@Api(tags = "预警", description = "学业预警微服务相关api")
public class AcademicEarlyWarnController {

    @Autowired
    private IEarlyWarningServer earlyWarningServer;


    @RequestMapping("/academicStudent")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "查询符合预警规则的学生", httpMethod = "POST")
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
    public CommResponse listAcademicWarningStudentsAOP(AcademicParams params) throws Exception {

        if (StringUtils.isBlank(params.getWarnRuleId())) {
            return CommResponse.failure("预警规则id不能为空");
        }

        List<Map<String, Object>> result = earlyWarningServer.listAcademicWarningStudents(params);

        if (null == result || result.size() == 0) {
            return CommResponse.success(new String[0]);
        }

        return CommResponse.success(result);
    }


    @RequestMapping("/student")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "查询符合综合预警规则的学生", httpMethod = "POST")
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
    public CommResponse listWarningStudentsAOP(AcademicParams params) throws Exception {

        if (StringUtils.isBlank(params.getWarnRuleId())) {
            return CommResponse.failure("预警规则id不能为空");
        }

        List<Map<String, Object>> result = earlyWarningServer.listWarningStudents(params);

        if (null == result || result.size() == 0) {

            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }


    @RequestMapping("/student/academicCount")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "查询符合学业预警规则的学生长度", httpMethod = "POST")
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
    public CommResponse countAcademicWarningAOP(AcademicParams params) throws Exception {
        if (StringUtils.isBlank(params.getWarnRuleId())) {
            return CommResponse.failure("预警规则id不能为空");
        }
        return CommResponse.success(earlyWarningServer.countAcademicWarningStudents(params));
    }


    @RequestMapping("/student/count")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "查询符合综合预警规则的学生长度", httpMethod = "POST")
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
    public CommResponse countWarningAOP(AcademicParams params) throws Exception {
        if (StringUtils.isBlank(params.getWarnRuleId())) {
            return CommResponse.failure("预警规则id不能为空");
        }

        return CommResponse.success(earlyWarningServer.countWarningStudents(params));
    }

    @RequestMapping("/detail")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "预警中给前端的展示内容", httpMethod = "POST")
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
    public CommResponse getWarnDetail(String token) {
        //返回操作成功的状态
        return CommResponse.success(earlyWarningServer.getEarlyWarnParamShow(token));
    }


    @RequestMapping("/academicRule")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "获得当前用户的学业预警规则", httpMethod = "POST")
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
    public CommResponse getAcademicWarnRule(@RequestParam Map<String, Object> map) {
        if (null == map.get("start")) {
            return CommResponse.success(null);
        } else {
            Map resultMap = new HashedMap();
            if (!map.containsKey("warnFlag")) {
                map.put("warnFlag", 2);
            }
            List<Map> ruleList = earlyWarningServer.getAcademicEarlyWarnRules(map);
            resultMap.put("data", limitPage(ruleList, Integer.parseInt(String.valueOf(map.get("start"))), 10));
            resultMap.put("total", ruleList.size());
            return CommResponse.success(resultMap);
        }
    }


    @RequestMapping("/rule")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "获得当前用户的综合预警规则", httpMethod = "POST")
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
    public CommResponse getWarnRule(@RequestParam Map<String, Object> map) {
        if (null == map.get("start")) {
            return CommResponse.success(null);
        } else {
            Map resultMap = new HashedMap();
            List<Map> ruleList = earlyWarningServer.getEarlyWarnRules(map);
            resultMap.put("data", limitPage(ruleList, Integer.parseInt(String.valueOf(map.get("start"))), 10));
            resultMap.put("total", ruleList.size());
            return CommResponse.success(resultMap);
        }
    }


    @RequestMapping(value = "/academicAddition")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "添加学业预警", httpMethod = "POST")
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
    public CommResponse addAcademicWarnRule(@RequestParam Map<String, Object> map) {
        return earlyWarningServer.addAcademicWarnRule(map);
    }


    @RequestMapping(value = "/addition")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "添加综合预警", httpMethod = "POST")
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
    public CommResponse addWarnRule(@RequestParam Map<String, Object> map) {
        return earlyWarningServer.addWarnRule(map);
    }


    @RequestMapping("/academicCancel")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "删除学业预警", httpMethod = "POST")
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
    public CommResponse delAcademicWarnRules(@RequestParam Map<String, Object> map) {
        return earlyWarningServer.delAcademicWarnRules(map);
    }


    @RequestMapping("/cancel")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "删除综合预警", httpMethod = "POST")
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
    public CommResponse delWarnRules(@RequestParam Map<String, Object> map) {
        return earlyWarningServer.delWarnRules(map);
    }

    @RequestMapping("/academicModification")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "修改学业预警", httpMethod = "POST")
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
    public CommResponse updateAcademicWarnRules(@RequestParam Map<String, Object> map) {
        earlyWarningServer.updateAcademicWarnRules(map);
        return CommResponse.success();
    }


    @RequestMapping("/modification")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "修改综合预警", httpMethod = "POST")
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
    public CommResponse updateWarnRules(@RequestParam Map<String, Object> map) {
        earlyWarningServer.updateWarnRules(map);
        return CommResponse.success();
    }

    //手动分页的方法
    private List<Map> limitPage(List<Map> list, int start, int length) {
        int end;
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

    @RequestMapping("/earlyNameIsExist")
    @ResponseBody
    public CommResponse earlyNameIsExist(@RequestParam Map map) {
        String earlyName = earlyWarningServer.earlyNameIsExist(map);
        if (StringUtils.isBlank(earlyName)) {
            return CommResponse.success(true);
        } else {
            return CommResponse.success(false);
        }

    }

    /**
     * 获取学期的开始，结束时间
     *
     * @param map
     * @return
     */
    @RequestMapping("/termTime")
    @ResponseBody
    public CommResponse getTermTime(@RequestParam Map map) {
        List<Map<String, Object>> list = earlyWarningServer.getTermTime(map);
        if (CollectionUtils.isEmpty(list)) {
            return CommResponse.success(new String[]{});
        }
        return CommResponse.success(list);
    }

    /**
     * 常规预警，疑是离校学生列表
     *
     * @param params
     * @return
     */
    @RequestMapping("/doubtfulLevelSchoolList")
    @ResponseBody
    public CommResponse doubtfulLevelSchoolListAOP(AcademicParams params) {
        List<Map<String, Object>> list = earlyWarningServer.doubtfulLevelSchoolList(params);
        if (CollectionUtils.isEmpty(list)) {
            return CommResponse.success(new String[]{});
        }
        return CommResponse.success(list);
    }

    /**
     * 常规预警，疑是离校学生列表长度
     *
     * @param params
     * @return
     */
    @RequestMapping("/doubtfulLevelSchoolSize")
    @ResponseBody
    public CommResponse doubtfulLevelSchoolSizeAOP(AcademicParams params) {
        int size = earlyWarningServer.doubtfulLevelSchoolSize(params);
        return CommResponse.success(size);
    }

    /**
     * 常规预警，消费激增人员
     *
     * @param params
     * @return
     */
    @RequestMapping("/consumeIncrease")
    @ResponseBody
    public CommResponse getConsumeIncreaseAOP(AcademicParams params) {
        List<Map<String, Object>> list = earlyWarningServer.getConsumeIncrease(params);
        if (CollectionUtils.isEmpty(list)) {
            return CommResponse.success(new String[]{});
        }
        return CommResponse.success(list);
    }

    /**
     * 常规预警，消费激增人员总数
     *
     * @param params
     * @return
     */
    @RequestMapping("/consumeIncreaseSize")
    @ResponseBody
    public CommResponse getConsumeIncreaseSizeAOP(AcademicParams params) {
        int size = earlyWarningServer.getConsumeIncreaseSize(params);
        return CommResponse.success(size);
    }

    /**
     * 常规预警，上网激增
     *
     * @param params
     * @return
     */
    @RequestMapping("/radacctIncrease")
    @ResponseBody
    public CommResponse getRadacctIncreaseAOP(AcademicParams params) {
        List<Map<String, Object>> list = earlyWarningServer.getRadacctIncrease(params);
        if (CollectionUtils.isEmpty(list)) {
            return CommResponse.success(new String[]{});
        }
        return CommResponse.success(list);
    }

    /**
     * 常规预警，上网激增人员总数
     *
     * @param params
     * @return
     */
    @RequestMapping("/radacctIncreaseSize")
    @ResponseBody
    public CommResponse getRadacctIncreaseSizeAOP(AcademicParams params) {
        int size = earlyWarningServer.getRadacctIncreaseSize(params);
        return CommResponse.success(size);
    }

    @RequestMapping("/earlyList")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "获取预警列表---v2.0", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "start", dataType = "Integer", value = "分页参数：从0开始"),
            @ApiImplicitParam(paramType = "query", name = "limit", dataType = "Integer", value = "分页参数：每页多少条数据"),
            @ApiImplicitParam(paramType = "query", name = "userId", dataType = "Integer", value = "属于哪个用户创建的预警"),
            @ApiImplicitParam(paramType = "query", name = "earlyType", dataType = "Integer", value = "预警类型：1 --预设预警，2--智能预警，3--我的预警")
    })
    public CommResponse getEarlyList(AcademicParams params) {
        List<Map<String, String>> list = earlyWarningServer.getEarlyList(params);
        if (CollectionUtils.isEmpty(list)) {
            return CommResponse.success(new String[]{});
        }
        return CommResponse.success(list);
    }

    @RequestMapping("/earlyListCount")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "获取预警列表总长度---v2.0", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "start", dataType = "Integer", value = "分页参数：从0开始"),
            @ApiImplicitParam(paramType = "query", name = "limit", dataType = "Integer", value = "分页参数：每页多少条数据"),
            @ApiImplicitParam(paramType = "query", name = "userId", dataType = "Integer", value = "属于哪个用户创建的预警"),
            @ApiImplicitParam(paramType = "query", name = "earlyType", dataType = "Integer", value = "预警类型：1 --预设预警，2--智能预警，3--我的预警")
    })
    public CommResponse getEarlyListCount(AcademicParams params) {
        int size = earlyWarningServer.getEarlyListCount(params);
        return CommResponse.success(size);
    }



    @RequestMapping("/editPage")
    @ResponseBody
    @ApiOperation(value = "学业预警", notes = "获取预警参数，跳转到预警编辑页面", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "Integer", value = "预警id：根据预警id查询预警参数列表")
    })
    public CommResponse toEditPage(AcademicParams params) {
        List<Map<String, Object>> list = earlyWarningServer.toEditPage(params);
        if (CollectionUtils.isEmpty(list)) {
            return CommResponse.success(new String[]{});
        }
        return CommResponse.success(list);
    }


    @RequestMapping("/saveCustomEarly")
    @ResponseBody
    @ApiOperation(value = "预设预警", notes = "将预预警保存我的预警", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", dataType = "Integer", value = "用户id"),
            @ApiImplicitParam(paramType = "query", name = "warnName", dataType = "String", value = "预警名称"),
            @ApiImplicitParam(paramType = "query", name = "warnDesc", dataType = "String", value = "预警描述"),
            @ApiImplicitParam(paramType = "query", name = "paramList[0].warnDesc", dataType = "List", value = "预警参数描述"),
            @ApiImplicitParam(paramType = "query", name = "paramList[0].warnParam", dataType = "List", value = "预警参数"),
            @ApiImplicitParam(paramType = "query", name = "paramList[0].minNum", dataType = "List", value = "参数最小值"),
            @ApiImplicitParam(paramType = "query", name = "paramList[0].maxNum", dataType = "List", value = "参数最大值")
    })
    public CommResponse saveCustomEarly(AcademicParams params) {
        int i = earlyWarningServer.saveCustomEarly(params);
        return CommResponse.success(i == 1 ? true : false);
    }


    @RequestMapping("/editEnableStatus")
    @ResponseBody
    @ApiOperation(value = "预警", notes = "编辑预警状态", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", dataType = "Integer[]", value = "预警id集合"),
            @ApiImplicitParam(paramType = "query", name = "flag", dataType = "Integer", value = "预警的状态")

    })
    public CommResponse editEnableStatus(AcademicParams params) {
        int i = earlyWarningServer.editEnableStatus(params);
        return CommResponse.success(i == 1 ? true : false);
    }


    @RequestMapping("/targetStudent")
    @ResponseBody
    @ApiOperation(value = "预警", notes = "查询符合预警条件的学生列表", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code: slg(苏理工)|jkd(江科大)"),
            @ApiImplicitParam(paramType = "query", name = "facultyCode", dataType = "String", value = "院系code"),
            @ApiImplicitParam(paramType = "query", name = "majorCode", dataType = "String", value = "专业code"),
            @ApiImplicitParam(paramType = "query", name = "classSelect", dataType = "String", value = "班级code多选，用逗号隔开，班级只有code,没有名称"),
            @ApiImplicitParam(paramType = "query", name = "semesterArr", dataType = "String[]", value = "学期可以多选"),
            @ApiImplicitParam(paramType = "query", name = "start", dataType = "Integer", value = "分页参数：从0开始"),
            @ApiImplicitParam(paramType = "query", name = "limit", dataType = "Integer", value = "分页参数：每页多少条数据"),
            @ApiImplicitParam(paramType = "query", name = "warnName", dataType = "String", value = "学生列表模糊查询字段")

    })
    public CommResponse getTargetStudentAOP(AcademicParams params) {
        List<Map<String, Object>> list = earlyWarningServer.getTargetStudent(params);
        if (CollectionUtils.isEmpty(list)) {
            return CommResponse.success(new String[]{});
        }
        return CommResponse.success(list);
    }

    @RequestMapping("/targetStudentCount")
    @ResponseBody
    @ApiOperation(value = "预警", notes = "查询符合预警条件的学生列表总长度", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code: slg(苏理工)|jkd(江科大)"),
            @ApiImplicitParam(paramType = "query", name = "facultyCode", dataType = "String", value = "院系code"),
            @ApiImplicitParam(paramType = "query", name = "majorCode", dataType = "String", value = "专业code"),
            @ApiImplicitParam(paramType = "query", name = "classSelect", dataType = "String", value = "班级code多选，用逗号隔开，班级只有code,没有名称"),
            @ApiImplicitParam(paramType = "query", name = "semesterArr", dataType = "String[]", value = "学期可以多选"),
            @ApiImplicitParam(paramType = "query", name = "start", dataType = "Integer", value = "分页参数：从0开始"),
            @ApiImplicitParam(paramType = "query", name = "limit", dataType = "Integer", value = "分页参数：每页多少条数据"),
            @ApiImplicitParam(paramType = "query", name = "warnName", dataType = "String", value = "学生列表模糊查询字段")

    })
    public CommResponse getTargetStudentCountAOP(AcademicParams params) {
        long size = earlyWarningServer.targetStudentCount(params);
        return CommResponse.success(size);
    }


    @RequestMapping("/deleteEarlyWarn")
    @ResponseBody
    @ApiOperation(value = "预警", notes = "删除我的预警：支持批量删除", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", dataType = "String[]", value = "预警id"),
    })
    public CommResponse deleteEarly(AcademicParams params) {
        int size = earlyWarningServer.deleteEarlyWarn(params);
        return CommResponse.success(size == 0 ? false : true);
    }

    @RequestMapping("/earlyLevelNum")
    @ResponseBody
    @ApiOperation(value = "预警", notes = "获取预警级别分布", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "earlyType", dataType = "String", value = "预警类型"),
    })
    public CommResponse getEarlyLevelNum(AcademicParams params) {
        List<Map<String, Object>> list = earlyWarningServer.getEarlyLevelNum(params);
        if (CollectionUtils.isEmpty(list)) {
            return CommResponse.success(new Object[]{});
        }
        return CommResponse.success(list);
    }

}
