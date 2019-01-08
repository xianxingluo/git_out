package com.ziyun.basic.controller;

import com.ziyun.basic.entity.OwnOrgStudent;
import com.ziyun.basic.entity.OwnOrgStudentType;
import com.ziyun.basic.server.IAcademicServer;
import com.ziyun.basic.server.IEcardRecConsumeServer;
import com.ziyun.basic.server.StudentServer;
import com.ziyun.basic.tools.LoginManager;
import com.ziyun.basic.tools.ParamUtils;
import com.ziyun.basic.vo.AcademicParams;
import com.ziyun.basic.vo.Params;
import com.ziyun.basic.vo.ParamsStatus;
import com.ziyun.utils.cache.TokenCasheManager;
import com.ziyun.utils.common.StringUtils;
import com.ziyun.utils.requests.CommResponse;
import com.ziyun.utils.requests.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/v2/student")
@Api(tags = "学业微服务", description = "学生基础行为相关api")
public class StudentController {

    @Autowired
    public StudentServer studentServer;

    @Autowired
    private IEcardRecConsumeServer consumeServe;

    @Autowired
    private IAcademicServer academicServer;

    /**
     * 学生社群画像首页
     *
     * @param para
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView toIndex(Params para) throws Exception {
        ModelAndView mv = new ModelAndView("index/indexmng");
        return mv;
    }

    @RequestMapping(value = "/community/source", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "基本信息-社群画像-生源地分布", notes = "生源地分布")
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
    public CommResponse sourceAOP(Params para) throws IOException {
        //学历选择研究生，返回null
        if (para.getEducation() != null && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        List<LinkedHashMap<String, Object>> list = studentServer.getSource(para);
        if (null == list || list.size() == 0) {
            return CommResponse.success(null);
        }
        if (para.getStart() != null) {
            int size = list.size();
            List resultList = ParamUtils.limitPage(list, para.getStart(), para.getLimit());
            Map<String, Object> map = new HashedMap();
            map.put("data", resultList);
            map.put("total", size);
            return CommResponse.success(map);
        }
        return CommResponse.success(list);
    }

    @ApiOperation(value = "基本信息-来源分布-根据省份获取学生列表", notes = "根据省份获取学生列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", value = "班级编号"),
            @ApiImplicitParam(name = "bdate", paramType = "query", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(name = "edate", paramType = "query", dataType = "String", value = "结束时间"),
            @ApiImplicitParam(name = "semester", paramType = "query", dataType = "String", value = "学期字段"),
            @ApiImplicitParam(name = "timeframe", paramType = "query", dataType = "String", value = "查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", value = " 1-->贫困生"),
            @ApiImplicitParam(name = "start", paramType = "query", dataType = "Integer", value = "分页参数：从0开始"),
            @ApiImplicitParam(name = "limit", paramType = "query", dataType = "Integer", value = "分页参数：每页多少条数据"),
    })
    @RequestMapping(value = "/community/sourceStudentList", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse sourceStudentListAOP(ParamsStatus param) throws IOException {
        List<Map<String, Object>> list = studentServer.sourceStudentList(param);
        return CommResponse.success(new PageResult(limitPage(list, param.getStart(), param.getLimit()), list.size()));
    }


    @ApiOperation(value = "社群画像-来源分布", notes = "社群画像-来源分布")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", value = "校区code：学籍类型--slg 苏理工 jkd 江科大"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", value = "班级编号"),
            @ApiImplicitParam(name = "bdate", paramType = "query", dataType = "String", value = "开始时间"),
            @ApiImplicitParam(name = "edate", paramType = "query", dataType = "String", value = "结束时间"),
            @ApiImplicitParam(name = "semester", paramType = "query", dataType = "String", value = "学期字段"),
            @ApiImplicitParam(name = "timeframe", paramType = "query", dataType = "String", value = "查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", value = " 1-->贫困生"),
            @ApiImplicitParam(name = "start", paramType = "query", dataType = "Integer", value = "分页参数：从0开始"),
            @ApiImplicitParam(name = "limit", paramType = "query", dataType = "Integer", value = "分页参数：每页多少条数据"),
    })
    @RequestMapping(value = "/community/sourceStudentListCount", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse sourceStudentListCountAOP(ParamsStatus para) throws IOException {
        int size = studentServer.sourceStudentListCount(para);
        return CommResponse.success(size);
    }

    /**
     * 学生画像-社群画像-借阅习惯
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/community/borrowHabits", method = RequestMethod.POST)
    @ResponseBody
    public List<LinkedHashMap<String, Object>> borrowHabitsAOP(Params para) throws IOException {
        return studentServer.getBorrowhabits(para);
    }

    /**
     * 学生画像-社群画像-网络
     * // *********平台未调用，先隐藏**********
     * @return
     * @throws IOException
     */
    /*@RequestMapping("/community/internet")
    @ResponseBody
    public Map<String, Object> internetAOP(Params para) {

        Map<String, List<HotSpotVo>> internet = studentServer.internet(para, "");
        Map<String, Integer> types = new HashMap<>();
        List<String> weeks = new ArrayList<>();
        // 類型 set //類型占比
        for (String week : internet.keySet()) {
            weeks.add(week);
            for (HotSpotVo HotSpotVo : internet.get(week)) {
                Integer nums = types.get(HotSpotVo.getType());
                if (nums == null) {
                    types.put(HotSpotVo.getType(), HotSpotVo.getNum());
                } else {
                    types.put(HotSpotVo.getType(), (types.get(HotSpotVo.getType()) + HotSpotVo.getNum()));
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("weekProportion", internet);
        map.put("typesProportion", types);
        map.put("types", types.keySet());
        map.put("weeks", weeks);
        return map;
    }*/

