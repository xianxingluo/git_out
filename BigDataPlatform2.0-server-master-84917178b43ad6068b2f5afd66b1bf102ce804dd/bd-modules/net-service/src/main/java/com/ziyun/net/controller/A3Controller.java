package com.ziyun.net.controller;

import com.ziyun.net.server.IA3RadacctTimeServer;
import com.ziyun.net.vo.NetParams;
import com.ziyun.net.vo.Params;
import com.ziyun.utils.requests.CommResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 *
 * @Description:
 * @Created by liquan
 * @date 2017年5月13日 上午10:15:34
 *
 */
@Api(tags = "上网模块", description = "上网模块api")
@Controller
@RequestMapping("/v2/a3")
public class A3Controller {

    private static Logger logger = Logger.getLogger(A3Controller.class);

    @Autowired
    public IA3RadacctTimeServer radacctTimeServer;

    @ApiOperation(value = "上网行为分析", notes = "8、上网内容类型{修改为按照人数：统计具体应用}:top10显示", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", required = true, value = "学号id"),
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
    @RequestMapping(value = "/preferencelist")
    @ResponseBody
    public List<Map<String, Object>> preferenceListAOP(Params para)
            throws Exception {

        return radacctTimeServer.preferenceList(para);
    }

    @ApiOperation(value = "上网行为分析", notes = "8、上网内容类型{修改为按照人数：统计学校关注的几个分类：例如游戏、炒股等}", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", required = true, value = "学号id"),
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
    @RequestMapping(value = "/preferencetypelist")
    @ResponseBody
    public List<Map<String, Object>> preferenceTypeListAOP(Params para)
            throws Exception {

        return radacctTimeServer.preferenceTypeList(para);
    }

    /*************************************Add By Linxiaojun***********************************************/
    /*************************************个人***********************************************/
    @ApiOperation(value = "上网总时长分布: 个人（图表）", notes = "上网总时长分布: 个人（图表）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", required = true, value = "学号id"),
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
    @RequestMapping(value = "/durationDistChartPersonal")
    @ResponseBody
    public CommResponse durationDistChartPersonal(NetParams para) throws Exception {
        return getCommResponse4ChartPersonal(para, 1);
    }

    @ApiOperation(value = "上网时段: 个人（图）", notes = "上网时段: 个人（图）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", required = true, value = "学号id"),
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
    @RequestMapping(value = "/periodChartPersonal")
    @ResponseBody
    public CommResponse periodChartPersonal(NetParams para) throws Exception {
        return getCommResponse4Chart(para, 4);
    }

    /*************************************社群***********************************************/

    @ApiOperation(value = "上网总时长分布（图表）", notes = "上网总时长分布（图表）", httpMethod = "POST")
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
    @RequestMapping(value = "/durationDistChart")
    @ResponseBody
    public CommResponse durationDistChartAOP(NetParams para) throws Exception {
        return getCommResponse4Chart(para, 1);
    }


    @ApiOperation(value = "上网时长排名（表格）", notes = "上网时长排名（表格）", httpMethod = "POST")
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
            @ApiImplicitParam(paramType = "query", name = "sort", dataType = "String", value = "排序字段名：'OUTID','OPDT'"),
            @ApiImplicitParam(paramType = "query", name = "order", dataType = "String", value = "排序类型：0:升序;1:降序"),
            @ApiImplicitParam(paramType = "query", name = "sort", dataType = "String", value = "排序字段名：'OUTID','OPDT'"),
            @ApiImplicitParam(paramType = "query", name = "order", dataType = "String", value = "排序类型：0:升序;1:降序")
    })
    @RequestMapping(value = "/durationTopTable")
    @ResponseBody
    public CommResponse durationTopTableAOP(NetParams para) throws Exception {
        return getCommResponse4Table(para, 3);
    }

    @ApiOperation(value = "上网时长TOP10（总记录数）", notes = "上网时长TOP10（总记录数）", httpMethod = "POST")
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
    @RequestMapping(value = "/durationTopCount")
    @ResponseBody
    public CommResponse durationTopCountAOP(NetParams para) throws Exception {
        return getCommResponse4Count(para, 3);
    }

