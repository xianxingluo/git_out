package com.ziyun.dormitory.controller;

import com.ziyun.common.model.Params;
import com.ziyun.common.model.ParamsStatus;
import com.ziyun.dormitory.service.IEcardAccessInoutService;
import com.ziyun.utils.common.StringUtils;
import com.ziyun.utils.requests.CommResponse;
import io.swagger.annotations.*;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**

 * @Desc　宿舍出入、异常行为相关api

 * @Created by liufubiao on 2018/5/22.

 **/
@Api(tags = "宿舍出入、异常行为", description = "宿舍出入、异常行为相关api")
@Controller
@RequestMapping("/v2/behaviour")
public class BehaviourController {

	@Autowired
	public IEcardAccessInoutService accessInoutService;

	@RequestMapping("/toConsume")
	public ModelAndView toConsume() throws Exception {
		ModelAndView mv = new ModelAndView("behavior/comBehaviormng");
		return mv;
	}

	/**
	 * ｛行为综合分析｝5.1、本月作息占比:按照天汇总{在宿舍的时间}
	 * 
	 * @param para
	 * @return
	 * @throws Exception
	 */
    @ApiOperation(value = "行为综合分析", notes = "本月作息占比:按照天汇总{在宿舍的时间}", httpMethod = "GET")
    //@ApiImplicitParam(paramType = "query", name = "id", value = "权限id", required = false, dataType = "Long")
    @RequestMapping(value = "/mouthRestList")
    @ResponseBody
    public List mouthRestListAOP(Params para) throws Exception {
		List<Map<String, Object>> listCo = accessInoutService.mouthRestList(para);
		return listCo;
	}

	/**
	 * ｛社群学业分析：成绩提高分析｝4、作息规律统计
	 * 
	 * 采用了新的跑批后的表进行统计
	 * 
	 * @param para
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/restWeekHourList")
	@ResponseBody
	public Map restWeekHourListAOP(Params para) throws Exception {
		Params parain=new Params();
		BeanUtils.copyProperties(para, parain);
		Params paraout=new Params();
		BeanUtils.copyProperties(para, paraout);
		
		List<Map<String, Object>> listIn = accessInoutService
				.inWeekHourList(parain);
		List<Map<String, Object>> listOut = accessInoutService
				.outWeekHourList(paraout);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("into", listIn);
		map.put("out", listOut);
		return map;
	}

    /**
	 * 10.2.3、宿舍流量：按照天、小时统计：{进入宿舍人数（into）、离开宿舍人数（out）、在宿舍的人数（on）}
	 * 
	 * @param para
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dayhour")
	@ResponseBody
	public Map dayHourAOP(Params para) throws Exception {
		
		return accessInoutService.dayHour(para);
	}

	/**
	 * @api {POST} /api/v2/behaviour/lateness
	 * @apiName lateness
	 * @apiGroup behaviour
	 * @apiVersion 2.0.0
	 * @apiDescription 晚归情况
	 * @apiPermission stu_dorm_times
	 * @apiSampleRequest http://localhost:8080/api/v2/behaviour/lateness
	 * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
	 * @apiParam {String} [facultyCode] 院系code
	 * @apiParam {String} [majorCode] 专业code
	 * @apiParam {String} [classSelect] 班级code多选，用逗号隔开：：班级只有code,没有名称
	 * @apiParam {String} [bdate] 开始时间
	 * @apiParam {String} [edate] 结束时间
	 * @apiParam {String} [semester] 学期字段
	 * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
	 * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
	 * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
	 * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
	 * @apiParamExample {json} 请求例子:
	 * {
	 * 	"outid": "168111511122"
	 * 	"searchYear": "2017"
	 * }
	 * @apiSuccess (200) {String} message 操作成功
	 * @apiSuccess (200) {int} statusCode 200
	 * @apiSuccessExample {json} 返回样例:
	 * {"statusCode":200,
	 *  "message":"操作成功",
	 *  "data":{[{"late":"1","nolate":9780,"time":2017-11-10 06:00:00-2017-11-11 06:00:00}]}
	 * }
	 * late:晚归数量  nolate：没有晚归的数量 time:时间段
	 * @apiErrorExample {json} 错误返回:
	 * {
	 *  "statusCode": 400
	 *  "message": "学号不能为空"
	 * }
	 */
	@RequestMapping("/lateness")
	@ResponseBody
	public CommResponse getLateToBedroomAOP(Params para){
		if (StringUtils.isBlank(para.getBdate()))
			return CommResponse.failure();
		if (null != para.getEducation() && para.getEducation() == 2) {
			return CommResponse.success(null);
		}
		try {
			return CommResponse.success(accessInoutService.getLateToBedroom(para));
		} catch (Exception e) {
			e.printStackTrace();
			return CommResponse.failure();
		}
	}