    /**
     * 学生画像-社群画像-消费偏好
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/community/consume", method = RequestMethod.POST)
    @ResponseBody
    public List<LinkedHashMap<String, Object>> consumeAOP(Params para) throws IOException {
        return studentServer.getConsume(para);
    }

    @ApiOperation(value = "基本信息-社群画像-来源分布表", notes = "来源分布表")
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
            @ApiImplicitParam(name = "start", paramType = "query", dataType = "Integer", value = "分页参数：从0开始"),
            @ApiImplicitParam(name = "limit", paramType = "query", dataType = "Integer", value = "分页参数：每页多少条数据"),
    })
    @RequestMapping(value = "/community/sourceList", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse sourceListAOP(Params para) throws IOException {
        //学历选择研究生，返回null
        if (para.getEducation() != null && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        List<LinkedHashMap<String, Object>> list = studentServer.getSource(para);
        if (null == list || list.size() == 0)
            return CommResponse.success(null);
        if (para.getStart() != null) {
            int size = list.size();
            List resultList = ParamUtils.limitPage(list, para.getStart(), para.getLimit());
            Map<String, Object> map = new HashedMap();
            map.put("data", resultList);
            map.put("total", size);
            return CommResponse.success(map);
        }
        return CommResponse.success(list);
    }

    /**
     * 学生画像-社群画像-学院专业关系
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/community/org", method = RequestMethod.POST)
    @ResponseBody
    public List<LinkedHashMap<String, Object>> orgTreeAOP(Params para) throws IOException {
        return studentServer.getOrgTree();
    }

    /**
     * 学生画像-社群画像-专业热度
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/community/major", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> majorAOP(Params para) throws IOException {
        List<LinkedHashMap<String, Object>> majors = studentServer.getMajor(para);
        // 专业(Y轴)
        List<String> majorY = new ArrayList<>();
        // 时间 选项轴
        Set<String> year = new HashSet<>();
        // MAP<专业List<map>>>
        for (LinkedHashMap<String, Object> map : majors) {
            // majorY.add(((String) map.get("major_name")).replace(" ", ""));
            if (Collections.frequency(majorY, map.get("major_name")) < 1)
                majorY.add(((String) map.get("major_name")));
            year.add((String) map.get("LEVEL"));
        }
        // 年排序
        List YearList = new ArrayList(year);
        Collections.sort(YearList);

        Map<String, Object> map = new HashMap<>();
        map.put("majors", majors);
        map.put("YearList", YearList);
        map.put("majorY", majorY);
        return map;
    }

    // 社群特征

    /**
     * 学生画像-社群画像-社群特征
     * // *********平台未调用，先隐藏**********
     * @return
     * @throws Exception
     */
    /*@RequestMapping("/community/features")
    @ResponseBody
    public Map<String, Object> featuresAOP(ParamsStatus para, String endDate, String StartDate) throws Exception {
        Map<String, Object> map = new HashMap<>();
        // 基本信息
        // 本省人为主 90后为主 有少数名族 男女比例失衡 （学籍表）
        Map<String, Object> status = studentServer.getStatus(para);
        map.put("status", status);

        // 借阅特征
        // 生源地 出生年份 性别比例
//		Map borrow = borrowServe.analysis(para);
//		map.put("borrow", borrow);

        // 上网特征
        // 上网总时长 人均上网时长 上网高峰时段 上网内容喜好
        Map<String, Object> hotsport = studentServer.getHotsport(para);
        map.put("hotsport", hotsport);

        // 消费特征
        Map consume = consumeServe.sumCollect(para);
        List<Map<String, Object>> list = consumeServe.preferenceList(para);
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
        // 奖惩特征
        // 获奖人数 获奖次数 处罚人数 处罚次数
        Map<String, Object> punishandreward = studentServer.getPunishAndReward(para);
        map.put("punishandreward", punishandreward);

        return map;
    }*/

    // 社群特征


    /**
     * 学生画像-社群画像-社群特征-学业分析
     * 学生画像-社群画像-社群特征-学业分析
     * // *********平台未调用，先隐藏**********
     * @return
     * @throws IOException
     */
  /*  @RequestMapping("/community/featuresAcademic")
    @ResponseBody
    public Map<String, String> featuresAcademicAOP(AcademicParams para) throws IOException {

        //选修5门课程 考试合格率80% 选修完成率70%
        Map<String, String> result = new HashMap<>();
        Map<String, Object> academic = academicServer.getAcademicLabel(para);
        List grades = (List) academic.get("ele_num");
        int num = 0;
        for (Object object : grades) {
            Map gradeMap = (Map) object;
            num += (Long) gradeMap.get("num");
        }
        result.put("pass", academic.get("pass") + "%");
        result.put("credit_pass", academic.get("credit_pass") + "%");
        result.put("number", num + "");
        result.put("rebuild_num", academic.get("rebuild_num") + "");
        result.put("average_credit_point", academic.get("average_credit_point") + "");
        return result;
    }
*/