    @ApiOperation(value = "上网时段（图表）", notes = "上网时段（图表）", httpMethod = "POST")
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
    @RequestMapping(value = "/periodChart")
    @ResponseBody
    public CommResponse periodChartAOP(NetParams para) throws Exception {
        para.setPeriodFlag(1);
        return getCommResponse4Chart(para, 4);
    }


    @ApiOperation(value = "上网访问内容（表格）", notes = "上网访问内容（表格）", httpMethod = "POST")
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
    @RequestMapping(value = "/visitContextTable")
    @ResponseBody
    public CommResponse visitContextTableTopAOP(NetParams para) throws Exception {
        return getCommResponse4Table(para, 6);
    }

    @ApiOperation(value = "上网访问内容（总记录数）", notes = "上网访问内容（总记录数）", httpMethod = "POST")
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
    @RequestMapping(value = "/visitContextCount")
    @ResponseBody
    public CommResponse visitContextCountAOP(NetParams para) throws Exception {
        return getCommResponse4Count(para, 6);
    }


    @ApiOperation(value = "上网内容热度: Chart [个人,社群特征]", notes = "上网内容热度: Chart [个人,社群特征]", httpMethod = "POST")
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
    @RequestMapping(value = "/contentHeatChart")
    @ResponseBody
    public CommResponse contentHeatChartAOP(NetParams para) throws Exception {
        return getCommResponse4Chart(para, 9);
    }


    @ApiOperation(value = "上网流量: Chart [个人,社群特征]", notes = "上网流量: Chart [个人,社群特征]", httpMethod = "POST")
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
    @RequestMapping(value = "/fluxChart")
    @ResponseBody
    public CommResponse fluxChartAOP(NetParams para) throws Exception {
        return getCommResponse4Chart(para, 10);
    }

    @RequestMapping(value = "/fluxChartMonitor")
    @ResponseBody
    public CommResponse fluxChartMonitorAOP(NetParams para) throws Exception {
        return getCommResponse4Chart(para, 12);
    }