	/**
     * 晚归情况列表
	 * @param para
	 * @return
	 */
    @ApiOperation(value = "晚归情况列表", notes = "晚归情况列表", httpMethod = "POST")
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
    @RequestMapping("/latenessDetail")
    @ResponseBody
    public CommResponse getLateDetailAOP(Params para){
		if (StringUtils.isBlank(para.getBdate()))
			return CommResponse.failure();
		if (null != para.getEducation() && para.getEducation() == 2) {
			return CommResponse.success(null);
		}
		try {
			return CommResponse.success(accessInoutService.getLateDetail(para));
		} catch (Exception e) {
			e.printStackTrace();
			return CommResponse.failure();
		}
	}

    /**
     * 晚归情况列表数量
     *
     * @param para
     * @return
     */
    @ApiOperation(value = "晚归情况列表数量", notes = "晚归情况列表数量", httpMethod = "POST")
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
    @RequestMapping("/latenessCount")
    @ResponseBody
    public CommResponse getLateDetailCountAOP(Params para){
        if (StringUtils.isBlank(para.getBdate()))
            return CommResponse.failure();
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        try {
            return CommResponse.success(accessInoutService.getLateDetailCount(para));
        } catch (Exception e) {
            e.printStackTrace();
            return CommResponse.failure();
        }
    }

	/**
	 * @api {POST} /api/v2/behaviour/latenessList
	 * @apiName latenessList
	 * @apiGroup behaviour
	 * @apiVersion 2.0.0
	 * @apiDescription 晚归情况
	 * @apiPermission stu_dorm_times
	 * @apiSampleRequest http://localhost:8080/api/v2/behaviour/latenessList
	 * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
	 * @apiParam {String} [facultyCode] 院系code
	 * @apiParam {String} [majorCode] 专业code
	 * @apiParam {String} [classSelect] 班级code多选，用逗号隔开：：班级只有code,没有名称
	 * @apiParam {String} [bdate] 开始时间
	 * @apiParam {String} [edate] 结束时间
	 * @apiParam {String} [semester] 学期字段
	 * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
	 * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
	 * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
	 * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
	 * @apiParamExample {json} 请求例子:
	 * {
	 * 	"outid": "168111511122"
	 * 	"searchYear": "2017"
	 * }
	 * @apiSuccess (200) {String} message 操作成功
	 * @apiSuccess (200) {int} statusCode 200
	 * @apiSuccessExample {json} 返回样例:
	 * {"statusCode":200,
	 *  "message":"操作成功",
	 *  "data":{[{"late":"1","nolate":9780,"time":2017-11-10 06:00:00-2017-11-11 06:00:00}]}
	 * }
	 * late:晚归数量  nolate：没有晚归的数量 time:时间段
	 * @apiErrorExample {json} 错误返回:
	 * {
	 *  "statusCode": 400
	 *  "message": "学号不能为空"
	 * }
	 */
	@RequestMapping("/latenessList")
	@ResponseBody
	public CommResponse getLateToBedroomListAOP(Params para){
		if (StringUtils.isBlank(para.getBdate()))
			return CommResponse.failure();
		try {
			return CommResponse.success(accessInoutService.getLateToBedroom(para));
		} catch (Exception e) {
			e.printStackTrace();
			return CommResponse.failure();
		}
	}
	/**
	 * @api {POST} /api/v2/behaviour/student/dorm/timesPerDay
	 * @apiName timesPerDay
	 * @apiGroup behaviour
	 * @apiVersion 2.0.0
	 * @apiDescription 个人画像-宿舍出入特征-出入情况
	 * @apiPermission stu_dorm_timesPerDay
	 * @apiSampleRequest http://localhost:3000/api/v2/behaviour/student/dorm/timesPerDay
	 * @apiParam {String} [outid] 学号
	 * @apiParam {String} [searchYear] 查询的年份
	 * @apiParamExample {json} 请求例子:
	 * {
	 * 	"outid": "168111511122"
	 * 	"searchYear": "2017"
	 * }
	 * @apiSuccess (200) {String} message 操作成功
	 * @apiSuccess (200) {int} statusCode 200
	 * @apiSuccessExample {json} 返回样例:
	 * {"statusCode":200,
	 *  "message":"操作成功",
	 *  "data":{[{"date":"2017-03-02","times":6},{"date":"2017-03-03","times":6}]}
	 * }
	 * @apiErrorExample {json} 错误返回:
	 * {
	 *  "statusCode": 400
	 *  "message": "学号不能为空"
	 * }
	 */
	@RequestMapping("/student/dorm/timesPerDay")
	@ResponseBody
	public CommResponse getStudentInOutDormTimes(Params params) throws Exception{

		if (StringUtils.isBlank(params.getOutid())) {
			return CommResponse.failure("学号不能为空");
		}

		List<Map<String, Object>> result = accessInoutService.getStudentInOutDormTimes(params);

		if (null == result || result.size() == 0) {
			return CommResponse.success(null);
		}

		return CommResponse.success(result);
	}

