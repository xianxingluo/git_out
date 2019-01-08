package com.ziyun.consume.controller;

import com.ziyun.consume.server.IEcardRecConsumeServer;
import com.ziyun.consume.vo.ConsumeParams;
import com.ziyun.consume.vo.Params;
import com.ziyun.consume.vo.ParamsStatus;
import com.ziyun.consume.vo.ResultData;
import com.ziyun.utils.requests.CommResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 *
 *
 * @Description:
 * @Created by liquan
 * @date 2017年5月13日 上午10:15:34
 *
 */
@Controller
@RequestMapping("/v2/consume")
@Api(tags = "消费微服务", description = "消费微服务相关api")
public class EcardRecConsumeController {



	@Autowired
	public IEcardRecConsumeServer consumeServe;

	//暂时未用
//    @RequestMapping("/toconsume")
	public ModelAndView toConsume() throws Exception,
			InterruptedException {
		ModelAndView mv = new ModelAndView("behavior/comBehaviormng");
		// mv.addObject("rightmng", "");
		return mv;
	}

	//暂时未用
	/**
	 * 画像：标签{统计消费总金额、人均消费金额、人均消费频次}
     *
	 * @param para
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping(value = "/sumcollect")
//	@ResponseBody
	public CommResponse sumCollectAOP(Params para) throws Exception {

		Map list = consumeServe.sumCollect(para);
		return CommResponse.success(list);
	}

	/**
	 * @api {POST} /api/v2/consume/sumtoplist
	 * @apiName sumtoplist
	 * @apiGroup consume
	 * @apiVersion 2.0.0
	 * @apiDescription 消费大户榜top10 || 有消费记录的天数/30作为月数的月平均、所有时间范围的天数/30作为月数的月平均
	 * @apiPermission 登录用户
	 * @apiSampleRequest http://127.0.0.1:80/api/v2/consume/sumtoplist
	 * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
	 * @apiParam {String} [facultyCode] 院系code
	 * @apiParam {String} [majorCode] 专业code
	 * @apiParam {String} [classSelect] 班级code多选，用逗号隔开：：班级只有code,没有名称
	 * @apiParam {String} [bdate] 开始时间
	 * @apiParam {String} [edate] 结束时间
	 * @apiParam {Integer} [start] 分页参数：从0开始
	 * @apiParam {Integer} [limit] 分页参数：每页多少条数据
	 * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
	 * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
	 * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
	 * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
	 * @apiParamExample {json} 请求例子:
	 * {
	 * "schoolCode": "jkd"
	 * }
	 * @apiSuccess (200) {String} message 提示信息code 根据code去查找错误提示信息
	 * @apiSuccess (200) {int} statusCode 200
	 * @apiSuccessExample {json} 返回样例:
	 * {
	 * "statusCode": 200,
	 * "message": "提示信息code 根据code去查找错误提示信息"
	 * "rows":[{"avg":837.26(有消费天数的月均金额),"stopHour":0,"startHour":0,"num":483,"name":"褚帅","index":1,"outid":"158111541111","sum":13396.16(总金额),"allAvg":637.91(全部天数的月均金额),"validDays":0},
	 * }
	 * @apiErrorExample {json} 错误返回:
	 * {
	 * "statusCode": 14695
	 * "message": "提示信息code 根据code去查找错误提示信息"
	 * }
	 */
	//暂时未用
//	@RequestMapping(value = "/sumtoplist")
//	@ResponseBody
	public CommResponse sumTopListAOP(Params para)
			throws Exception {
		if(null == para.getStart()){//如果不传分页：默认查询top10
			para.setStart(0);
		}
        //学历选择为研究生，向前端返回为null
        if (StringUtils.isNotBlank(para.getEduStatus()) && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        List list = consumeServe.sumTopList(para);
		return CommResponse.success(list);
	}


	@RequestMapping(value = "/sumTopListDetail")
	@ResponseBody
	@ApiOperation(value = "群体--消费排名列表", notes = "消费排名列表")
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
			@ApiImplicitParam(name = "bdate", paramType = "query", dataType = "String", required = false, value = " 开始时间"),
			@ApiImplicitParam(name = "edate", paramType = "query", dataType = "String", required = false, value = " 结束时间"),
			@ApiImplicitParam(name = "name", paramType = "query", dataType = "String", required = false, value = " 学生姓名"),
			@ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")

	})
	public CommResponse sumTopListDetailAOP(Params para)
			throws Exception {
        if (para.getEducation() != null && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
		List list = consumeServe.sumTopList(para);
		return CommResponse.success(list);
	}



	@RequestMapping(value = "/sumTopListDetailTotal")
	@ResponseBody
	@ApiOperation(value = "群体--消费排名列表总长度", notes = "消费排名列表总长度")
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
			@ApiImplicitParam(name = "bdate", paramType = "query", dataType = "String", required = false, value = " 开始时间"),
			@ApiImplicitParam(name = "edate", paramType = "query", dataType = "String", required = false, value = " 结束时间"),
			@ApiImplicitParam(name = "name", paramType = "query", dataType = "String", required = false, value = " 学生姓名"),
			@ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")

	})
	public CommResponse sumTopListDetailTotalAOP(Params para)
			throws Exception {
        //用户选择研究生
        if (para.getEducation() != null && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
		ResultData size = consumeServe.sumCount(para);// 总数量
		return CommResponse.success(size.getSum().intValue());
	}

	/**
	 * @api {POST} /api/v2/consume/avgbottomlist
	 * @apiName avgbottomlist
	 * @apiGroup consume
	 * @apiVersion 2.0.0
	 * @apiDescription 月节俭榜top10 || 有消费记录的天数/30作为月数的月平均、所有时间范围的天数/30作为月数的月平均
	 * @apiPermission 登录用户
	 * @apiSampleRequest http://127.0.0.1:80/api/v2/consume/avgbottomlist
	 * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
	 * @apiParam {String} [facultyCode] 院系code
	 * @apiParam {String} [majorCode] 专业code
	 * @apiParam {String} [classSelect] 班级code多选，用逗号隔开：：班级只有code,没有名称
	 * @apiParam {String} [bdate] 开始时间
	 * @apiParam {String} [edate] 结束时间
	 * @apiParam {Integer} [start] 分页参数：从0开始
	 * @apiParam {Integer} [limit] 分页参数：每页多少条数据
	 * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
	 * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
	 * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
	 * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
	 * @apiParamExample {json} 请求例子:
	 * {
	 * "schoolCode": "jkd"
	 * }
	 * @apiSuccess (200) {String} message 提示信息code 根据code去查找错误提示信息
	 * @apiSuccess (200) {int} statusCode 200
	 * @apiSuccessExample {json} 返回样例:
	 * {
	 * "statusCode": 200,
	 * "message": "提示信息code 根据code去查找错误提示信息"
	 * "rows":[{"avg":403.97(有消费天数的月均金额),"stopHour":0,"startHour":0,"num":196,"name":"张慧","index":1,"outid":"1345734102","sum":2423.80,"allAvg":115.42(全部天数的月均金额),"validDays":0},
	 * }
	 * @apiErrorExample {json} 错误返回:
	 * {
	 * "statusCode": 14695
	 * "message": "提示信息code 根据code去查找错误提示信息"
	 * }
	 */
//暂时未用
//	@RequestMapping(value = "/avgbottomlist")
//	@ResponseBody
	public CommResponse avgBottomListAOP(Params para)
			throws Exception {
		if(null == para.getStart()){//如果不传分页：默认查询top10
			para.setStart(0);
		}
        //学历选择为研究生，向前端返回为null
        if (StringUtils.isNotBlank(para.getEduStatus()) && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        List list = consumeServe.avgBottomList(para);
		return CommResponse.success(list);
	}

	/**
	 * @api {POST} /api/v2/consume/avgbottomlistdetail
	 * @apiName avgbottomlistdetail
	 * @apiGroup consume
	 * @apiVersion 2.0.0
	 * @apiDescription 月节俭榜top10(图下面的分页明细) || 有消费记录的天数/30作为月数的月平均、所有时间范围的天数/30作为月数的月平均
	 * @apiPermission 登录用户
	 * @apiSampleRequest http://127.0.0.1:80/api/v2/consume/avgbottomlistdetail
	 * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
	 * @apiParam {String} [facultyCode] 院系code
	 * @apiParam {String} [majorCode] 专业code
	 * @apiParam {String} [classSelect] 班级code多选，用逗号隔开：：班级只有code,没有名称
	 * @apiParam {String} [bdate] 开始时间
	 * @apiParam {String} [edate] 结束时间
	 * @apiParam {Integer} [start] 分页参数：从0开始
	 * @apiParam {Integer} [limit] 分页参数：每页多少条数据
	 * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
	 * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
	 * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
	 * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
	 * @apiParamExample {json} 请求例子:
	 * {
	 * "schoolCode": "jkd"
	 * }
	 * @apiSuccess (200) {String} message 提示信息code 根据code去查找错误提示信息
	 * @apiSuccess (200) {int} statusCode 200
	 * @apiSuccessExample {json} 返回样例:
	 * {
	 * "statusCode": 200,
	 * "message": "提示信息code 根据code去查找错误提示信息"
	 * "rows":[{"classCode":"13457341","avg":577.37(有消费天数的月均金额),"stopHour":0,"startHour":0,"num":202,"name":"张怡敏","index":1,"outid":"1345734103","sum":3464.20(消费总金额),"allAvg":164.96(全部天数的月均金额),"majorName":"计算机科学与技术"},
	 * }
	 * @apiErrorExample {json} 错误返回:
	 * {
	 * "statusCode": 14695
	 * "message": "提示信息code 根据code去查找错误提示信息"
	 * }
	 */
//暂时未用
	@RequestMapping(value = "/avgBottomListDetail")
	@ResponseBody
	public CommResponse avgBottomListDetailAOP(Params para)
			throws Exception {
        //如果用户选择了研究生，返回null
        if (para.getEducation() != null && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
		List list = consumeServe.avgBottomListDetail(para);
		return CommResponse.success(list);
	}

	/**
	 * @api {POST} /api/v2/consume/avgbottomlistdetailtotal
	 * @apiName avgbottomlistdetailtotal
	 * @apiGroup consume
	 * @apiVersion 2.0.0
	 * @apiDescription 月节俭榜top10(图下面的分页明细的总条数) || 有消费记录的天数/30作为月数的月平均、所有时间范围的天数/30作为月数的月平均
	 * @apiPermission 登录用户
	 * @apiSampleRequest http://127.0.0.1:80/api/v2/consume/avgbottomlistdetailtotal
	 * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
	 * @apiParam {String} [facultyCode] 院系code
	 * @apiParam {String} [majorCode] 专业code
	 * @apiParam {String} [classSelect] 班级code多选，用逗号隔开：：班级只有code,没有名称
	 * @apiParam {String} [bdate] 开始时间
	 * @apiParam {String} [edate] 结束时间
	 * @apiParam {Integer} [start] 分页参数：从0开始
	 * @apiParam {Integer} [limit] 分页参数：每页多少条数据
	 * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
	 * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
	 * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
	 * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
	 * @apiParamExample {json} 请求例子:
	 * {
	 * "schoolCode": "jkd"
	 * }
	 * @apiSuccess (200) {String} message 提示信息code 根据code去查找错误提示信息
	 * @apiSuccess (200) {int} statusCode 200
	 * @apiSuccessExample {json} 返回样例:
	 * {
	 * "statusCode": 200,
	 * "message": "提示信息code 根据code去查找错误提示信息"
	 * "rows":[{"classCode":"13457341","avg":577.37(有消费天数的月均金额),"stopHour":0,"startHour":0,"num":202,"name":"张怡敏","index":1,"outid":"1345734103","sum":3464.20(消费总金额),"allAvg":164.96(全部天数的月均金额),"majorName":"计算机科学与技术"},
	 * }
	 * @apiErrorExample {json} 错误返回:
	 * {
	 * "statusCode": 14695
	 * "message": "提示信息code 根据code去查找错误提示信息"
	 * }
	 */
	//暂时未用
//	@RequestMapping(value = "/avgbottomlistdetailtotal")
//	@ResponseBody
	public CommResponse avgBottomListDetailTotalAOP(Params para)
			throws Exception {
        //如果用户选择了研究生，返回null
        if (para.getEducation() != null && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
		ResultData size = consumeServe.sumCount(para);//总数量
		return CommResponse.success(size.getSum().intValue());
	}

	/**
	 * 4、 消费总体偏好：top5
     *
	 * <!-- 合并考试费的：把考试相关的费用全部合并到考试费这一项中显示 -->
     *
	 * <!-- 4、 消费总体偏好 -通过对Java对日期的格式进行再次处理 -->
     *
	 * <!-- 7.2、 消费人群分析 -｛消费类型｝ -通过对Java对日期的格式进行再次处理 -->
     *
	 * @param para
	 * @return
	 * @throws Exception
	 */
	//暂时未用
//	@RequestMapping(value = "/preferencelist")
//	@ResponseBody
	public CommResponse preferenceListAOP(Params para) throws Exception {

		List<Map<String, Object>> list = consumeServe.preferenceList(para);
		int i = 0;
		for (Map value : list) {
			value.put("index", i++);
		}
		return CommResponse.success(list);
	}

	/**
	 * 4、 消费总体偏好 ++ 显示分类下的商户排名：只显示消费商户，去掉保险、考试等学校统一收费
     *
	 * @param para
	 * @return
	 * @throws Exception
	 */
	//暂时未用
//	@RequestMapping(value = "/preferencelisttop")
//	@ResponseBody
	public CommResponse preferenceListTopAOP(Params para)
			throws Exception {
		// String someCode = para.getSomeCode();
		// if (StringUtils.isNotEmpty(someCode)) {
		// // ：只显示消费商户，去掉保险、考试等学校统一收费
		// if (someCode.startsWith("8") || someCode.equals("218")
		// || someCode.equals("406")) {
		// return null;
		// }
		// }
		List<Map<String, Object>> list = consumeServe.preferenceListTop(para);
		return CommResponse.success(list);
	}

	/**
	 * 10.1.2、 消费类目占比（消费类型、男女：统计消费金额）
	 *
	 * @param para
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/preferenceSex")
	@ResponseBody
	@ApiOperation(value = "群体--消费类目占比", notes = "消费类目占比")
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
			@ApiImplicitParam(name = "bdate", paramType = "query", dataType = "String", required = false, value = " 开始时间"),
			@ApiImplicitParam(name = "edate", paramType = "query", dataType = "String", required = false, value = " 结束时间")

	})
	public CommResponse preferenceSexAOP(Params para) throws Exception {
		//学历选择为研究生，向前端返回为null
		if (para.getEducation() != null && para.getEducation() == 2) {
			return CommResponse.success(null);
		}
		return CommResponse.success(consumeServe.preferenceSex(para));
	}

	/**
	 * 4、 消费总体偏好：top5
	 * <p>
	 * 考试费的：把考试相关的费用全部合并到考试费这一项中显示
	 *
	 * @param para
	 * @return
	 * @throws Exception
	 */
//暂时未用
//	@RequestMapping(value = "/preferencelisttest")
//	@ResponseBody
	public CommResponse preferenceListTestAOP(Params para) throws Exception {
		return CommResponse.success(consumeServe.preferenceListTest(para));
	}

	/**
	 * 10.1.2.3、 消费类目占比的明细(去掉考试部分的)只统计各个消费分类的商家
     *
	 * 10.1.2.2、 消费类目占比的明细(考试部分的)：汇总考试的三级分类的金额
     *
	 * 全部分类：包含（餐费、商场购物、考试类、其他类）
     *
	 * @param para
	 * @return
	 * @throws Exception
	 */
	//暂时未用
//	@RequestMapping(value = "/preferencedeptnamesex")
//	@ResponseBody
	public CommResponse preferenceDeptnameSexAOP(Params para) throws Exception {

		return CommResponse.success(consumeServe.preferenceDeptnameSex(para));
	}

	/**
	 * 10.1.2.3、 消费类目占比的明细的总条数(去掉考试部分的)只统计各个消费分类的商家
	 *
	 * 10.1.2.2、 消费类目占比的明细(考试部分的)：汇总考试的三级分类的金额
	 *
	 * 全部分类：包含（餐费、商场购物、考试类、其他类）
	 *
	 * @param para
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping(value = "/preferencedeptnamesextotal")
//	@ResponseBody
	public CommResponse preferenceDeptnameSexTotalAOP(Params para) throws Exception {

		return CommResponse.success(consumeServe.preferenceDeptnameSexTotal(para));
	}

	/**
	 * 4、 消费总体偏好：top5
     *
	 * <!-- 不合并考试费的： --> ::{消费个人页面的单图是不合并的：用这个URL}
     *
	 * <!-- 4、 消费总体偏好 -通过对Java对日期的格式进行再次处理 -->
     *
	 * @param para
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/preferenceListNot")
	@ResponseBody
	@ApiOperation(value = "个人--消费类目占比", notes = "消费类目占比")
	@ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")
	public CommResponse preferenceListNotAOP(Params para) throws Exception {

		return CommResponse.success(consumeServe.preferenceListNot(para));
	}


	@RequestMapping(value = "datetimeList")
	@ResponseBody
	@ApiOperation(value = "群体--消费金额", notes = "消费金额")
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
			@ApiImplicitParam(name = "bdate", paramType = "query", dataType = "String", required = false, value = " 开始时间"),
			@ApiImplicitParam(name = "edate", paramType = "query", dataType = "String", required = false, value = " 结束时间"),
			@ApiImplicitParam(name = "name", paramType = "query", dataType = "String", required = false, value = " 学生姓名"),
			@ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")

	})
	public CommResponse timeChangeListAOP(Params para) throws Exception {
        //学历选择为研究生，向前端返回为null
        if (para.getEducation() != null && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        return CommResponse.success(consumeServe.timeChangeList(para));
	}

	/**
	 * @api {POST} /api/v2/consume/datetimedetail
	 * @apiName datetimedetail
	 * @apiGroup consume
	 * @apiVersion 2.0.0
	 * @apiDescription ｛图表下的明细：分页｝消费金额变化趋势 || 每天的：消费总金额、消费总人数、有效人数日人均、全部人数日人均、有效人数月人均、全部人数月人均
	 * @apiPermission 登录用户
	 * @apiSampleRequest http://127.0.0.1:80/api/v2/consume/datetimedetail
	 * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
	 * @apiParam {String} [facultyCode] 院系code
	 * @apiParam {String} [majorCode] 专业code
	 * @apiParam {String} [classSelect] 班级code多选，用逗号隔开：：班级只有code,没有名称
	 * @apiParam {String} [bdate] 开始时间
	 * @apiParam {String} [edate] 结束时间
	 * @apiParam {Integer} [start] 分页参数：从0开始
	 * @apiParam {Integer} [limit] 分页参数：每页多少条数据
	 * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
	 * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
	 * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
	 * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
	 * @apiParamExample {json} 请求例子:
	 * {
	 * "schoolCode": "jkd"
	 * }
	 * @apiSuccess (200) {String} message 提示信息code 根据code去查找错误提示信息
	 * @apiSuccess (200) {int} statusCode 200
	 * @apiSuccessExample {json} 返回样例:
	 * {
	 * "statusCode": 200,
	 * "message": "提示信息code 根据code去查找错误提示信息"
	 * "rows":[{{"datetime":1441267200000,"avg":23.00(有效人数的人均金额),"stopHour":0,"startHour":0,"datetimeStr":"2015-09-04"（显示的时间）,"num":1,"index":1,"monthAvg":633.07(有效人数的月人均金额),"sum":23.00（消费总金额）,"allMonthAvg":180.71(全部人数的月人均金额),"allAvg":0.00(全部人数的日人均金额)},
	 * }
	 * @apiErrorExample {json} 错误返回:
	 * {
	 * "statusCode": 14695
	 * "message": "提示信息code 根据code去查找错误提示信息"
	 * }
	 */
	//暂时未用
//	@RequestMapping(value = "datetimedetail")
//	@ResponseBody
	public CommResponse datetimeDetailAOP(Params para) throws Exception {

		List<Map<String, Object>> list = consumeServe.timeChangeList(para);
		return CommResponse.success(list);
	}

	/**
	 * @api {POST} /api/v2/consume/datetimedetailtotal
	 * @apiName datetimedetail
	 * @apiGroup consume
	 * @apiVersion 2.0.0
	 * @apiDescription ｛图表下的明细：分页｝消费金额变化趋势总条数 || 每天的：消费总金额、消费总人数、有效人数日人均、全部人数日人均、有效人数月人均、全部人数月人均
	 * @apiPermission 登录用户
	 * @apiSampleRequest http://127.0.0.1:80/api/v2/consume/datetimedetailtotal
	 * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
	 * @apiParam {String} [facultyCode] 院系code
	 * @apiParam {String} [majorCode] 专业code
	 * @apiParam {String} [classSelect] 班级code多选，用逗号隔开：：班级只有code,没有名称
	 * @apiParam {String} [bdate] 开始时间
	 * @apiParam {String} [edate] 结束时间
	 * @apiParam {Integer} [start] 分页参数：从0开始
	 * @apiParam {Integer} [limit] 分页参数：每页多少条数据
	 * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
	 * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
	 * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
	 * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
	 * @apiParamExample {json} 请求例子:
	 * {
	 * "schoolCode": "jkd"
	 * }
	 * @apiSuccess (200) {String} message 提示信息code 根据code去查找错误提示信息
	 * @apiSuccess (200) {int} statusCode 200
	 * @apiSuccessExample {json} 返回样例:
	 * {
	 * "statusCode": 200,
	 * "message": "提示信息code 根据code去查找错误提示信息"
	 * "rows":[{{"datetime":1441267200000,"avg":23.00(有效人数的人均金额),"stopHour":0,"startHour":0,"datetimeStr":"2015-09-04"（显示的时间）,"num":1,"index":1,"monthAvg":633.07(有效人数的月人均金额),"sum":23.00（消费总金额）,"allMonthAvg":180.71(全部人数的月人均金额),"allAvg":0.00(全部人数的日人均金额)},
	 * }
	 * @apiErrorExample {json} 错误返回:
	 * {
	 * "statusCode": 14695
	 * "message": "提示信息code 根据code去查找错误提示信息"
	 * }
	 */
	//暂时未用
//	@RequestMapping(value = "datetimedetailtotal")
//	@ResponseBody
	public CommResponse datetimeDetailTotalAOP(Params para) throws Exception {

		 ResultData size = consumeServe.timeChangeCount(para);//总数量
		return CommResponse.success(size.getSum().intValue());
	}

	/**
	 * 5、 消费金额变化趋势 --：：个人
     *
	 * 个人每天的消费总金额
     *
	 * 班级每天的平均消费额：：作为个人金额的参考来显示
     *
	 * @param para
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/timeChangeListOne")
	@ResponseBody
	@ApiOperation(value = "个人--消费金额", notes = "消费金额")
	@ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")
	public CommResponse timeChangeListOneAOP(Params para) throws Exception {

		return CommResponse.success(consumeServe.timeChangeListOne(para));
	}

	/**
	 * 6、 消费时段分布 -按照周几、小时汇总-平均 -
     *
	 * 周几排序、各个小时：汇总平均列表
     *
	 * @param para
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/weekHourList")
	@ResponseBody
	@ApiOperation(value = "个人--消费时段分布", notes = "消费时段分布")
	@ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")
	public CommResponse weekHourListAOP(Params para) throws Exception {

		return CommResponse.success(consumeServe.weekHourList(para));//个体查询
	}

	/**
	 * 6.1.2、 消费时段分布 -按照周几、小时汇总:消费金额
     *
	 * 周几排序、各个小时：汇总平均
     *
	 * @param para
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/weekHourListNew")
	@ResponseBody
	@ApiOperation(value = "群体--消费时段", notes = "消费时段")
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
			@ApiImplicitParam(name = "bdate", paramType = "query", dataType = "String", required = false, value = " 开始时间"),
			@ApiImplicitParam(name = "edate", paramType = "query", dataType = "String", required = false, value = " 结束时间"),
			@ApiImplicitParam(name = "name", paramType = "query", dataType = "String", required = false, value = " 学生姓名"),
			@ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")

	})
	public CommResponse weekHourListNewAOP(Params para) throws Exception {
        //学历选择为研究生，向前端返回为null
        if (para.getEducation() != null && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        return CommResponse.success(consumeServe.weekHourListNew(para));
	}

	/**
	 * @api {POST} /api/v2/consume/hourlist
	 * @apiName hourlist
	 * @apiGroup consume
	 * @apiVersion 2.0.0
	 * @apiDescription 消费时钟表｛查询某个人某一天的消费时段分布｝ -按小时汇总：消费总金额
	 * @apiPermission 登录用户
	 * @apiSampleRequest http://127.0.0.1:80/api/v2/consume/hourlist
	 * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
	 * @apiParam {String} [facultyCode] 院系code
	 * @apiParam {String} [majorCode] 专业code
	 * @apiParam {String} [classSelect] 班级code多选，用逗号隔开：：班级只有code,没有名称
	 * @apiParam {String} [bdate] 开始时间
	 * @apiParam {String} [edate] 结束时间
	 * @apiParam {Integer} [start] 分页参数：从0开始
	 * @apiParam {Integer} [limit] 分页参数：每页多少条数据
	 * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
	 * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
	 * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
	 * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
	 * @apiParamExample {json} 请求例子:
	 * {
	 * "schoolCode": "jkd"
	 * }
	 * @apiSuccess (200) {String} message 提示信息code 根据code去查找错误提示信息
	 * @apiSuccess (200) {int} statusCode 200
	 * @apiSuccessExample {json} 返回样例:
	 * {
	 * "statusCode": 200,
	 * "message": "提示信息code 根据code去查找错误提示信息"
	 * "data":[{{"hour":"0"(小时段),"stopHour":0,"startHour":0,"index":0,"sum":0(小时段内的平均消费)}
	 * }
	 * @apiErrorExample {json} 错误返回:
	 * {
	 * "statusCode": 14695
	 * "message": "提示信息code 根据code去查找错误提示信息"
	 * }
	 */
	//暂时未用
//	@RequestMapping(value = "hourlist")
//	@ResponseBody
	public CommResponse hourList(Params para) throws Exception {

		return CommResponse.success(consumeServe.hourList(para));
	}


	@RequestMapping(value = "detailHour")
	@ResponseBody
	@ApiOperation(value = "个人--消费类目占比", notes = "消费类目占比")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号"),
			@ApiImplicitParam(name = "bdate", paramType = "query", dataType = "String", required = false, value = " 开始时间"),
			@ApiImplicitParam(name = "edate", paramType = "query", dataType = "String", required = false, value = " 结束时间")

	})
	public CommResponse detailHour(Params para) throws Exception {
		return CommResponse.success(consumeServe.detailHour(para));
	}

	/**
	 * 6.1.2.1、｛图表下的明细｝ 消费时段分布 -按照周几、小时汇总:消费金额
     *
	 * 周几排序、各个小时：汇总平均
     *
	 * 按2个小时（或是几个小时）再此汇总
     *
	 * @param para
	 * @return
	 * @throws Exception
	 */
	//暂时未用
//	@RequestMapping(value = "/weekhourlistnewdetail")
//	@ResponseBody
	public CommResponse weekHourListNewDetailAOP(Params para) throws Exception {

		return CommResponse.success(consumeServe.weekHourListNewDetail(para));
	}

	/**
	 * 7.1、消费人群分析 --｛男、女｝按照金额，时段内汇总平均 - 周几排序、男女分成2个列表
     *
	 * @param para
	 * @return
	 * @throws Exception
	 */
	//暂时未用
//	@RequestMapping(value = "/sexweeklist")
//	@ResponseBody
	public CommResponse sexWeekListAOP(Params para) throws Exception {

		return CommResponse.success(consumeServe.sexWeekList(para));
	}

	/**
	 * 7.2、 消费人群分析 --｛消费类型｝
     *
	 * @param para
	 * @return
	 * @throws Exception
	 */
	//暂时未用
//	@RequestMapping(value = "/preferenceotherlist")
//	@ResponseBody
	public CommResponse preferenceOtherListAOP(Params para) throws Exception {

		return CommResponse.success(consumeServe.preferenceList(para));
	}


	@RequestMapping(value = "sexWeekPreferenceTop")
	@ResponseBody
	@ApiOperation(value = "群体--消费人群", notes = "消费人群")
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
			@ApiImplicitParam(name = "bdate", paramType = "query", dataType = "String", required = false, value = " 开始时间"),
			@ApiImplicitParam(name = "edate", paramType = "query", dataType = "String", required = false, value = " 结束时间"),
			@ApiImplicitParam(name = "name", paramType = "query", dataType = "String", required = false, value = " 学生姓名"),
			@ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")

	})
	public CommResponse sexWeekClientTypeListAOP(Params para) throws Exception {
        //如果用户选择了研究生，返回null
        if (para.getEducation() != null && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
		Params param = new Params();
		BeanUtils.copyProperties(para, param);
		Map<String, Object> listSex = consumeServe.sexWeekList(para);
		if (listSex == null) {
			return CommResponse.success(null);
		}
		Set<String> nameLegend = new HashSet<String>();
        if (StringUtils.equals("男", para.getSex()) || null == para.getSex()) {
            param.setSex("0");
            List<Map<String, Object>> listMan = consumeServe.preferenceList(param);
            listSex.put("manClass", listMan);
            for (Map<String, Object> man : listMan) {
                nameLegend.add((String) man.get("dscrp"));
            }
            if (StringUtils.equals("男", para.getSex())) {
                listSex.remove("women");
            }
        }
        if (StringUtils.equals("女", para.getSex()) || null == para.getSex()) {
            param.setSex("1");
            List<Map<String, Object>> listWomen = consumeServe
                    .preferenceList(param);
            listSex.put("womenClass", listWomen);

            if (listWomen != null) {
                for (Map<String, Object> women : listWomen) {
                    nameLegend.add((String) women.get("dscrp"));
                }
            }
            if (StringUtils.equals("女", para.getSex())) {
                listSex.remove("man");
            }
        }
        listSex.put("nameLegend", nameLegend);
		if(nameLegend.size()==0){//没有数据时返回空，方便前端判断
			return CommResponse.success(null);
		}
		return CommResponse.success(listSex);
	}

	/**
	 * @api {POST} /api/v2/consume/weektypeavglist
	 * @apiName weektypeavglist
	 * @apiGroup consume
	 * @apiVersion 2.0.0
	 * @apiDescription 消费人群分析｛明细列表｝ || 1、计算周几的：各个消费类型的金额、男女金额占比、人均消费
	 * @apiPermission 登录用户
	 * @apiSampleRequest http://127.0.0.1:80/api/v2/consume/datetimelist
	 * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
	 * @apiParam {String} [facultyCode] 院系code
	 * @apiParam {String} [majorCode] 专业code
	 * @apiParam {String} [classSelect] 班级code多选，用逗号隔开：：班级只有code,没有名称
	 * @apiParam {String} [bdate] 开始时间
	 * @apiParam {String} [edate] 结束时间
	 * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
	 * @apiParam {Integer} [politicalCode] 政治面貌code:(1、团员，2、预备党员，3、党员)
	 * @apiParam {Integer} [scholarship] 传1：优等生  传参过来，即可；或者不传
	 * @apiParam {Integer} [impoverish] 传1：贫困生判断，传参过来，即可；或者不传
	 * @apiParamExample {json} 请求例子:
	 * {
	 * "schoolCode": "jkd"
	 * }
	 * @apiSuccess (200) {String} message 提示信息code 根据code去查找错误提示信息
	 * @apiSuccess (200) {int} statusCode 200
	 * @apiSuccessExample {json} 返回样例:
	 * {
	 * "statusCode": 200,
	 * "message": "提示信息code 根据code去查找错误提示信息"
	 * {"womansum":521.75,(周几的女性消费总额)"week":"周一","index":0,"sum":3796.94(周几的消费总额),"sum1":32.19(用水的消费金额),"womanPercent":"14.00%"(女性的消费占比),"sum2":41.02(用电的消费金额),"avg":23.07(有效人数的平均消费),"weekindex":1,"stopHour":0,"startHour":0,"mansum":3275.19(周几的男性消费总额),"allAvg":12.05(周几除以总人数的平均消费),"sum5":2575.29(餐费的消费金额),"sum6":1012.43(商场购物的消费金额),"manPercent":"86.00%"(男性的消费占比),"sum3":33.32(淋浴的消费占比),"validDays":0,"sum4":70.20(考试的消费占比)},
	 * }
	 * @apiErrorExample {json} 错误返回:
	 * {
	 * "statusCode": 14695
	 * "message": "提示信息code 根据code去查找错误提示信息"
	 * }
	 */
	//暂时未用
//	@RequestMapping(value = "weektypeavglist")
//	@ResponseBody
	public CommResponse WeekTypePersonAvgListAOP(Params para) throws Exception {

		return CommResponse.success(consumeServe.WeekTypePersonAvgList(para));
	}


    /**
	 * 8.1、消费开支情况 -消费情况
     *
	 * 8.2、消费开支情况 -充值情况
     *
	 * @param para
	 * @return
	 * @throws Exception
	 */
	//暂时未用
//	@RequestMapping(value = "/consumedayList")
//	@ResponseBody
	public CommResponse consumeDayListAOP(Params para) throws Exception {

		List<Map<String, Object>> listCo = consumeServe.consumeDayList(para);// 8.1、-消费情况
		// List<Map<String, Object>> listRe =
		// consumeServe.rechargeDayList(para);// 8.2、-充值情况
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("consume", listCo);
		resultMap.put("recharge", null);
		return CommResponse.success(resultMap);
	}


	@RequestMapping("/consumeStudentList")
	@ResponseBody
	@ApiOperation(value = "群体--消费学生列表", notes = "消费学生列表")
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
			@ApiImplicitParam(name = "bdate", paramType = "query", dataType = "String", required = false, value = " 开始时间"),
			@ApiImplicitParam(name = "edate", paramType = "query", dataType = "String", required = false, value = " 结束时间"),
			@ApiImplicitParam(name = "name", paramType = "query", dataType = "String", required = false, value = " 学生姓名"),
			@ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")

	})
	public CommResponse getConsumeStudentListAOP(Params params) {
		if (null != params.getTermtype() && params.getTermtype() == 2) {
			return CommResponse.success(null);
		}
		//如果用户选择了研究生，返回null
		if (params.getEducation() != null && params.getEducation() == 2) {
			return CommResponse.success(null);
		}
		if (params.getStart() == null) {
			params.setStart(0);
			params.setLimit(10);
		}
		List<Map<String, Object>> list = consumeServe.getConsumeStudentList(params);


		return CommResponse.success(list);
	}


	@RequestMapping("/consumeStudentCount")
	@ResponseBody
	@ApiOperation(value = "群体--消费学生列表", notes = "消费学生列表")
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
			@ApiImplicitParam(name = "bdate", paramType = "query", dataType = "String", required = false, value = " 开始时间"),
			@ApiImplicitParam(name = "edate", paramType = "query", dataType = "String", required = false, value = " 结束时间"),
			@ApiImplicitParam(name = "name", paramType = "query", dataType = "String", required = false, value = " 学生姓名"),
			@ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")

	})
	public CommResponse getConsumeStudentCountAOP(Params params) {
		if (null != params.getTermtype() && params.getTermtype() == 2) {
			return CommResponse.success(null);
		}
		//如果用户选择了研究生，返回null
		if (params.getEducation() != null && params.getEducation() == 2) {
			return CommResponse.success(null);
		}

		Long size = consumeServe.getConsumeStudentCount(params);

		return CommResponse.success(size);
	}

    @RequestMapping("/common/avgConsumeByDay")
    @ResponseBody
    @ApiOperation(value = "计算消费总金额、消费天数、日平均消费金额", notes = "计算消费总金额、消费天数、日平均消费金额")
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
            @ApiImplicitParam(name = "bdate", paramType = "query", dataType = "String", required = false, value = " 开始时间"),
            @ApiImplicitParam(name = "edate", paramType = "query", dataType = "String", required = false, value = " 结束时间"),
            @ApiImplicitParam(name = "name", paramType = "query", dataType = "String", required = false, value = " 学生姓名"),
            @ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")

    })
    public Map avgConsumeByDay(@RequestBody Params params) throws Exception {
        return consumeServe.avgConsumeByDay(params);
    }

    @RequestMapping("/common/preferenceList")
    @ResponseBody
    @ApiOperation(value = "消费类目占比", notes = "消费类目占比")
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
            @ApiImplicitParam(name = "bdate", paramType = "query", dataType = "String", required = false, value = " 开始时间"),
            @ApiImplicitParam(name = "edate", paramType = "query", dataType = "String", required = false, value = " 结束时间"),
            @ApiImplicitParam(name = "name", paramType = "query", dataType = "String", required = false, value = " 学生姓名"),
            @ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")

    })
    public List<Map<String, Object>> preferenceList(@RequestBody Params params) throws Exception {
        return consumeServe.preferenceList(params);
    }

    @RequestMapping("/common/sumCollect")
    @ResponseBody
    @ApiOperation(value = "消费类目占比", notes = "消费类目占比")
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
            @ApiImplicitParam(name = "bdate", paramType = "query", dataType = "String", required = false, value = " 开始时间"),
            @ApiImplicitParam(name = "edate", paramType = "query", dataType = "String", required = false, value = " 结束时间"),
            @ApiImplicitParam(name = "name", paramType = "query", dataType = "String", required = false, value = " 学生姓名"),
            @ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")

    })
    public Map sumCollect(@RequestBody Params params) throws Exception {
        return consumeServe.sumCollect(params);
    }


	@RequestMapping("/community/consumeFeatures")
    @ResponseBody
    @ApiOperation(value = "消费特征", notes = "社群概述-消费特征")
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
            @ApiImplicitParam(name = "bdate", paramType = "query", dataType = "String", required = false, value = " 开始时间"),
            @ApiImplicitParam(name = "edate", paramType = "query", dataType = "String", required = false, value = " 结束时间"),
            @ApiImplicitParam(name = "name", paramType = "query", dataType = "String", required = false, value = " 学生姓名"),
            @ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")

    })
    public Map<String, Object> consumeFeaturesAOP(Params para, String endDate, String StartDate) throws Exception {
		//如果用户选择了研究生，返回null
		if (para.getEducation() != null && para.getEducation() == 2) {
			return null;
		}
        Map<String, Object> map = new HashMap<>();

        Params paraClass = new Params();
        BeanUtils.copyProperties(para, paraClass);
        // 消费特征
        Map consume = consumeServe.sumCollect(para);
        List<Map<String, Object>> list = consumeServe.preferenceList(paraClass);
        String type = "";
        for (int i = 0; (list != null && i < (list.size() > 2 ? 2 : list.size())); i++) {
            type += list.get(i).get("dscrp") + "、";
        }
        if (type.endsWith("、")) {
            type = type.substring(0, type.length() - 1);
        }
        if (null == consume || consume.isEmpty()) {
            map.put("consume", null);
        } else {
            consume.put("type", type);
            map.put("consume", consume);
        }
        return map;
    }


    /**
     * 基础微服务--个人基本信息-消费特征
     * <p>
     * {"consume":{"avg":7207(个人实际消费天数平均金额),"sum":56307770（消费总金额）,"times":7109(个人时间段内消费天数),"type":"餐费,商场购物"（消费类目喜好）}}
     * }
     *
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping("/common/consumeFeature")
    @ResponseBody
    @ApiOperation(value = "消费特征", notes = "基本信息--个人-消费特征")
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
            @ApiImplicitParam(name = "bdate", paramType = "query", dataType = "String", required = false, value = " 开始时间"),
            @ApiImplicitParam(name = "edate", paramType = "query", dataType = "String", required = false, value = " 结束时间"),
            @ApiImplicitParam(name = "name", paramType = "query", dataType = "String", required = false, value = " 学生姓名"),
            @ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = false, value = " 学号")

    })
    public Map<String, Object> getConsumeFeature(@RequestBody ParamsStatus param) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Map consume = consumeServe.avgConsumeByDay(param);
        List<Map<String, Object>> list = consumeServe.preferenceList(param);
        String type = "";
        for (int i = 0; (list != null && i < (list.size() > 3 ? 3 : list.size())); i++) {
            type += list.get(i).get("dscrp") + ",";
        }
        if (type.endsWith(",")) {
            type = type.substring(0, type.length() - 1);
        }
        if (null == consume || consume.isEmpty()) {
            map.put("consume", null);
        } else {
            consume.put("type", type);
            map.put("consume", consume);
        }
        return map;
    }


    @RequestMapping("/consumeCategoryDetail")
    @ResponseBody
    public CommResponse getConsumeCategoryDetailAOP(ConsumeParams params) {
        //判断是否是研究生
        if (params.getEducation() != null && params.getEducation() == 2) {
            return CommResponse.success(new String[]{});
        }
        Integer queryType = params.getQueryType();
        List<Map<String, Object>> list = null;
        switch (queryType) {
            case 1:
                //展示消费全部：餐费，考试费，购物，用水，用电，其他
                list = consumeServe.getConsumeCategoryDetail(params);
                break;
            //餐厅详情
            case 2:
                //商户详情
            case 3:
                //窗口详情
            case 4:
                list = consumeServe.getCanteenConsumeDetail(params);
                break;
            //购物
            case 5:
                //用水
            case 6:
                //用电
            case 7:
                //其他
            case 8:
                list = consumeServe.getShopConsumeDetail(params);
                break;
            //考试费 ：英语，计算机，普通话等
            case 9:
                //考试费 ：英语四级，英语六级等
            case 10:
                list = consumeServe.getExamConsumeDetail(params);
                break;

            default:
                break;
        }
        if (CollectionUtils.isEmpty(list)) {
            return CommResponse.success(new String[]{});
        }
        return CommResponse.success(list);
    }

    @RequestMapping("/consumeCategoryDetailCount")
    @ResponseBody
    public CommResponse getConsumeCategoryDetailCount(ConsumeParams params) {
        //判断是否是研究生
        if (params.getEducation() != null && params.getEducation() == 2) {
            return CommResponse.success(new String[]{});
        }
        Integer queryType = params.getQueryType();
        Integer count = 0;
        switch (queryType) {
            case 2:
            case 3:
            case 4:
                count = consumeServe.getCanteenConsumeDetailCount(params);
                break;
            //购物
            case 5:
                //用水
            case 6:
                //用电
            case 7:
                //其他
            case 8:
                count = consumeServe.getShopConsumeDetailCount(params);
                break;
            case 9:
            case 10:
                count = consumeServe.getExamConsumeDetailCount(params);
                break;
            default:
                break;
        }
        return CommResponse.success(count);
    }

    /**
     * 消费类目明细 ，查询餐费的所有餐饮
     *
     * @param params
     * @return
     */
    @RequestMapping("/consumeDptNames")
    @ResponseBody
    public CommResponse getConsumeDptNames(ConsumeParams params) {
        List<String> dptNames = consumeServe.getConsumeDptNames(params);
        if (CollectionUtils.isEmpty(dptNames)) {
            return CommResponse.success(new String[]{});
        }
        return CommResponse.success(dptNames);
    }



}