    @ApiOperation(value = "个人画像", notes = "上网特征", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", required = true, value = "学号id"),
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

    @RequestMapping(value = "/netOverviewPersonal")
    @ResponseBody
    public CommResponse netOverviewPersonal(NetParams para) throws Exception {
        return getCommResponse4Chart(para, 11);
    }

    /**
     * 图表公共方法： 处理Service返回结果: 群体
     *
     * @param para
     * @param flag
     * @return
     */
    private CommResponse getCommResponse4Chart(NetParams para, int flag) {
        CommResponse response = null;
        Object obj = null;
        try {
            switch (flag) {
                case 1:
                    obj = radacctTimeServer.getDurationDistChart(para);
                    break;
                case 2:
                    obj = radacctTimeServer.getCrowdAnalysisChart(para);
                    break;
                case 3:
                    obj = radacctTimeServer.getDurationTopChart(para);
                    break;
                case 4:
                    obj = radacctTimeServer.getPeriodChart(para);
                    break;
                case 5:
                    obj = radacctTimeServer.getTerminalTypeChart(para);
                    break;
                case 6:
                    obj = radacctTimeServer.getVisitContextTopChart(para);
                    break;
                case 7:
                    obj = radacctTimeServer.getCommunityOverview(para);
                    break;
                case 8:
                    obj = radacctTimeServer.getContentHeatServAppType(para);
                    break;
                case 9:
                    obj = radacctTimeServer.getContentHeatChart(para);
                    break;
                case 10:
                    obj = radacctTimeServer.getFluxChart(para);
                    break;
                case 11:
                    obj = radacctTimeServer.getNetOverviewPersonal(para);
                    break;
                case 12:
                    obj = radacctTimeServer.getFluxChartMonitor(para);
                    break;
                case 14:
                    obj = radacctTimeServer.getNetStudentList(para);
                    break;
            }
            response = CommResponse.success(obj);
        } catch (Exception ex) {
            logger.error(ex);
            response = CommResponse.failure(ex);
        } finally {
            return response;
        }
    }

    /**
     * 表格公共方法： 处理Service返回结果
     * @param para
     * @param flag
     * @return
     */
    private CommResponse getCommResponse4Table(NetParams para, int flag) {
        CommResponse response = null;
        Object obj = null;
        try {
            switch (flag) {
                case 1:
                    obj = radacctTimeServer.getDurationDistTable(para);
                    break;
                case 2:
                    obj = radacctTimeServer.getCrowdAnalysisTable(para);
                    break;
                case 3:
                    obj = radacctTimeServer.getDurationTopTable(para);
                    break;
                case 4:
                    obj = radacctTimeServer.getPeriodTable(para);
                    break;
                case 5:
                    obj = radacctTimeServer.getTerminalTypeTable(para);
                    break;
                case 6:
                    obj = radacctTimeServer.getVisitContextTopTable(para);
                    break;
                case 7:
                    obj = radacctTimeServer.getContentHeatTable(para);
                    break;
                case 8:
                    obj = radacctTimeServer.getFluxTable(para);
                    break;
                case 9:
                    obj = radacctTimeServer.getStudentListByApp(para);
                    break;
            }
            response = CommResponse.success(obj);
        } catch (Exception ex) {
            logger.error(ex);
            response = CommResponse.failure(ex);
        } finally {
            return response;
        }
    }

    /**
     * 总条数公共方法： 处理Service返回结果
     *
     * @param para
     * @param flag
     * @return
     */
    private CommResponse getCommResponse4Count(NetParams para, int flag) {
        CommResponse response = null;
        Object obj = null;
        try {
            switch (flag) {
                case 1:
                    obj = radacctTimeServer.getDurationDistRecordNum(para);
                    break;
                case 2:
                    obj = radacctTimeServer.getCrowdAnalysisRecordNum(para);
                    break;
                case 3:
                    obj = radacctTimeServer.getDurationTopRecordNum(para);
                    break;
                case 6:
                    obj = radacctTimeServer.getVisitContextTopRecordNum(para);
                    break;
                case 8:
                    obj = radacctTimeServer.getTotalPeople(para);
                    break;
                case 9:
                    obj = radacctTimeServer.getTotalPeopleMale(para);
                    break;
                case 10:
                    obj = radacctTimeServer.getTotalPeopleFemale(para);
                    break;
                case 11:
                    obj = radacctTimeServer.getContentHeatRecordNum(para);
                    break;
                case 12:
                    obj = radacctTimeServer.getFluxRecordNum(para);
                    break;
                case 13:
                    obj = radacctTimeServer.getAppStudentRecordNum(para);
                    break;

            }
            response = CommResponse.success(obj);
        } catch (Exception ex) {
            logger.error(ex);
            response = CommResponse.failure(ex);
        } finally {
            return response;
        }
    }

    /**
     * 图表公共方法： 处理Service返回结果: 个人
     *
     * @param para
     * @param flag
     * @return
     */
    private CommResponse getCommResponse4ChartPersonal(NetParams para, int flag) {
        CommResponse response = null;
        Object obj = null;
        try {
            switch (flag) {
                case 1:
                    obj = radacctTimeServer.getDurationDistChartPersonal(para);
                    break;
                default:
                    break;
            }
            response = CommResponse.success(obj);
        } catch (Exception ex) {
            logger.error(ex);
            response = CommResponse.failure(ex);
        } finally {
            return response;
        }
    }

    @ApiOperation(value = "上网模块：学生列表", notes = "上网模块：学生列表", httpMethod = "POST")
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
    @RequestMapping(value = "/getNetStudentList")
    @ResponseBody
    public CommResponse getNetStudentListAOP(NetParams para) throws Exception {
        return getCommResponse4Chart(para, 14);
    }

    @ApiOperation(value = "社群特征", notes = "上网特征", httpMethod = "POST")
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
    @RequestMapping(value = "/communityOverview")
    @ResponseBody
    public CommResponse communityOverviewAOP(NetParams para) throws Exception {
        return getCommResponse4Chart(para, 7);
    }

    /**
     * 常规预警 --游戏时长大于3小时的人员列表
     *
     * @param para
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getNetEarlywarnList")
    @ResponseBody
    public CommResponse getNetEarlywarnListAOP(NetParams para) throws Exception {
        List<Map<String, Object>> list = radacctTimeServer.getNetEarlywarnList(para);
        return CommResponse.success(list);

    }

    /**
     * 常规预警 --游戏时长大于3小时的人员列表总长度
     *
     * @param para
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getNetEarlywarnCount")
    @ResponseBody
    public CommResponse getNetEarlywarnCountAOP(NetParams para) throws Exception {
        int size = radacctTimeServer.getNetEarlywarnCount(para);
        return CommResponse.success(size);

    }
}