	/**
	 * @api {POST} /api/v2/behaviour/student/dorm/years
	 * @apiName years
	 * @apiGroup behaviour
	 * @apiVersion 2.0.0
	 * @apiDescription 个人画像-宿舍出入特征-出入情况 -年份
	 * @apiPermission stu_dorm_years
	 * @apiSampleRequest http://localhost:3000/api/v2/behaviour/student/dorm/years
	 * @apiParam {String} [outid] 学号
	 * @apiParamExample {json} 请求例子:
	 * {
	 * 	"outid": "168111511122"
	 * }
	 * @apiSuccess (200) {String} message 操作成功
	 * @apiSuccess (200) {int} statusCode 200
	 * @apiSuccessExample {json} 返回样例:
	 * {"statusCode":200,
	 *  "message":"操作成功",
	 *  "data":{["2017","2016"]}
	 * }
	 * @apiErrorExample {json} 错误返回:
	 * {
	 *  "statusCode": 400
	 *  "message": "学号不能为空"
	 * }
	 */
	@RequestMapping("/student/dormYears")
	@ResponseBody
	public CommResponse getStudentDormYears(Params params) throws Exception{

		if (StringUtils.isBlank(params.getOutid())) {

			return CommResponse.failure("学号不能为空");
		}

		List<String> result = accessInoutService.getStudentDormYears(params);
		if (null == result || result.size() == 0){

			return CommResponse.success(null);
		}

		return CommResponse.success(result);
	}

	/**
	 * @api {POST} /api/v2/behaviour/student/dorm/weekView
	 * @apiName weekView
	 * @apiGroup behaviour
	 * @apiVersion 2.0.0
	 * @apiDescription 个人画像-宿舍出入特征-周视图
	 * @apiPermission stu_dorm_weekView
	 * @apiSampleRequest http://localhost:3000/api/v2/behaviour/student/dorm/weekView
	 * @apiParam {String} [outid] 学号
	 * @apiParam {String} [bdate] 开始时间
	 * @apiParam {String} [edate] 结束时间
	 * @apiParam {String} [someCode] 进出类型：0-进  1-出  2-全部
	 * @apiParamExample {json} 请求例子:
	 * {
	 * 	"outid": "1142801107"
	 * 	"bdate": "2017-03-02"
	 * 	"edate": "2017-03-08"
	 * 	"someCode": 0
	 * }
	 * @apiSuccess (200) {String} message 操作成功
	 * @apiSuccess (200) {int} statusCode 200
	 * @apiSuccessExample {json} 返回样例:
	 * {"statusCode":200,
	 *  "message":"操作成功",
	 *  "data":[{"OpDT":"2017-03-02","num":3,"avg_num":3.0},{"OpDT":"2017-03-03","num":2,"avg_num":2.5}]
	 * }
	 * @apiErrorExample {json} 错误返回:
	 * {
	 *  "statusCode": 400
	 *  "message": "学号不能为空"
	 * }
	 */
	@RequestMapping("/student/dormWeekView")
	@ResponseBody
	public CommResponse listStudentInOutDormWeekView(Params params) throws Exception{

		if (StringUtils.isBlank(params.getOutid())) {
			return CommResponse.failure("学号不能为空");
		}

		List<Map<String, Object>> result = accessInoutService.listStudentInOutDormWeekView(params);

		if (null == result || result.size() == 0) {
			return CommResponse.success(null);
		}

		return CommResponse.success(result);
    }

