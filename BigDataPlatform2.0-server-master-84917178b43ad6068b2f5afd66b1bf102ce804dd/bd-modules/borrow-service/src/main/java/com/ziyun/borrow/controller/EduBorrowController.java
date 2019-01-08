package com.ziyun.borrow.controller;

import com.ziyun.borrow.model.BorrowParamsVo;
import com.ziyun.borrow.service.IEduBorrowService;
import com.ziyun.common.model.Params;
import com.ziyun.common.model.ParamsStatus;
import com.ziyun.utils.common.ParamUtils;
import com.ziyun.utils.requests.CommResponse;
import com.ziyun.utils.requests.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by liquan
 */
@Api(tags = "借阅特征", description = "借阅特征api")
@Controller
@RequestMapping("/v2/borrow")
public class EduBorrowController {

    @Autowired
    public IEduBorrowService borrowServe;

    @RequestMapping("/toBorrow")
    public ModelAndView toConsume() throws Exception,
            InterruptedException {
        ModelAndView mv = new ModelAndView("behavior/comBehaviormng");
        return mv;
    }

    /**
     * 1、 借阅书籍排名top10 -- 时间段内的合计
     *
     * @param para
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/bookTopList")
    @ResponseBody
    public PageResult<List<Map<String, Object>>> bookTopListAOP(Params para)
            throws Exception {

        List<Map> list = borrowServe.bookTopList(para);
        return new PageResult(list, list.size());
    }

    /**
     * 1、 借阅书籍排名top10 -- 时间段内的合计
     * <p>
     * 非bootstrap封装的格式
     *
     * @param para
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/bookTop")
    @ResponseBody
    public List<Map> bookTopAOP(Params para) throws Exception {

        return borrowServe.bookTopList(para);
    }

    /**
     * 2、 借阅书籍数量top10 -相同的书，也累计次数
     * <p>
     * 新增了：月平均借阅
     *
     * @param para
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/peopleTopList")
    @ResponseBody
    public PageResult<List<Map<String, Object>>> peopleTopListAOP(Params para)
            throws Exception {
        List<Map> list = borrowServe.peopleTopList(para);
        return new PageResult(list, list.size());
    }

    /**
     * 2、 借阅书籍数量top10 -相同的书，也累计次数
     * <p>
     * 新增了：月平均借阅
     * <p>
     * 非bootstrap封装的格式
     *
     * @param para
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/peopletop")
    @ResponseBody
    public List<Map> peopleTopAOP(Params para) throws Exception {
        return borrowServe.peopleTopList(para);
    }

    /**
     * 3、 借阅频次TOP10 -还得根据选择的时间计算月，然后算出每月频次：
     *
     * @param para
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/avgTopList")
    @ResponseBody
    public PageResult<List<Map<String, Object>>> avgPeopleTopListAOP(Params para)
            throws Exception {
        List<Map> list = borrowServe.avgPeopleTopList(para);
        return new PageResult(list, list.size());
    }

    @ApiOperation(value = "借阅书籍类型", notes = "个人-借阅书籍类型:全部分类", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bdate", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "edate", dataType = "String", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "semester", dataType = "String", value = "学期字段"),
            @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", value = "token编码"),
            @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", required = true, value = "学号"),
            @ApiImplicitParam(paramType = "query", name = "timeframe", dataType = "String", value = "查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年")
    })
    @RequestMapping(value = "/preferenceList")
    @ResponseBody
    public CommResponse preferenceListAOP(Params para) throws Exception {
        return CommResponse.success(borrowServe.preferenceList(para));
    }

    @ApiOperation(value = "借阅书籍类型", notes = "所有顶级分类:选中某个分类：显示下面借书的排名", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bdate", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "edate", dataType = "String", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "semester", dataType = "String", value = "学期字段"),
            @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", value = "token编码"),
            @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", required = true, value = "学号"),
            @ApiImplicitParam(paramType = "query", name = "someCode", dataType = "String", value = "选中分类的code"),
            @ApiImplicitParam(paramType = "query", name = "timeframe", dataType = "String", value = "查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年")
    })
    @RequestMapping(value = "/preferenceListTop")
    @ResponseBody
    public CommResponse preferenceListTopAOP(Params para) throws Exception {
        List<Map<String, Object>> result = borrowServe.preferenceListTop(para);
        if (result == null || result.size() == 0) {
            return CommResponse.success(result);
        }
        if (para.getStart() != null) {
            int size = result.size();
            result = ParamUtils.limitPage(result, para.getStart(), para.getLimit());
            Map<String, Object> map = new HashMap<>();
            map.put("data", result);
            map.put("total", size);
            return CommResponse.success(map);
        }
        return CommResponse.success(result);
    }

    /**
     * 4、 借阅书籍类型 :二级的“工业技术”分类下的分类
     * <p>
     * +++ "dscrp":分类的code;后面根据此按照“someCode”作为参数：查询分类下面的top
     *
     * @param para
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/preferenceListTwo")
    @ResponseBody
    public List preferenceListTwoAOP(Params para) throws Exception {
        return borrowServe.preferenceListTwo(para);
    }

    /**
     * 4、 借阅书籍类型 :二级的“工业技术”分类下的分类:选中某个分类：显示下面借书的排名
     * <p>
     * ++++ someCode选中分类的code
     *
     * @param para
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/preferenceListTwoTop")
    @ResponseBody
    public PageResult<List<Map<String, Object>>> preferenceListTwoTopAOP(Params para) throws Exception {
        List<Map> list = borrowServe.preferenceListTwoTop(para);
        return new PageResult(list, list.size());
    }

    /**
     * 9、借阅行为概况 ：借阅人数、借阅书籍数量、平均保有时长、人均借阅数量
     * <p>
     * 新增了一个上网总时长（timeSum）
     *
     * @param para
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/analysis")
    @ResponseBody
    public Map analysisAOP(Params para) throws Exception {
        return borrowServe.analysis(para);
    }

    @ApiOperation(value = "社群特征", notes = "借阅特征", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code"),
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
    @RequestMapping(value = "/analysisShow")
    @ResponseBody
    public CommResponse analysisShowAOP(Params para) throws Exception {
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        return CommResponse.success(borrowServe.getBorrowFeature(para));
    }

    /**
     *  画像（借阅特征） 个人：借阅人数、借阅书籍数量、人均保有时长、人均借阅数量、人均借阅频次
     *
     * @param para
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "个人画像:借阅特征", notes = "借阅人数、借阅书籍数量、人均保有时长、人均借阅数量、人均借阅频次", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bdate", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "edate", dataType = "String", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "semester", dataType = "String", value = "学期字段"),
            @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", value = "token编码"),
            @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", required = true, value = "学号"),
            @ApiImplicitParam(paramType = "query", name = "timeframe", dataType = "String", value = "查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年")
    })
    @RequestMapping(value = "/student/analysisShow")
    @ResponseBody
    public CommResponse analysisShowBystudentAOP(Params para) throws Exception {
        return CommResponse.success(borrowServe.analysisShow(para));
    }

    @ApiOperation(value = "图书持有时长", notes = "个人-图书持有时长", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bdate", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "edate", dataType = "String", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "semester", dataType = "String", value = "学期字段"),
            @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", value = "token编码"),
            @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", required = true, value = "学号")
    })
    @RequestMapping(value = "/analysison")
    @ResponseBody
    public CommResponse analysisOnAOP(Params para) throws Exception {
        return CommResponse.success(borrowServe.analysisOn(para));
    }

    /**
     * new : 只统计借阅的时间点分布 5、 借阅时段分布-
     * 群体的要按照月平均。个体的不用平均
     */
    @ApiOperation(value = "借阅时间习惯", notes = "个人-借阅时段分布", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bdate", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "edate", dataType = "String", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "semester", dataType = "String", value = "学期字段"),
            @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", value = "token编码"),
            @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", required = true, value = "学号")
    })
    @RequestMapping(value = "/hourlist")
    @ResponseBody
    public CommResponse hourlistAOP(Params para) throws Exception {
        return CommResponse.success(borrowServe.hourList(para));
    }

    @ApiOperation(value = "借阅频次习惯", notes = "按照周几、小时汇总-通过对Java对日期的格式进行再次处理", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code"),
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
    @RequestMapping(value = "/weekHourList")
    @ResponseBody
    public CommResponse weekHourListAOP(Params para) throws Exception {
        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        return CommResponse.success(borrowServe.weekHourList(para));
    }

    /**
     * 7.1、借阅人群分析 -按照｛男、女｝周几:统计人数:去重复
     *
     * @param para
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sexWeekList")
    @ResponseBody
    public Map sexWeekListAOP(Params para) throws Exception {
        return borrowServe.sexWeekList(para);
    }

    /**
     * 7.2、 借阅人群分析 -｛书籍类型｝：top5
     *
     * @param para
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/preferenceTopList")
    @ResponseBody
    public List preferenceTopListAOP(Params para) throws Exception {
        return borrowServe.preferenceTopList(para);
    }

    @ApiOperation(value = "借阅人群分析", notes = "按照｛男、女｝：｛书籍类型｝：top5", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code"),
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
    @RequestMapping(value = "/sexWeekPreferenceTop")
    @ResponseBody
    public CommResponse sexWeekClientTypeList(Params para) throws Exception {
        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        Map<String, Object> listSex = borrowServe.sexWeekList(para);

        if (listSex == null || listSex.size() == 0) {
            return CommResponse.success(null);
        }
        Params param = new Params();
        BeanUtils.copyProperties(para, param);
        Set<String> nameLegend = new HashSet<>();
        if (StringUtils.equals("男", para.getSex()) || null == para.getSex()) {
            param.setSex("0");
            List<Map<String, Object>> manClass = borrowServe.preferenceTopList(param);
            listSex.put("manClass", manClass);
            for (Map<String, Object> man : manClass) {
                nameLegend.add((String) man.get("name"));
            }
            if (StringUtils.equals("男", para.getSex())) {
                listSex.remove("women");
            }
        }
        if (StringUtils.equals("女", para.getSex()) || null == para.getSex()) {
            param.setSex("1");
            List<Map<String, Object>> womenClass = borrowServe.preferenceTopList(param);
            listSex.put("womenClass", womenClass);
            for (Map<String, Object> woman : womenClass) {
                nameLegend.add((String) woman.get("name"));
            }
            if (StringUtils.equals("女", para.getSex())) {
                listSex.remove("man");
            }
        }
        listSex.put("nameLegend", nameLegend);
        return CommResponse.success(listSex);
    }

    @ApiOperation(value = "借阅数量变化趋势", notes = "按照天:统计人数:去重复", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code"),
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
    @RequestMapping(value = "/dayList")
    @ResponseBody
    public CommResponse consumeDayListAOP(Params para) throws Exception {
        if (null != para.getTermtype() && para.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        if (null != para.getEducation() && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        List<Map<String, Object>> listPeople = borrowServe.dayPeopleNum(para);
        List<Map<String, Object>> listBorrow = borrowServe.dayBorrowNum(para);
        if (listPeople == null || listBorrow == null)
            return CommResponse.success(null);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("peopleNum", listPeople);
        resultMap.put("borrowNum", listBorrow);
        return CommResponse.success(resultMap);
    }

    @ApiOperation(value = "借阅类型", notes = "返回一级类目借阅数据的top7+其他", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code"),
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
    @RequestMapping("/types")
    @ResponseBody
    public CommResponse getBorrowTypeAOP(com.ziyun.borrow.vo.Params params) {
        if (null != params.getEducation() && params.getEducation() == 2) {
            return CommResponse.success(null);
        }
        return CommResponse.success(borrowServe.getBorrowType(params));
    }

    /**
     * @api {POST} /api/v2/borrow/secoundtypes
     * @apiName secoundtypes
     * @apiGroup borrow
     * @apiVersion 2.0.0
     * @apiDescription 一级类目返回对应的二级类目
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/borrow/secoundtypes
     * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
     * @apiParam {String} [facultyCode] 院系code
     * @apiParam {String} [majorCode] 专业code
     * @apiParam {String} [classSelect] 班级code多选，用逗号隔开：：班级只有code,没有名称
     * @apiParam {String} [bdate] 开始时间
     * @apiParam {String} [edate] 结束时间
     * @apiParam {String} [semester] 学期字段
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
     * "data":total=171, data=[{name=自动化技术、计算机技术, time=329.11, bookname=C++入门很简单, value=8897, timebytotal=68.97, frequency=132.79}
     * total:总条数 data：数据内容 name：二级类目名称 time：人均持有时长 bookname：借阅次数最高的书籍名称 value：值  timebytotal：人均持有时长除以总人数的 frequency：借阅频次
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping("/secondTypes")
    @ResponseBody
    public CommResponse getSecondBorrowTypeAOP(ParamsStatus params) {
        if (null != params.getEducation() && params.getEducation() == 2) {
            return CommResponse.success(null);
        }
        Map<String, Object> map = new HashedMap();
        if (params.getStart() == null) {
            params.setStart(0);
            params.setLimit(10);
        }
        if (StringUtil.isNotBlank(params.getTypes()) && params.getTypes().contains(",")) {
            String[] tys = params.getTypes().split(",");
            params.setTypeArr(tys);
            params.setTypes(null);
        }
        List<Map<String, Object>> list = borrowServe.getLevelTwoType(params);
        if (null == list || list.size() == 0) {
            return CommResponse.success(null);
        }
        map.put("data", ParamUtils.limitPage(list, params.getStart(), params.getLimit()));
        map.put("total", list.size());
        return CommResponse.success(map);
    }

    //图书排行明细需求修改前。借阅top10书名的学生信息
    @RequestMapping("/bookList")
    @ResponseBody
    public List<Map<String, Object>> getBorrowBookListAOP(Params params) {
        return borrowServe.getBorrowBookList(params);
    }

    /**
     * @api {POST} /api/v2/borrow/bookdetail
     * @apiName bookdetail
     * @apiGroup borrow
     * @apiVersion 2.0.0
     * @apiDescription 借阅图书排行
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/borrow/bookdetail
     * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
     * @apiParam {String} [facultyCode] 院系code
     * @apiParam {String} [majorCode] 专业code
     * @apiParam {String} [classSelect] 班级code多选，用逗号隔开：：班级只有code,没有名称
     * @apiParam {String} [bdate] 开始时间
     * @apiParam {String} [edate] 结束时间
     * @apiParam {String} [semester] 学期字段
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
     * "data":{bookNum=43, BOOK_NAME=白夜行.第2版, avgtimeSum=26.28, personNum=36}
     * 书籍名称：BOOK_NAME；人均保有时长：avgtimeSum；借阅人数：personNum；借阅次数：bookNum
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping("/bookDetail")
    @ResponseBody
    public CommResponse getBorrowBookDetail(Params params) {
        if (null != params.getTermtype() && params.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        if (null != params.getEducation() && params.getEducation() == 2) {
            return CommResponse.success(null);
        }
        List<Map<String, Object>> list = null;
        if (params.getStart() == null) {
            params.setStart(0);
            params.setLimit(10);
            list = borrowServe.getBorrowBookDetail(params);
            return CommResponse.success(list);
        } else {
            list = borrowServe.getBorrowBookDetail(params);
            return CommResponse.success(list);
        }
    }

    @ApiOperation(value = "借阅图书排名", notes = "借阅图书排行列表", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code"),
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
    @RequestMapping("/bookDetailList")
    @ResponseBody
    public CommResponse getBorrowBookDetailListAOP(Params params) {
        if (null != params.getTermtype() && params.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        //如果用户选择了研究生，返回null
        if (params.getEducation() != null && params.getEducation() == 2) {
            return CommResponse.success(null);
        }
        List<Map<String, Object>> list;
        if (params.getStart() == null) {
            params.setStart(0);
            params.setLimit(10);
            list = borrowServe.getBorrowBookDetail(params);
            return CommResponse.success(list);
        } else {
            list = borrowServe.getBorrowBookDetail(params);
            return CommResponse.success(list);
        }
    }

    @ApiOperation(value = "借阅图书排名", notes = "借阅图书排行列表-数量", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code"),
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
    @RequestMapping("/bookDetailSize")
    @ResponseBody
    public CommResponse getBorrowBookDetailsizeAOP(Params params) {
        if (null != params.getTermtype() && params.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        //如果用户选择了研究生，返回null
        if (params.getEducation() != null && params.getEducation() == 2) {
            return CommResponse.success(null);
        }
        int size = borrowServe.getBorrowBookDetailLength(params);
        return CommResponse.success(size);
    }

    /**
     * @api {POST} /api/v2/borrow/peopledetail
     * @apiName peopledetail
     * @apiGroup borrow
     * @apiVersion 2.0.0
     * @apiDescription 借阅数量排行及其明细。不传start则默认top10
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/borrow/peopledetail
     * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
     * @apiParam {String} [facultyCode] 院系code
     * @apiParam {String} [majorCode] 专业code
     * @apiParam {String} [classSelect] 班级code多选，用逗号隔开：：班级只有code,没有名称
     * @apiParam {String} [bdate] 开始时间
     * @apiParam {String} [edate] 结束时间
     * @apiParam {String} [semester] 学期字段
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
     * "data":{name=李加其, value=196, frequency=4.89}, {name=徐昊, value=158, frequency=3.03}]
     * 姓名：name；借阅频次:frequency；借阅数量：value；
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping("/peopleDetail")
    @ResponseBody
    public CommResponse getBorrowPeopleDetail(ParamsStatus params) {
        if (null != params.getTermtype() && params.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        if (null != params.getEducation() && params.getEducation() == 2) {
            return CommResponse.success(null);
        }
        List<Map<String, Object>> list = null;
        if (params.getStart() == null) {
            params.setStart(0);
            params.setLimit(10);
            list = borrowServe.getBorrowPeopleDetail(params);
            return CommResponse.success(list);
        } else {
            list = borrowServe.getBorrowPeopleDetail(params);
            return CommResponse.success(list);
        }
    }

    @ApiOperation(value = "借阅数量排名", notes = "借阅数量排名列表及其明细。不传start则默认top10", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code"),
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
    @RequestMapping("/peopleDetailList")
    @ResponseBody
    public CommResponse getBorrowPeopleDetailListAOP(BorrowParamsVo params) {
        if (null != params.getTermtype() && params.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        //如果用户选择了研究生，返回null
        if (params.getEducation() != null && params.getEducation() == 2) {
            return CommResponse.success(null);
        }
        List<Map<String, Object>> list = null;
        if (params.getStart() == null) {
            params.setStart(0);
            params.setLimit(10);
        }
        list = borrowServe.getBorrowPeopleDetail(params);
        return CommResponse.success(list);
    }

    @ApiOperation(value = "借阅数量排名", notes = "借阅数量排名列表-数量", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code"),
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
    @RequestMapping("/peopleDetailSize")
    @ResponseBody
    public CommResponse getBorrowPeopleDetailSizeAOP(BorrowParamsVo params) {
        if (null != params.getTermtype() && params.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        //如果用户选择了研究生，则返回null
        if (params.getEducation() != null && params.getEducation() == 2) {
            return CommResponse.success(null);
        }
        int size = borrowServe.getBorrowPeopleDetailLength(params);
        return CommResponse.success(size);
    }

    /**
     * @api {POST} /api/v2/borrow/borrowtrend
     * @apiName borrowtrend
     * @apiGroup borrow
     * @apiVersion 2.0.0
     * @apiDescription 借阅数量变化趋势的表
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/borrow/borrowtrend
     * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
     * @apiParam {String} [facultyCode] 院系code
     * @apiParam {String} [majorCode] 专业code
     * @apiParam {String} [classSelect] 班级code多选，用逗号隔开：：班级只有code,没有名称
     * @apiParam {String} [bdate] 开始时间
     * @apiParam {String} [edate] 结束时间
     * @apiParam {String} [semester] 学期字段
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
     * data=[{date=2010-09-27, borrowNum=2, peopleNum=1}]
     * date：时间；borrowNum：借阅数量；peopleNum：借阅人数
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping("/borrowTrend")
    @ResponseBody
    public CommResponse getBorrowVariationTrendAOP(Params params) {
        if (null != params.getTermtype() && params.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        if (params.getStart() == null) {
            params.setStart(0);
            params.setLimit(10);
        }

        List<Map<String, Object>> list = borrowServe.getBorrowVariationTrend(params);
        return CommResponse.success(list);
    }

    /**
     * @api {POST} /api/v2/borrow/borrowtrendsize
     * @apiName borrowtrendsize
     * @apiGroup borrow
     * @apiVersion 2.0.0
     * @apiDescription 借阅数量变化趋势的表的长度
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/borrow/borrowtrendsize
     * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
     * @apiParam {String} [facultyCode] 院系code
     * @apiParam {String} [majorCode] 专业code
     * @apiParam {String} [classSelect] 班级code多选，用逗号隔开：：班级只有code,没有名称
     * @apiParam {String} [bdate] 开始时间
     * @apiParam {String} [edate] 结束时间
     * @apiParam {String} [semester] 学期字段
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
     * data=1051]
     * data 总条数
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping("/borrowTrendSize")
    @ResponseBody
    public CommResponse getBorrowVariationTrendSizeAOP(Params params) {
        if (null != params.getTermtype() && params.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        int size = borrowServe.getBorrowtrendLength(params);
        return CommResponse.success(size);
    }

    /**
     * @api {POST} /api/v2/borrow/borrowfrequency
     * @apiName borrowfrequency
     * @apiGroup borrow
     * @apiVersion 2.0.0
     * @apiDescription 借阅数量变化趋势的表的长度
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/borrow/borrowfrequency
     * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
     * @apiParam {String} [facultyCode] 院系code
     * @apiParam {String} [majorCode] 专业code
     * @apiParam {String} [classSelect] 班级code多选，用逗号隔开：：班级只有code,没有名称
     * @apiParam {String} [bdate] 开始时间
     * @apiParam {String} [edate] 结束时间
     * @apiParam {String} [semester] 学期字段
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
     * data=[{maxTime=16:00-17:00, avg=28, week=周一, sum=10041}]
     * week:周几对应的中文；sum：总借阅书籍；avg：平均借阅书籍；maxTime：最大借阅时间
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping("/borrowFrequency")
    @ResponseBody
    public CommResponse getBorrowFrequencyAOP(Params params) throws Exception {
        if (null != params.getTermtype() && params.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        return CommResponse.success(borrowServe.getWeekBorrowTotal(params));
    }

    /**
     * @api {POST} /api/v2/borrow/detailsbysex
     * @apiName detailsbysex
     * @apiGroup borrow
     * @apiVersion 2.0.0
     * @apiDescription 借阅数量变化趋势的表的长度
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/borrow/detailsbysex
     * @apiParam {String} [schoolCode] 校区code：：'学籍类型--slg 苏理工 jkd 江科大
     * @apiParam {String} [facultyCode] 院系code
     * @apiParam {String} [majorCode] 专业code
     * @apiParam {String} [classSelect] 班级code多选，用逗号隔开：：班级只有code,没有名称
     * @apiParam {String} [bdate] 开始时间
     * @apiParam {String} [edate] 结束时间
     * @apiParam {String} [semester] 学期字段
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
     * data={week=周一, nv=3, sum=10040, nan=5, time=209, bookname=高等数学同步辅导:配同济五版, avgsum=8, day=1, timebytotal=63}]
     * sum：借阅次数；bookname：借阅次数最高图书；time：人均持有时长；week：中文星期;nan:男的数据；nv：女的数据;avgsum：男女平均数量 timebytotal：持有时长除以总人数的
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping("/detailSbySex")
    @ResponseBody
    public CommResponse getBorrowPeopleAOP(Params params) throws Exception {
        if (null != params.getTermtype() && params.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        return CommResponse.success(borrowServe.getBorrowPeople(params));
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

    @ApiOperation(value = "借阅特征", notes = "借阅学生列表", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code"),
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
    @RequestMapping("/borrowStudentList")
    @ResponseBody
    public CommResponse getBorrowStudentListAOP(Params params) throws Exception {
        if (null != params.getTermtype() && params.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        //如果用户选择了研究生，返回null
        if (params.getEducation() != null && params.getEducation() == 2) {
            return CommResponse.success(null);
        }
        List<Map<String, Object>> list;
        if (params.getStart() == null) {
            params.setStart(0);
            params.setLimit(10);
            list = borrowServe.getBorrowStudentList(params);
            return CommResponse.success(list);
        } else {
            list = borrowServe.getBorrowStudentList(params);
            return CommResponse.success(list);
        }
    }

    @ApiOperation(value = "借阅特征", notes = "借阅学生列表-数量", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "schoolCode", dataType = "String", value = "校区code"),
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
    @RequestMapping("/borrowStudentCount")
    @ResponseBody
    public CommResponse getBorrowStudentCountAOP(Params params) throws Exception {
        if (null != params.getTermtype() && params.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        //如果用户选择了研究生，返回null
        if (params.getEducation() != null && params.getEducation() == 2) {
            return CommResponse.success(null);
        }
        Long size = borrowServe.getBorrowStudentCount(params);
        return CommResponse.success(size);
    }
}