    /**
     * 学生社群画像-学生画像-男女比例
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/community/countBySex", method = RequestMethod.POST)
    @ResponseBody
    public List<LinkedHashMap<String, Object>> countBySexAOP(ParamsStatus param) throws IOException {
        return studentServer.countBySex(param);
    }


    @RequestMapping(value = "/community/sexRatio", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "基本信息-学生画像-男女比例", notes = "男女比例")
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
    public CommResponse getCountBySexAOP(Params param) throws IOException {
        //学历选择研究生，返回null
        if (param.getEducation() != null && param.getEducation() == 2) {
            return CommResponse.success(new String[]{});
        }
        List<Map<String, Object>> list = studentServer.getCountBySex(param);
        if (CollectionUtils.isEmpty(list)) {
            return CommResponse.success(new String[]{});
        }
        return CommResponse.success(list);
    }

    @ApiOperation(value = "学生画像-男女比例-学生列表", notes = "学生列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", value = "校区code：学籍类型--slg 苏理工 jkd 江科大"),
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
            @ApiImplicitParam(name = "start", paramType = "query", dataType = "Integer", value = "分页参数：从0开始"),
            @ApiImplicitParam(name = "limit", paramType = "query", dataType = "Integer", value = "分页参数：每页多少条数据"),
    })
    @RequestMapping(value = "/community/sexRatioStudentList", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse sexRatioStudentListAOP(ParamsStatus param) throws IOException {
        List<Map<String, Object>> list = studentServer.sexRatioStudentList(param);
        return CommResponse.success(new PageResult(limitPage(list, param.getStart(), param.getLimit()), list.size()));
    }

    @ApiOperation(value = "学生画像-男女比例-学生列表总长度", notes = "学生列表总长度")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", value = "校区code：学籍类型--slg 苏理工 jkd 江科大"),
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
            @ApiImplicitParam(name = "start", paramType = "query", dataType = "Integer", value = "分页参数：从0开始"),
            @ApiImplicitParam(name = "limit", paramType = "query", dataType = "Integer", value = "分页参数：每页多少条数据"),
    })
    @RequestMapping(value = "/community/sexRatioStudentListCount", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse sexRatioStudentListCountAOP(ParamsStatus param) throws IOException {
        int size = studentServer.sexRatioStudentListCount(param);
        return CommResponse.success(size);
    }

    /**
     * @api {POST} /api/v2/student/community/sexRatioList
     * @apiName sexRatioList
     * @apiGroup student
     * @apiVersion 2.0.0
     * @apiDescription 学生画像-男女比例
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/student/community/sexRatioList
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
     * "data":{nan_ratio=72%, nv_ratio=28%, grade=第一学期, nv=2213, nan=5682, sum=7895}}
     * nan_ratio:男生比例  nv_ratio：女生比例 grade：学期 nv：女生数量 nan：男生数量 sum：总数量
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping(value = "/community/sexRatioList", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse getCountBySexListAOP(Params param) throws IOException {
        return CommResponse.success(studentServer.getCountBySex(param));
    }

    @RequestMapping(value = "/community/countByBirthday", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "基本信息-学生画像-年龄分布", notes = "年龄分布")
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
    public CommResponse countByBirthdayAOP(ParamsStatus para) throws IOException {
        //学历选择研究生，返回null
        if (para.getEducation() != null && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        List<LinkedHashMap<String, Object>> list = studentServer.countByBirthday(para);
        if (CollectionUtils.isEmpty(list)) {
            return CommResponse.success(null);
        }
        if (para.getStart() != null) {
            int size = list.size();
            List resultList = ParamUtils.limitPage(list, para.getStart(), para.getLimit());
            Map<String, Object> map = new HashedMap();
            map.put("data", resultList);
            map.put("total", size);
            return CommResponse.success(map);
        }
        return CommResponse.success(list);
    }

    @ApiOperation(value = "学生画像-生日比例-学生列表", notes = "生日比例学生列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", value = "校区code：学籍类型--slg 苏理工 jkd 江科大"),
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
            @ApiImplicitParam(name = "start", paramType = "query", dataType = "Integer", value = "分页参数：从0开始"),
            @ApiImplicitParam(name = "limit", paramType = "query", dataType = "Integer", value = "分页参数：每页多少条数据"),
    })
    @RequestMapping(value = "/community/countByBirthdayStudentList", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse countByBirthdayStudentListAOP(ParamsStatus para) throws IOException {
        //学历选择研究生，返回null
        if (para.getEducation() != null && para.getEducation() == 2) {
            return CommResponse.success(new String[]{});
        }
        List<Map<String, Object>> list = studentServer.countByBrithdryStudentList(para);
        if (CollectionUtils.isEmpty(list)) {
            return CommResponse.success(new String[]{});
        }
        return CommResponse.success(new PageResult(limitPage(list, para.getStart(), para.getLimit()), list.size()));
    }

    @ApiOperation(value = "学生画像-生日比例-学生列表总长度", notes = "生日比例学生列表总长度")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", value = "校区code：学籍类型--slg 苏理工 jkd 江科大"),
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
            @ApiImplicitParam(name = "start", paramType = "query", dataType = "Integer", value = "分页参数：从0开始"),
            @ApiImplicitParam(name = "limit", paramType = "query", dataType = "Integer", value = "分页参数：每页多少条数据"),
    })
    @RequestMapping(value = "/community/countByBirthdayStudentListCount", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse countByBirthdayStudentListCountAOP(ParamsStatus para) throws IOException {
        int size = studentServer.countByBrithdryStudentListCount(para);
        return CommResponse.success(size);
    }

    /**
     * @api {POST} /api/v2/student/community/countByBrithdryList
     * @apiName countByBrithdryList
     * @apiGroup student
     * @apiVersion 2.0.0
     * @apiDescription 学生画像-生日比例表
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/student/community/countByBrithdryList
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
     * "data":{num=1, queryType=1987, ratio=0.01%}}
     * queryType 年份 ratio：百分比 num：数量
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping(value = "/community/countByBirthdayList", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse countByBirthdayListAOP(ParamsStatus para) throws IOException {
        List<LinkedHashMap<String, Object>> list = studentServer.countByBirthday(para);
        if (null == list || list.size() == 0)
            return CommResponse.success(null);
        if (para.getStart() != null) {
            int size = list.size();
            List resultList = ParamUtils.limitPage(list, para.getStart(), para.getLimit());
            Map<String, Object> map = new HashedMap();
            map.put("data", resultList);
            map.put("total", size);
            return CommResponse.success(map);
        }
        return CommResponse.success(list);
    }

    /**
     * @api {POST} /api/v2/student/community/scholarshipList
     * @apiName scholarshipList
     * @apiGroup student
     * @apiVersion 2.0.0
     * @apiDescription 获奖类型对应表的分页
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/student/community/scholarshipList
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
     * "data":{name=薛磊, major_name=工商管理(苏理), outid=1142818311, year=2015, type=人民奖学金, money=800}}
     * name:姓名  major_name 专业 type：获奖类型 money：获奖金额 outid：学号 year：获奖年限
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping(value = "/community/scholarshipList", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse scholarshipListAOP(ParamsStatus para) throws IOException {
        //如果选择为第一，二学期，是没有数据的（产品规定的）
        if (para != null) {
            if (para.getTermNum() != null && (para.getTermNum() == 1 || para.getTermNum() == 2)) {
                return CommResponse.success(null);
            }
            if (StringUtils.isNotBlank(para.getEdate()) && StringUtils.isNotBlank(para.getBdate())) {
                para.setEdate(para.getEdate().substring(0, 4));
                para.setBdate(para.getBdate().substring(0, 4));
            }
        }
        List<LinkedHashMap<String, Object>> list = studentServer.scholarshipPageList(para);
        return CommResponse.success(list);
    }

    /**
     * @api {POST} /api/v2/student/community/scholarshipListsize
     * @apiName scholarshipListsize
     * @apiGroup student
     * @apiVersion 2.0.0
     * @apiDescription 获奖类型对应表的分页
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/student/community/scholarshipListsize
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
     * "data":100
     * data：总条数
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping(value = "/community/scholarshipListSize", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse scholarshipListSizeAOP(ParamsStatus param) throws IOException {
        //如果选择为第一，二学期，是没有数据的（产品规定的）m
        if (param != null) {
            if (param.getTermNum() != null && (param.getTermNum() == 1 || param.getTermNum() == 2)) {
                return CommResponse.success(null);
            }
            if (StringUtils.isNotBlank(param.getEdate()) && StringUtils.isNotBlank(param.getBdate())) {
                param.setEdate(param.getEdate().substring(0, 4));
                param.setBdate(param.getBdate().substring(0, 4));
            }
        }
        Long count = studentServer.scholarshipPageListLength(param);
        return CommResponse.success(count);
    }


    /**
     * @api {POST} /api/v2/student/studentscholarship
     * @apiName studentscholarship
     * @apiGroup student
     * @apiVersion 2.0.0
     * @apiDescription 个人的获奖情况的分页
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/student/studentscholarship
     * @apiParam {String} [bdate] 开始时间
     * @apiParam {String} [edate] 结束时间
     * @apiParam {String} [semester] 学期字段
     * @apiParam {Integer} [start] 分页参数：从0开始
     * @apiParam {Integer} [limit] 分页参数：每页多少条数据
     * @apiParam {String} [timeframe] 查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年
     * @apiParam {String} [token] token 编码
     * @apiParam {String} [outid] 学号
     * @apiParamExample {json} 请求例子:
     * {
     * "outid": "111"
     * }
     * @apiSuccess (200) {String} message 提示信息code 根据code去查找错误提示信息
     * @apiSuccess (200) {int} statusCode 200
     * @apiSuccessExample {json} 返回样例:
     * {
     * "statusCode": 200,
     * "message": "提示信息code 根据code去查找错误提示信息"
     * "data":{name=薛磊, major_name=工商管理(苏理), outid=1142818311, year=2015, type=人民奖学金, money=800}}
     * name:姓名  major_name 专业 type：获奖类型 money：获奖金额 outid：学号 year：获奖年限
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping(value = "/studentScholarship", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse studentScholarshipListAOP(ParamsStatus para) throws IOException {
        if (StringUtils.isNotBlank(para.getEdate()) && StringUtils.isNotBlank(para.getBdate())) {
            para.setStartDate(para.getBdate());
            para.setEndDate(para.getEdate());
            para.setEdate(para.getEdate().substring(0, 4));
            para.setBdate(para.getBdate().substring(0, 4));
        }
        List<LinkedHashMap<String, Object>> list = studentServer.scholarshipPageList(para);
        return CommResponse.success(list);
    }

    /**
     * @api {POST} /api/v2/student/studentscholarshipsize
     * @apiName studentscholarshipsize
     * @apiGroup student
     * @apiVersion 2.0.0
     * @apiDescription 个人获奖类型对应表的分页长度
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/student/studentscholarshipsize
     * @apiParam {String} [bdate] 开始时间
     * @apiParam {String} [edate] 结束时间
     * @apiParam {String} [semester] 学期字段
     * @apiParam {Integer} [start] 分页参数：从0开始
     * @apiParam {Integer} [limit] 分页参数：每页多少条数据
     * @apiParam {String} [token] token 编码
     * @apiParam {String} [outid] 学号
     * @apiParamExample {json} 请求例子:
     * {
     * "outid": "111"
     * }
     * @apiSuccess (200) {String} message 提示信息code 根据code去查找错误提示信息
     * @apiSuccess (200) {int} statusCode 200
     * @apiSuccessExample {json} 返回样例:
     * {
     * "statusCode": 200,
     * "message": "提示信息code 根据code去查找错误提示信息"
     * "data":100
     * data：总条数
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping(value = "/studentScholarshipSize", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse studentScholarshipListSizeAOP(ParamsStatus para) throws IOException {
        if (StringUtils.isNotBlank(para.getEdate()) && StringUtils.isNotBlank(para.getBdate())) {
            para.setEdate(para.getEdate().substring(0, 4));
            para.setBdate(para.getBdate().substring(0, 4));
        }
        Long count = studentServer.scholarshipPageListLength(para);
        return CommResponse.success(count);
    }

    /**
     * @api {POST} /api/v2/student/community/punishList
     * @apiName punishList
     * @apiGroup student
     * @apiVersion 2.0.0
     * @apiDescription 惩罚列表中的分页内容
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/student/community/punishList
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
     * @apiParam {String} [punishType] 处罚类型
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
     * "data":[{outid=1445522207, major_name=机械设计制造及其自动化, name=谷天山, class_name=14张家港, punish_type=留校察看, punish_reasion=在大学物理考试中夹带纸条, punish_date=2015-8-31,class_code=1522198021}]
     * major_name：专业  name：姓名 class_code 班级 punish_type：处罚类型 punish_reasion：处罚原因 punish_date：处罚时间
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping(value = "/community/punishList", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse punishListAOP(ParamsStatus para) throws IOException {
        List<LinkedHashMap<String, Object>> list = studentServer.punishPageList(para);
        return CommResponse.success(list);
    }

    /**
     * @api {POST} /api/v2/student/community/punishListsize
     * @apiName punishListsize
     * @apiGroup student
     * @apiVersion 2.0.0
     * @apiDescription 惩罚列表中的分页的总长度
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/student/community/punishListsize
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
     * "data":62}
     * data对应的总长度
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping(value = "/community/punishListSize", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse punishListSizeAOP(ParamsStatus para) throws IOException {
        Long count = studentServer.punishPageListLength(para);
        return CommResponse.success(count);
    }

    /**
     * @api {POST} /api/v2/student/studnetpunish
     * @apiName studnetpunish
     * @apiGroup student
     * @apiVersion 2.0.0
     * @apiDescription 个人 惩罚列表中的分页内容
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/student/studnetpunish
     * @apiParam {String} [bdate] 开始时间
     * @apiParam {String} [edate] 结束时间
     * @apiParam {String} [semester] 学期字段
     * @apiParam {Integer} [start] 分页参数：从0开始
     * @apiParam {Integer} [limit] 分页参数：每页多少条数据
     * @apiParam {String} [punishType] 处罚类型
     * @apiParam {String} [outid] 学号
     * @apiParam {String} [token] token编码
     * @apiParamExample {json} 请求例子:
     * {
     * "outid": "111"
     * }
     * @apiSuccess (200) {String} message 提示信息code 根据code去查找错误提示信息
     * @apiSuccess (200) {int} statusCode 200
     * @apiSuccessExample {json} 返回样例:
     * {
     * "statusCode": 200,
     * "message": "提示信息code 根据code去查找错误提示信息"
     * "data":[{outid=1445522207, major_name=机械设计制造及其自动化, name=谷天山, class_name=14张家港, punish_type=留校察看, punish_reasion=在大学物理考试中夹带纸条, punish_date=2015-8-31,class_code=1522198021}]
     * major_name：专业  name：姓名 class_code 班级 punish_type：处罚类型 punish_reasion：处罚原因 punish_date：处罚时间
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping(value = "/studentPunish", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse studentPunishListAOP(ParamsStatus para) throws IOException {
        List<LinkedHashMap<String, Object>> list = studentServer.punishPageList(para);
        return CommResponse.success(list);
    }

    /**
     * @api {POST} /api/v2/student/studnetpunishsize
     * @apiName studnetpunishsize
     * @apiGroup student
     * @apiVersion 2.0.0
     * @apiDescription 个人 惩罚列表中的分页的总长度
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/student/studnetpunishsize
     * @apiParam {String} [bdate] 开始时间
     * @apiParam {String} [edate] 结束时间
     * @apiParam {String} [semester] 学期字段
     * @apiParam {Integer} [start] 分页参数：从0开始
     * @apiParam {Integer} [limit] 分页参数：每页多少条数据
     * @apiParam {String} [outid] 学号
     * @apiParam {String} [token] token编码
     * @apiParamExample {json} 请求例子:
     * {
     * "outid": "1111"
     * }
     * @apiSuccess (200) {String} message 提示信息code 根据code去查找错误提示信息
     * @apiSuccess (200) {int} statusCode 200
     * @apiSuccessExample {json} 返回样例:
     * {
     * "statusCode": 200,
     * "message": "提示信息code 根据code去查找错误提示信息"
     * "data":62}
     * data对应的总长度
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping(value = "/studentPunishSize", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse studentPunishListSizeAOP(ParamsStatus para) throws IOException {
        Long count = studentServer.punishPageListLength(para);
        return CommResponse.success(count);
    }
    // -------------------------个人画像---------------------------------------------------

    /**
     * 学生列表
     *
     * @param para
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pageStudent", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "基本信息-学生列表", notes = "学生列表")
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
    public CommResponse getPageStudentAOP(Params para) throws Exception {
        //学历选择研究生，返回null
        if (para.getEducation() != null && para.getEducation() == 2) {
            return CommResponse.success(null);
        }
        List<LinkedHashMap<String, Object>> list = studentServer.getPageStudent(para);
        List<LinkedHashMap<String, Object>> result = new ArrayList<LinkedHashMap<String, Object>>();

        Set<String> menusCache = TokenCasheManager.getMenusCache(LoginManager.getToken());
        if (menusCache.contains("show_complete_idcard")) {
            return CommResponse.success(list);
        }
        //【admin】和【校长】可以查看身份证号码。（show_complete_idcard 权限）
        if (null != list && list.size() > 0) {
            for (LinkedHashMap<String, Object> map : list) {
                if (map.containsKey("idcard_no") && null != map.get("idcard_no")) {
                    String idcard_no = String.valueOf(map.get("idcard_no"));
                    if (idcard_no.length() > 4) {
                        idcard_no = idcard_no.substring(0, idcard_no.length() - 4) + "****";
                        map.put("idcard_no", idcard_no);
                    }
                }
                result.add(map);
            }
        }
        return CommResponse.success(result);
    }

    @RequestMapping(value = "/studentsCount", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "基本信息-学生列表总长度", notes = "学生列表总长度")
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
    public CommResponse getStudentsCountAOP(Params params) {
        //学历选择研究生，返回null
        if (params.getEducation() != null && params.getEducation() == 2) {
            return CommResponse.success(null);
        }
        return CommResponse.success(studentServer.countStudent(params));
    }


    /**
     * 用户特征
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/personal/characteristic", method = RequestMethod.POST)
    @ResponseBody
    public List<LinkedHashMap<String, Object>> characteristicAOP(Params para) throws IOException {
        return studentServer.getMajor(para);
    }

    /**
     * 学生档案
     *
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "基本信息-个人画像-学生档案", notes = "学生档案", httpMethod = "POST")
    @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", required = true, value = "学号")
    @RequestMapping(value = "/personal/archive", method = RequestMethod.POST)
    @ResponseBody
    public List<LinkedHashMap<String, Object>> archiveAOP(Params para) throws IOException {
        List<LinkedHashMap<String, Object>> list = studentServer.getArchive(para);
        List<LinkedHashMap<String, Object>> result = new ArrayList<>();

        Set<String> menusCache = TokenCasheManager.getMenusCache(LoginManager.getToken());
        if (menusCache.contains("show_complete_idcard")) {
            return list;
        }
        //【admin】和【校长】可以查看身份证号码。（show_complete_idcard 权限）
        if (null != list && list.size() > 0) {
            for (LinkedHashMap<String, Object> map : list) {
                if (map.containsKey("idcard_no") && null != map.get("idcard_no")) {
                    String idcard_no = String.valueOf(map.get("idcard_no"));
                    if (idcard_no.length() > 4) {
                        idcard_no = idcard_no.substring(0, idcard_no.length() - 4) + "****";
                        map.put("idcard_no", idcard_no);
                    }
                }
                result.add(map);
            }
        }
        return result;
    }

    /**
     * 学业情况 /academic/complex/getCreditSituation?outid=1445742210
     *
     * @return
     * @throws IOException
     */
    /*
     * @RequestMapping(value ="/personal/learning")
     *
     * @ResponseBody public List<LinkedHashMap<String, Object>> learning(Params
     * para) throws IOException{ String outid="1445742210"; return
     * studentServer.getArchive(outid); }
     */