    /**
     * 宿舍进出流量图
     * @param params
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "宿舍出入特征-宿舍进出流量图", notes = "宿舍出入特征-宿舍进出流量图", httpMethod = "POST")
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
    @RequestMapping("/dorm/inOutFlows")
    @ResponseBody
    public CommResponse listDormInOutFlowsAOP(Params params) throws Exception {
		if (null != params.getEducation() && params.getEducation() == 2) {
			return CommResponse.success(null);
		}
		List<Map<String, Object>> result = accessInoutService.listDormInOutFlows(params);

		if (null == result || result.size() == 0) {
			return CommResponse.success(null);
		}

		return CommResponse.success(result);
    }

    /**
     * 宿舍进出流量图 - 列表
     * @param params
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "宿舍出入特征-宿舍进出流量图-列表", notes = "宿舍出入特征-宿舍进出流量图-列表", httpMethod = "POST")
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
    @ResponseBody
    public CommResponse listDormInOutFlowsAllAOP(Params params) throws Exception {

		Map<String, Object> result = accessInoutService.listDormInOutFlowsAll(params);

		if (null == result || result.isEmpty()) {
			return CommResponse.success(null);
		}

		return CommResponse.success(result);
    }

    /**
     * 个人 - 24小时打卡情况
     * @param params
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "个人画像-宿舍出入特征-24小时打卡情况", notes = "个人画像-宿舍出入特征-24小时打卡情况", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", required = true, value = "学号"),
            @ApiImplicitParam(paramType = "query", name = "someCode", dataType = "String", value = "进出类型：0-进  1-出  2-全部"),
            @ApiImplicitParam(paramType = "query", name = "bdate", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "edate", dataType = "String", value = "结束时间")
    })
    @RequestMapping("/student/dormHoursView")
    @ResponseBody
    public CommResponse listStudentInOutDormHours(Params params) throws Exception{

		if (StringUtils.isBlank(params.getOutid())) {
			return CommResponse.failure("学号不能为空");
		}

		Integer[] result = accessInoutService.listStudentInOutDormHours(params);

		if (null == result || result.length == 0) {
			return CommResponse.success(null);
		}

		return CommResponse.success(result);
    }

    /**
     * 个人 - 24小时打卡情况
     * @param params
     * @return
     */
    @ApiOperation(value = "个人画像-宿舍出入特征-24小时打卡情况", notes = "个人画像-宿舍出入特征-24小时打卡情况", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", required = true, value = "学号"),
            @ApiImplicitParam(paramType = "query", name = "someCode", dataType = "String", value = "进出类型：0-进  1-出  2-全部")
    })
    @RequestMapping("/student/dormFeatures")
    @ResponseBody
    public CommResponse stuDormFeature(Params params){

		if (StringUtils.isBlank(params.getOutid())) {
			return CommResponse.failure("学号不能为空");
		}

		Map<String, Object> result = accessInoutService.listStuDormFeature(params);

		if (null == result || result.isEmpty()) {
			return CommResponse.success(null);
		}

		return CommResponse.success(result);
    }

    /**
     * 晚归次数百分比
     * @param params
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "晚归次数百分比", notes = "晚归次数百分比", httpMethod = "POST")
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
    @ResponseBody
    public CommResponse getLateRatioAOP(Params params)throws Exception{
        if (null != params.getEducation() && params.getEducation() == 2) {
            return CommResponse.success(null);
        }
        return CommResponse.success(accessInoutService.getLateRatio(params));
    }

    @ApiOperation(value = "晚归次数-学生列表", notes = "晚归次数-学生列表", httpMethod = "POST")
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
            @ApiImplicitParam(paramType = "query", name = "lateTime", dataType = "String", value = "晚归次数"),
    })
    @RequestMapping("/dorm/studentRatio")
    @ResponseBody
    public CommResponse getStudnetLateRatioAOP(ParamsStatus params)throws Exception{
        if (null != params.getEducation() && params.getEducation() == 2) {
            return CommResponse.success(null);
        }
        if (null != params.getStart()){
			// 晚归大于7次的处理
			if (params.getLateTime() != null && params.getLateTime().equals("&gt;7")) {
				params.setLateTime(params.getLateTime().replace("&gt;7", ">7"));
			}
			List<Map<String, Object>> totalStudentList = accessInoutService.getStudnetLateRatio(params);//测试环境kylin有问题，sql执行报错
			List<Map<String, Object>> studentList = limitPage(totalStudentList, params.getStart(), params.getLimit());
			Map map =new HashedMap();
			map.put("data",studentList);
			map.put("total",totalStudentList.size());
			return CommResponse.success(map);
        }else
            return CommResponse.success(null);
    }

    /**
     * 晚归百分比表
     * @param params
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "晚归百分比表", notes = "晚归百分比表", httpMethod = "POST")
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
    @RequestMapping("/dorm/ratioList")
    @ResponseBody
    public CommResponse getLateRatioListAOP(Params params)throws Exception{
		return CommResponse.success(accessInoutService.getLateRatio(params));
    }

    /**
     * 可能未归情况
     * @param params
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "可能未归情况", notes = "可能未归情况", httpMethod = "POST")
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
    @ResponseBody
    public CommResponse getMybeLateAOP(Params params)throws Exception{
		if (null != params.getEducation() && params.getEducation() == 2) {
			return CommResponse.success(null);
		}
		return CommResponse.success(accessInoutService.getMybeLate(params));
	}

	@ApiOperation(value = "社群特征", notes = "宿舍出入特征", httpMethod = "POST")
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
	@RequestMapping("/dorm/summarize")
	@ResponseBody
	public CommResponse getSummarize(Params params)throws Exception{
		if (null != params.getEducation() && params.getEducation() == 2) {
			return CommResponse.success(null);
		}
		return CommResponse.success(accessInoutService.dormSummarize(params));
	}
	/**
	 * @api {POST} /api/v2/behaviour/dorm/possibleList
	 * @apiName possibleList
	 * @apiGroup behaviour
	 * @apiVersion 2.0.0
	 * @apiDescription 可能晚归表
	 * @apiPermission dorm_in_out
	 * @apiSampleRequest http://localhost:8080/api/v2/behaviour/dorm/possibleList
	 * @apiParam {String} [schoolCode] 校区code
	 * @apiParam {String} [facultyCode] 院系code
	 * @apiParam {String} [majorCode] 专业code
	 * @apiParam {String} [classSelect] 班级code多选，用逗号隔开，班级只有code,没有名称
	 * @apiParam {String} [bdate] 开始时间
	 * @apiParam {String} [edate] 结束时间
	 * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
	 * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
	 * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
	 * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
	 * @apiParamExample {json} 请求例子:
	 * {
	 *     "schoolCode": "jkd"
	 * }
	 * @apiSuccess (200) {String} message 操作成功
	 * @apiSuccess (200) {int} statusCode 200
	 * @apiSuccessExample {json} 返回样例:
	 *{"statusCode":200,
	 * "message":"操作成功",
	 * "data":[{{NUM=200, DAYS=2017-04-01}]
	 * }
	 * NUM：数值 DAYS：时间
	 */
	@RequestMapping("/dorm/possibleList")
	@ResponseBody
	public CommResponse getMybeLateListAOP(Params params)throws Exception{
		return CommResponse.success(accessInoutService.getMybeLate(params));
	}

	/**
	 * @api {POST} /api/v2/behaviour/dorm/possiblesize
	 * @apiName possiblesize
	 * @apiGroup behaviour
	 * @apiVersion 2.0.0
	 * @apiDescription 可能晚归列表的长度
	 * @apiPermission dorm_in_out
	 * @apiSampleRequest http://localhost:8080/api/v2/behaviour/dorm/possiblesize
	 * @apiParam {String} [schoolCode] 校区code
	 * @apiParam {String} [facultyCode] 院系code
	 * @apiParam {String} [majorCode] 专业code
	 * @apiParam {String} [classSelect] 班级code多选，用逗号隔开，班级只有code,没有名称
	 * @apiParam {String} [bdate] 开始时间
	 * @apiParam {String} [edate] 结束时间
	 * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
	 * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
	 * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
	 * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
	 * @apiParamExample {json} 请求例子:
	 * {
	 *     "schoolCode": "jkd"
	 * }
	 * @apiSuccess (200) {String} message 操作成功
	 * @apiSuccess (200) {int} statusCode 200
	 * @apiSuccessExample {json} 返回样例:
	 *{"statusCode":200,
	 * "message":"操作成功",
	 * "data":100
	 * }
	 *
	 */
	@RequestMapping("/dorm/possibleSize")
	@ResponseBody
	public CommResponse getMybeLatesizeAOP(Params params)throws Exception{
		return CommResponse.success(accessInoutService.getMybeLatesize(params));
    }

    /**
     * 宿舍出入打卡记录明细
     * @param params
     * @return
     */
    @ApiOperation(value = "宿舍出入打卡记录", notes = "宿舍出入打卡记录", httpMethod = "POST")
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
    @ResponseBody
    public  CommResponse getClockRecordAOP(ParamsStatus params){
        return CommResponse.success(accessInoutService.getClockRecord(params));
    }

    /**
     * 宿舍出入打卡记录数量
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "宿舍出入打卡记录数量", notes = "宿舍出入打卡记录数量", httpMethod = "POST")
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
    @RequestMapping("/dorm/recordSize")
    @ResponseBody
    public  CommResponse getClockRecordSizeAOP(ParamsStatus params){
        return CommResponse.success(accessInoutService.getClockRecordLength(params));
    }

    @ApiOperation(value = "个人画像-宿舍出入特征-作息规律", notes = "个人画像-宿舍出入特征-作息规律", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", required = true, value = "学号"),
            @ApiImplicitParam(paramType = "query", name = "someCode", dataType = "String", value = "进出类型：0-进  1-出  2-全部"),
            @ApiImplicitParam(paramType = "query", name = "bdate", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "edate", dataType = "String", value = "结束时间")
    })
    @RequestMapping("/dorm/rest")
    @ResponseBody
    public  CommResponse getRestRegularAOP(ParamsStatus params){
        Map<String,Object> map = new HashedMap();
        params.setIoflag(0);
        map.put("in", accessInoutService.getRestRegular(params));
        params.setIoflag(1);
        map.put("out", accessInoutService.getRestRegular(params));
        return CommResponse.success(map);
    }

	//手动分页的方法
    private List<Map<String, Object>> limitPage(List<Map<String, Object>> list, int start, int length){
        int end;
        //start = start;
        //null值判断
        if (list == null)
            return  null;
        //如果start大于list的长度则从0开始
        if(list.size() < start)
            start = 0;
        //如果截取的长度大于list的size则为size
        end = start + length > list.size() ? list.size():start + length;
        return list.subList(start, end);
    }

    /**
     * 个人 - 晚归情况
     *
     * @param para
     * @return
     */
    @ApiOperation(value = "个人画像-宿舍出入特征-晚归情况", notes = "个人画像-宿舍出入特征-晚归情况", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", required = true, value = "学号"),
            @ApiImplicitParam(paramType = "query", name = "bdate", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "edate", dataType = "String", value = "结束时间"),
    })
    @RequestMapping("/dorm/personLater")
    @ResponseBody
    public CommResponse personLaterAOP(ParamsStatus para) {
        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        //如果用户选择了研究生，返回null
        if (para.getEducation() != null && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        if (para.getStart() == null) {
            para.setStart(0);
            para.setLimit(10);
        }

        List<Map<String, Object>> result = accessInoutService.personLater(para);

        if (null == result || result.size() == 0) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);
    }

    /**
     * 个人 - 晚归情况记录数量
     *
     * @param para
     * @return
     */
    @ApiOperation(value = "个人画像-宿舍出入特征-晚归情况数量", notes = "个人画像-宿舍出入特征-晚归数量", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", required = true, value = "学号"),
            @ApiImplicitParam(paramType = "query", name = "bdate", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "edate", dataType = "String", value = "结束时间"),
    })
    @RequestMapping("/dorm/personLaterSize")
    @ResponseBody
    public CommResponse personLaterCountAOP(ParamsStatus para) {
        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        para.getStartDate();
        para.getEndDate();
        //如果用户选择了研究生，返回null
        if (para.getEducation() != null && para.getEducation() == 2) {
            return CommResponse.success(null);
        }

        Long result = accessInoutService.personLaterSize(para);

        return CommResponse.success(result);
    }

    /**
     * 个人 - 可能未归情况
     *
     * @param para
     * @return
     */
    @ApiOperation(value = "个人画像-宿舍出入特征-可能未归情况", notes = "个人画像-宿舍出入特征-可能未归情况", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", required = true, value = "学号"),
            @ApiImplicitParam(paramType = "query", name = "bdate", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "edate", dataType = "String", value = "结束时间"),
    })
    @RequestMapping("/dorm/personNoComeBack")
    @ResponseBody
    public CommResponse personNoComeBackAOP(ParamsStatus para) {
        if (para.getStart() == null) {
            para.setStart(0);
            para.setLimit(10);
        }

        List<Map<String, Object>> result = accessInoutService.personNoComeBack(para);

        if (null == result || result.size() == 0) {
            return CommResponse.success(null);
        }

        return CommResponse.success(result);

    }

    /**
     * 个人 - 可能未归情况记录数量
     *
     * @param para
     * @return
     */
    @ApiOperation(value = "个人画像-宿舍出入特征-可能未归情况", notes = "个人画像-宿舍出入特征-可能未归情况", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", required = true, value = "学号"),
            @ApiImplicitParam(paramType = "query", name = "bdate", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "edate", dataType = "String", value = "结束时间"),
    })
    @RequestMapping("/dorm/personNoComeBackSize")
    @ResponseBody
    public CommResponse personNoComeBackCountAOP(ParamsStatus para) {

        Long result = accessInoutService.personNoComeBackSize(para);

        return CommResponse.success(result);
    }

	/**
	 * @api {POST} /api/v2/behaviour/dorm/noComeBackByDay
	 * @apiName personLater
	 * @apiGroup behaviour
	 * @apiVersion 2.0.0
	 * @apiDescription 宿舍出入特征--群体  --某天可能未归学生列表
	 * @apiPermission person_Later
	 * @apiSampleRequest http://localhost:8080/api/v2/behaviour/dorm/noComeBackByDay
	 * @apiParam {String} [day] 日期 yyyy-MM-dd
	 * <p>
	 * <p>
	 * {
	 * "schoolCode": "jkd",
	 * 'courseNo':"TEM4",
	 * "semester":"2015-2016-1"
	 * }
	 * @apiSuccess (200) {String} message 操作成功
	 * @apiSuccess (200) {int} statusCode 200
	 * @apiSuccessExample {json} 返回样例:
	 * {"statusCode":200,
	 * "message":"操作成功",
	 * "data":[{"semester":2014-2015-2,"course_no":"NCRE65N","sum","8"}]
	 * }
	 */
	@ApiOperation(value = "宿舍出入特征-群体-某天可能未归学生-列表", notes = "宿舍出入特征-群体-某天可能未归学生-列表", httpMethod = "POST")
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
	@ResponseBody
	public CommResponse noComeBackByDayAOP(ParamsStatus para) {
		if (para.getStart() == null) {
			para.setStart(0);
			para.setLimit(10);
		}
		Map<String, Object> resultMap = new HashMap<>();
		List<Map<String, Object>> result = accessInoutService.queryNoComeBackByDay(para);
		if (null == result || result.size() == 0) {
			return CommResponse.success(null);
		}
		Long total = accessInoutService.queryNoComeBackByDaySize(para);
		resultMap.put("rows",result);
		resultMap.put("total",total);
		return CommResponse.success(resultMap);
	}
}