    /**
     * /student/personal/borrow?outid=1345732205 借阅习惯
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/personal/borrow", method = RequestMethod.POST)
    @ResponseBody
    public List<LinkedHashMap<String, Object>> borrowAOP(Params para) throws IOException {
        return studentServer.getBorrowhabits(para);
    }

    /**
     * 学生社群画像-学生画像-上网偏好
     * // *********平台未调用，先隐藏**********
     * @return
     * @throws IOException
     */
    /*@RequestMapping(value ="/personal/internetSingle")
    @ResponseBody
    public Map<String, List<HotSpotVo>> internetSingleAOP(Params para) throws IOException {
        return studentServer.internet(para, "Single");
    }*/

    /**
     * 学生画像-社群画像-社群特征
     * // *********平台未调用，先隐藏**********
     *
     * @return
     * @throws Exception
     */
    /*@RequestMapping(value ="/personal/features")
    @ResponseBody
    public Map<String, Object> featureAOP(ParamsStatus para, String endDate, String StartDate) throws Exception {
        Map<String, Object> map = new HashMap<>();
        // 基本信息

        // 借阅特征
        // 生源地 出生年份 性别比例
//		Map borrow = borrowServe.analysis(para);
//		map.put("borrow", borrow);

        // 上网特征
        // 上网总时长 人均上网时长 上网高峰时段 上网内容喜好
        Map<String, Object> hotsport = studentServer.getHotsport(para);
        map.put("hotsport", hotsport);

        // 消费特征
        Map consume = consumeServe.sumCollect(para);
        List<Map<String, Object>> list = consumeServe.preferenceList(para);
        String type = "";
        for (int i = 0; (list != null && i < (list.size() > 3 ? 3 : list.size())); i++) {
            type += list.get(i).get("dscrp") + ",";
        }
        if (type.endsWith(",")) {
            type = type.substring(0, type.length() - 1);
        }
        consume.put("type", type);
        map.put("consume", consume);
        // 奖惩特征
        // 获奖人数 获奖次数 处罚人数 处罚次数
        Map<String, Object> punishandreward = studentServer.getPunishAndReward(para);
        map.put("punishandreward", punishandreward);

        return map;
    }*/
    @ApiOperation(value = "社群画像-社群特征-基本信息", notes = "社群画像-社群特征-基本信息", httpMethod = "POST")
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
    @RequestMapping(value = "/community/basicFeature", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse getBasicFeatureAOP(ParamsStatus param) {
        //学历选择研究生，返回null
        if (param.getEducation() != null && param.getEducation() == 2) {
            return CommResponse.success(null);
        }
        Map<String, Object> map = null;
        // 本省人为主 90后为主 有少数名族 男女比例失衡 （学籍表）
        Map<String, Object> status = studentServer.getStatus(param);
        if (null != status && status.size() != 0) {
            map = new HashMap<>();
            map.put("status", status);
        }
        return CommResponse.success(map);
    }

    /**
     * 社群画像-社群特征-上网特征
     * 上网总时长 人均上网时长 上网高峰时段 上网内容喜好
     * // *********平台未调用，先隐藏**********
     * @param param
     * @return
     */
    /*@RequestMapping(value ="/community/surfInternetFeature")
    @ResponseBody
    public Map<String, Object> getSurfInternetFeatureAOP(ParamsStatus param) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> hotsport = studentServer.getHotsport(param);
        map.put("hotsport", hotsport);
        return map;
    }*/

    /**
     * 社群画像-社群特征-消费特征
     * <p>
     * {"consume":{"avg":7207(个人实际消费天数平均金额),"sum":56307770（消费总金额）,"times":7109(个人时间段内消费天数),"type":"餐费,商场购物"（消费类目喜好）}}
     * }
     *
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/community/consumeFeature", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getConsumeFeatureAOP(ParamsStatus param) throws Exception {
        return consumeServe.getConsumeFeature(param);
    }

    @ApiOperation(value = "社群画像-社群概述-奖罚特征", notes = "社群画像-社群概述-奖罚特征", httpMethod = "POST")
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
    @RequestMapping(value = "/community/punishAndFeature", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse getPunishAndFeatureAOP(ParamsStatus param) {
        //学历选择研究生，返回null
        if (param.getEducation() != null && param.getEducation() == 2) {
            return CommResponse.success(null);
        }
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> punishandreward = studentServer.getPunishAndReward(param);
        map.put("punishandreward", punishandreward);
        return CommResponse.success(map);
    }

    @ApiOperation(value = "基本信息-个人画像-奖罚特征", notes = "奖罚特征", httpMethod = "POST")
    @ApiImplicitParam(paramType = "query", name = "outid", dataType = "String", required = true, value = "学号")
    @RequestMapping(value = "/punishReward", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse getPunishAndFeatureByStudentAOP(ParamsStatus param) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> punishandreward = studentServer.getPunishAndReward(param);
        map.put("punishandreward", punishandreward);
        return CommResponse.success(map);
    }

    @ApiOperation(value = "奖惩特征--处罚人员的图", notes = "处罚人员的图")
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
            @ApiImplicitParam(paramType = "query", name = "start", dataType = "Integer", value = "分页参数：从0开始"),
            @ApiImplicitParam(paramType = "query", name = "limit", dataType = "Integer", value = "分页参数：每页多少条数据"),
    })
    @RequestMapping(value = "/community/punishNum", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse getPunishCountAOP(ParamsStatus param) {
        //学历选择研究生，返回null
        if (param.getEducation() != null && param.getEducation() == 2) {
            return CommResponse.success(null);
        }
        return CommResponse.success(studentServer.getPunishCount(param));
    }

    /**
     * @api {POST} /api/v2/student/community/getPunishStudentList
     * @apiName getPunishStudentList
     * @apiGroup student
     * @apiVersion 2.0.0
     * @apiDescription 奖罚特征--》处罚人员  根据处罚类型获取详细的学生列表
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/student/community/getPunishStudentList
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
     * "data":{name=休学一年加留校察看, value=7}}
     * name:处罚类型  value 数量
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping(value = "/community/getPunishStudentList", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse getPunishStudentListAOP(ParamsStatus param) {
        List<Map<String, Object>> list = studentServer.getPunishStudentList(param);
        if (list != null && list.size() > 0) {
            return CommResponse.success(new PageResult(limitPage(list, param.getStart(), param.getLimit()), list.size()));
        }
        return CommResponse.success(list);
    }

    /**
     * @api {POST} /api/v2/student/community/scholarshipSex
     * @apiName scholarshipSex
     * @apiGroup student
     * @apiVersion 2.0.0
     * @apiDescription 获奖类型对应表的分页
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/student/community/scholarshipSex
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
     * "data":{nan_ratio=61%, total=1095, nv_ratio=39%, nv=417, name=1, nan=678}}
     * nan_ratio：男生百分比 nv_ratio：女生百分比 nv：女生数量 nan：男生数量 total：总数量 name：获奖次数
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping(value = "/community/scholarshipSex", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse getScholarshipCountBysexAOP(ParamsStatus param) {
        //学历选择研究生，返回null
        if (param.getEducation() != null && param.getEducation() == 2) {
            return CommResponse.success(null);
        }
        return CommResponse.success(studentServer.getScholarshipCountBysex(param));
    }

    /**
     * @api {POST} /api/v2/student/community/scholarshipSexList
     * @apiName scholarshipSexList
     * @apiGroup student
     * @apiVersion 2.0.0
     * @apiDescription 获奖类型对应表的分页
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/student/community/scholarshipSexList
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
     * "data":{nan_ratio=61%, total=1095, nv_ratio=39%, nv=417, name=1, nan=678}}
     * nan_ratio：男生百分比 nv_ratio：女生百分比 nv：女生数量 nan：男生数量 total：总数量 name：获奖次数
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping(value = "/community/scholarshipSexList", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse getScholarshipCountBysexListAOP(ParamsStatus param) {
        return CommResponse.success(studentServer.getScholarshipCountBysex(param));
    }

    /**
     * @api {POST} /api/v2/student/community/scholarshipType
     * @apiName scholarshipType
     * @apiGroup student
     * @apiVersion 2.0.0
     * @apiDescription 获奖类型 前三+其他
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/student/community/scholarshipType
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
     * "data":{name=人民奖学金, type=人民奖学金, value=3130}}
     * name:奖励类型  value 数量 type：刷新table时候的传递的参数
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping(value = "/community/scholarshipType", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse getScholarshipTypeCountAOP(ParamsStatus param) {
        //如果选择为第一，二学期，是没有数据的（产品规定的）
//		if (param != null && param.getTermNum() != null) {
//			if (param.getTermNum() == 1 || param.getTermNum() == 2) {
//				return CommResponse.success(null);
//			}
//		}
        //学历选择研究生，返回null
        if (param.getEducation() != null && param.getEducation() == 2) {
            return CommResponse.success(null);
        }
        return CommResponse.success(studentServer.getScholarshipTypeCount(param));
    }

    /**
     * @api {POST} /api/v2/student/community/getScholarshipTypeStudentList
     * @apiName getScholarshipTypeStudentList
     * @apiGroup student
     * @apiVersion 2.0.0
     * @apiDescription 奖惩特征 - 获奖类型 - 获奖类型学生列表
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/student/community/getScholarshipTypeStudentList
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
     * "data":{name=陈希, outid=134575888, class_code=13457471}}
     * name:奖励类型  value 数量 type：刷新table时候的传递的参数
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping(value = "/community/getScholarshipTypeStudentList", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse getScholarshipTypeStudentListAOP(ParamsStatus para) {
        //如果选择为第一，二学期，是没有数据的（产品规定的）
//		if (param != null && param.getTermNum() != null) {
//			if (param.getTermNum() == 1 || param.getTermNum() == 2) {
//				return CommResponse.success(null);
//			}
//		}
        List<Map<String, Object>> result = studentServer.getScholarshipTypeStudentList(para);
        return CommResponse.success(new PageResult(limitPage(result, para.getStart(), para.getLimit()), result.size()));
    }

    /**
     * @api {POST} /api/v2/student/community/getScholarshipStudentList
     * @apiName getScholarshipStudentList
     * @apiGroup student
     * @apiVersion 2.0.0
     * @apiDescription 奖罚特征——》获奖次数，根据获奖次数，查询学生列表
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/student/community/getScholarshipStudentList
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
     * "data":{name=人民奖学金, type=人民奖学金, value=3130}}
     * name:奖励类型  value 数量 type：刷新table时候的传递的参数
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping(value = "/community/getScholarshipStudentList", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse getScholarshipStudentListAOP(ParamsStatus paramsStatus) {
        List<Map<String, Object>> list = studentServer.getgetScholarshipStudentList(paramsStatus);
        if (list != null && list.size() > 0) {
            return CommResponse.success(new PageResult(limitPage(list, paramsStatus.getStart(), paramsStatus.getLimit()), list.size()));
        }
        return CommResponse.success(list);
    }

    /**
     * @api {POST} /api/v2/student/community/termDate
     * @apiName termDate
     * @apiGroup student
     * @apiVersion 2.0.0
     * @apiDescription 获取当前学期，上学期，上学年开始时间和结束时间
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/student/community/termDate
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
     * "data":{starttime="2018-02-26", endtime="2018-07-26" ,remark="2018上学期"}}
     * name:奖励类型  value 数量 type：刷新table时候的传递的参数
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
/*	@ResponseBody
    @RequestMapping(value ="/community/termDate")
	public CommResponse getTermDateAOP(ParamsStatus param) {
		return CommResponse.success(studentServer.getTermDate());
	}*/
    //手动分页的方法
    private List<Map<String, Object>> limitPage(List<Map<String, Object>> list, int start, int length) {
        //null值判断
        if (list == null) {
            return null;
        }
        //如果start大于list的长度则从0开始
        if (list.size() < start) {
            start = 0;
        }
        //如果截取的长度大于list的size则为size
        int end = start + length > list.size() ? list.size() : start + length;
        return list.subList(start, end);
    }

    /**
     * 提供为消费微服务--获取学生消费列表总长度
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "群体-消费学生列表", notes = "消费学生列表")
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
            @ApiImplicitParam(name = "bdate", paramType = "query", dataType = "String", value = " 开始时间"),
            @ApiImplicitParam(name = "edate", paramType = "query", dataType = "String", value = " 结束时间"),
            @ApiImplicitParam(name = "name", paramType = "query", dataType = "String", value = " 学生姓名"),
            @ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", value = " 学号")
    })
    @RequestMapping(value = "/common/getStudents", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> getStudents(@RequestBody Params params) {
        return studentServer.getStudents(params);
    }

    /**
     * 提供为消费微服务--获取学生消费列表总长度
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "群体-消费学生列表", notes = "消费学生列表")
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
            @ApiImplicitParam(name = "bdate", paramType = "query", dataType = "String", value = " 开始时间"),
            @ApiImplicitParam(name = "edate", paramType = "query", dataType = "String", value = " 结束时间"),
            @ApiImplicitParam(name = "name", paramType = "query", dataType = "String", value = " 学生姓名"),
            @ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", value = " 学号")
    })
    @ResponseBody
    @RequestMapping(value = "/common/getStudentSize", method = RequestMethod.POST)
    public Long getStudentSize(@RequestBody Params params) {
        return studentServer.getStudentSize(params);
    }

    /**
     * 微服务提供接口  获取所有的学生基本详情
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "微服务提供接口-获取所有学生的基本详情", notes = "获取所有学生的基本详情")
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
            @ApiImplicitParam(name = "bdate", paramType = "query", dataType = "String", value = " 开始时间"),
            @ApiImplicitParam(name = "edate", paramType = "query", dataType = "String", value = " 结束时间"),
            @ApiImplicitParam(name = "name", paramType = "query", dataType = "String", value = " 学生姓名"),
            @ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", value = " 学号")
    })
    @ResponseBody
    @RequestMapping(value = "/common/selectAllDetail", method = RequestMethod.POST)
    List<OwnOrgStudentType> selectAllDetail(@RequestBody Params params) {
        return studentServer.selectAllDetail(params);
    }

    /**
     * 根据学号id，查询学生基本信息  公共接口
     *
     * @param outid 学号
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/common/selectByPrimaryKey", method = RequestMethod.POST)
    @ApiOperation(value = "微服务提供接口-获取所有学生的基本详情", notes = "获取所有学生的基本详情")
    @ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", value = " 学号")
    public OwnOrgStudent selectByPrimaryKey(@RequestBody String outid) {
        return studentServer.selectByPrimaryKey(outid);
    }


    @RequestMapping(value = "/common/pageStudent", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "基本信息-学生列表", notes = "学生列表")
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
    public CommResponse pageStudent(@RequestBody Params para) throws Exception {
        List<LinkedHashMap<String, Object>> list = studentServer.getPageStudent(para);

        List<LinkedHashMap<String, Object>> result = new ArrayList<>();
//		if(!SecurityUtils.getSubject().isPermitted("show_complete_idcard")){
        if (null != list && list.size() > 0) {
            for (LinkedHashMap<String, Object> map : list) {
                if (map.containsKey("idcard_no") && null != map.get("idcard_no")) {
                    String idcard_no = String.valueOf(map.get("idcard_no"));
                    if (idcard_no.length() > 3) {
                        idcard_no = idcard_no.substring(0, idcard_no.length() - 3) + "***";
                        map.put("idcard_no", idcard_no);
                    }
                }
                result.add(map);
            }
        }
//		}else{
//			result.addAll(list);
//		}
        return CommResponse.success(result);
    }

    @RequestMapping(value = "/common/iPageStudent", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "基本信息-学生列表", notes = "学生列表")
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
    public List<LinkedHashMap<String, Object>> iPageStudent(@RequestBody Params para) throws Exception {
        List<LinkedHashMap<String, Object>> list = studentServer.getPageStudent(para);

        List<LinkedHashMap<String, Object>> result = new ArrayList<>();
//		if(!SecurityUtils.getSubject().isPermitted("show_complete_idcard")){
        if (null != list && list.size() > 0) {
            for (LinkedHashMap<String, Object> map : list) {
                if (map.containsKey("idcard_no") && null != map.get("idcard_no")) {
                    String idcard_no = String.valueOf(map.get("idcard_no"));
                    if (idcard_no.length() > 3) {
                        idcard_no = idcard_no.substring(0, idcard_no.length() - 3) + "***";
                        map.put("idcard_no", idcard_no);
                    }
                }
                result.add(map);
            }
        }
//		}else{
//			result.addAll(list);
//		}
        return result;
    }

    @RequestMapping(value = "/common/studentsCount", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "基本信息--学生列表总长度", notes = "学生列表总长度")
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
    public CommResponse studentsCount(Params params) {
        Integer count = studentServer.countStudent(params);
        return CommResponse.success(count);
    }

    @ApiOperation(value = "个人档案-判断是否是毕业生", notes = "判断是否是毕业生")
    @ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", value = "学号")
    @RequestMapping(value = "/graduationStudent", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse isGraduationStudent(ParamsStatus param) {
        return CommResponse.success(studentServer.isGraduationStudent(param.getOutid()));
    }

    /**
     * 个人画像-学业特征
     *
     * @return
     * @throws IOException
     */
    //http://localhost:8090/student/personal/featuresAcademic
    @RequestMapping(value = "/personal/featuresAcademic", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> featureAcademicAOP(AcademicParams para) throws IOException {

        Map<String, Object> academicLabel = null;
        try {
            academicLabel = academicServer.getAcademicLabel(para);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return academicLabel;
    }

    @RequestMapping(value = "/common/selectByOutId", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "根据学号查询学生个人基本信息", notes = "根据学号查询学生个人基本信息")
    @ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", value = "学号")
    public Map selectByOutid(@RequestBody String outid) {
        return studentServer.selectByOutid(outid);
    }

    @ApiOperation(value = "奖惩特征--奖惩学生列表", notes = "奖惩学生列表")
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
            @ApiImplicitParam(paramType = "query", name = "start", dataType = "Integer", value = "分页参数：从0开始"),
            @ApiImplicitParam(paramType = "query", name = "limit", dataType = "Integer", value = "分页参数：每页多少条数据"),
    })
    @RequestMapping(value = "/community/scholarshipPunishStudentList", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse getScholarshipPunishStudentListAOP(ParamsStatus paramsStatus) {
        if (null != paramsStatus.getTermtype() && paramsStatus.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        //如果用户选择了研究生，返回null
        if (paramsStatus.getEducation() != null && paramsStatus.getEducation() == 2) {
            return CommResponse.success(null);
        }
        if (paramsStatus.getStart() == null) {
            paramsStatus.setStart(0);
            paramsStatus.setLimit(10);
        }
        List<Map<String, Object>> list = studentServer.getScholarshipPunishStudentList(paramsStatus);

        return CommResponse.success(list);
    }

    @ApiOperation(value = "奖惩特征--奖惩学生列表", notes = "奖惩学生列表")
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
            @ApiImplicitParam(paramType = "query", name = "start", dataType = "Integer", value = "分页参数：从0开始"),
            @ApiImplicitParam(paramType = "query", name = "limit", dataType = "Integer", value = "分页参数：每页多少条数据"),
    })
    @RequestMapping(value = "/community/scholarshipPunishStudentCount", method = RequestMethod.POST)
    @ResponseBody
    public CommResponse getScholarshipPunishStudentCountAOP(ParamsStatus paramsStatus) {
        if (null != paramsStatus.getTermtype() && paramsStatus.getTermtype() == 2) {
            return CommResponse.success(null);
        }
        //如果用户选择了研究生，返回null
        if (paramsStatus.getEducation() != null && paramsStatus.getEducation() == 2) {
            return CommResponse.success(null);
        }
        if (paramsStatus.getStart() == null) {
            paramsStatus.setStart(0);
            paramsStatus.setLimit(10);
        }
        Long size = studentServer.getScholarshipPunishStudentCount(paramsStatus);


        return CommResponse.success(size);
    }

    @RequestMapping(value = "/common/getEnrollmentYearById", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "微服务提供接口--根据学号id查询入学年份", notes = "根据学号id查询入学年份")
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
            @ApiImplicitParam(paramType = "query", name = "start", dataType = "Integer", value = "分页参数：从0开始"),
            @ApiImplicitParam(paramType = "query", name = "limit", dataType = "Integer", value = "分页参数：每页多少条数据"),
    })
    public String getEnrollmentYearById(@RequestBody Params params) {
        return studentServer.getEnrollmentYearById(params);
    }


    @ResponseBody
    @RequestMapping(value = "/activeRanking", method = RequestMethod.POST)
    @ApiOperation(value = "活动积分排名", notes = "活动积分排名列表")
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
            @ApiImplicitParam(paramType = "query", name = "start", dataType = "Integer", value = "分页参数：从0开始"),
            @ApiImplicitParam(paramType = "query", name = "limit", dataType = "Integer", value = "分页参数：每页多少条数据"),
    })
    public CommResponse getActiveRankingAOP(ParamsStatus param) {
        if (param != null) {
            if (StringUtils.isBlank(param.getSort()) || StringUtils.isBlank(param.getOrder())) {
                param.setSort("integration");
                param.setOrder("descending");
            }
            //学历选择研究生，返回null
            if (param.getEducation() != null && param.getEducation() == 2) {
                return CommResponse.success(null);
            }
        }
        List<Map<String, Object>> list = studentServer.getActiveRanking(param);
        if (CollectionUtils.isEmpty(list)) {
            return CommResponse.success(new String[0]);
        }
        return CommResponse.success(list);
    }


    @ResponseBody
    @RequestMapping(value = "/activeRankingSize", method = RequestMethod.POST)
    @ApiOperation(value = "活动积分排名", notes = "活动积分排名总长度")
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
            @ApiImplicitParam(paramType = "query", name = "start", dataType = "Integer", value = "分页参数：从0开始"),
            @ApiImplicitParam(paramType = "query", name = "limit", dataType = "Integer", value = "分页参数：每页多少条数据"),
    })
    public CommResponse geatctiveRankingSizeAOP(ParamsStatus param) {
        //学历选择研究生，返回null
        if (param.getEducation() != null && param.getEducation() == 2) {
            return CommResponse.success(null);
        }
        int size = studentServer.getActiveRankingSize(param);
        return CommResponse.success(size);
    }

    @ResponseBody
    @RequestMapping(value = "/getPunishType", method = RequestMethod.POST)
    @ApiOperation(value = "活动积分排名", notes = "活动积分排名总长度")
    public CommResponse getPunishType(Params params) {
        List<String> list = studentServer.getPunishType(params);
        return CommResponse.success(list);
    }
}
