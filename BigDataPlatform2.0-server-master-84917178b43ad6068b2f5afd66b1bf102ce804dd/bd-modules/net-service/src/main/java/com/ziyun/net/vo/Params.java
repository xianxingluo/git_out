package com.ziyun.net.vo;

import com.google.common.base.Objects;
import com.ziyun.net.enums.SexEnum;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

/**
 * 从前台传过来的：查询条件
 */
public class Params {
    private String token;//登录之后，后端返回一个token给前端；后面每个请求，前端需要带着这个token。后台根据这个进行权限验证。

    private String schoolCode;// 校区code：：'学籍类型--slg 苏理工 jkd 江科大
    private String facultyCode;// 院系code
    private String majorCode;// 专业code
    //private String[] classCode;// 班级code：：班级只有code,没有名称

    private String timeframe;//查询的时间范围标识： 0、全部时间', 1、本学期',2、上学期',3、上学年'+++++
    private Integer termtype;//1、上课期间。2、寒暑假期间。+++++只有部分查询支持该参数
    private String orgCode;// 机构、单位编码

    // 学号
    private String outid;

    // 姓名
    private String name;


    // 性别
    private String sex;

    // 班级: 多选用逗号隔开
    private String classSelect;

    // 政治面貌: 1、团员，2、预备党员，3、党员
    private Integer politicalCode;

    // 入学年份
    private String enrollmentYear;


    //入学年份
    private String[] yearArr;

    public String[] getYearArr() {
        return yearArr;
    }

    public void setYearArr(String[] yearArr) {
        this.yearArr = yearArr;
    }

    // 学籍: 1-0 --> 正常生[新生,老生]
    //      1-3 --> 留级生
    // 	    1-4 --> 休学生
    // 		2-5 --> 毕业生
    // 		2-6 --> 肄业
    // 		2-7 --> 其他
    private String eduStatus;

    // 学历: 1->本科, -1->null
    private Integer education;

    // 开始时间

    //学号多选：使用
    private String[] outidArr;
    private String bdate;

    // 结束时间
    private String edate;

    private String likes;// 模糊搜索条件：不同的表匹配不同字段

    // 贫困生类型: maybe not use
    private String type;

    // 优等生: 1
    private String scholarship;

    // 贫困生: 1
    private String impoverish;

    // 用于学业预警,上网总时长: net_time_sum, 消费总金额: spend_sum
    private String columnName;

    private String semester;// 本学期0，上学期 -1

    private String someCode;// 根据前台传过来的分类：查询下面的明细、或者排名

    private String borrowDate;

    private String warnRuleId; //学业预警规则id

    private String warnRule; //学业预警规则

    private Integer sumesomeCode;// 消费类型的code是int类型，但是也通过someCode传过来，所以这里用这个接受并带人数据库::根据前台传过来的分类：查询下面的明细、或者排名

    public Integer getSumesomeCode() {
        if (StringUtils.isBlank(someCode)) {
            return null;
        }
        return Integer.parseInt(someCode);
    }

    //----------------------------------------
    private String[] classCode;// 班级code：：班级只有code,没有名称

    private String searchYear;

    private String dataDuration;//时间期间

    // 第几学期：相对这个学生的，一共8个学期，留级的是有几个学期是重复的
    private Integer termNum;

    // 分页参数
    // private Integer pageNumber;// 第几页
    // private Integer pageSize;// 每页多少条数据

    // 查询起始--由于前台bootstrap
    // table的start传参有问题，从1开始了，所以分页的时间做个处理，从0开始
    private Integer start;

    // 每页多少条数据
    private Integer limit;

    public String getDataDuration() {
        return dataDuration;
    }

    public void setDataDuration(String dataDuration) {
        this.dataDuration = dataDuration;
    }

    public void setSumesomeCode(Integer sumesomeCode) {
        this.sumesomeCode = sumesomeCode;
    }


    public Integer getEducation() {
        return education;
    }

    public void setEducation(Integer education) {
        this.education = education;
    }

    public Integer getTermNum() {
        return termNum;
    }

    public void setTermNum(Integer termNum) {
        this.termNum = termNum;
    }

    public String getEduStatus() {
        return eduStatus;
    }

    public void setEduStatus(String eduStatus) {
        if (StringUtils.isBlank(eduStatus))
            eduStatus = null;
        this.eduStatus = eduStatus;
    }


    public void setClassCode(String[] classCode) {
        this.classCode = classCode;
    }

    // classSelect班级编码是：用逗号隔开的多个字符串（多选），这里把它拆成数组。赋值到classCode；方便sql中进行多选处理
    public String[] getClassCode() {
        if (this.classCode != null && this.classSelect == null) {
            return this.classCode;
        } else {
            if (StringUtils.isNotEmpty(this.classSelect)) {
                String str = this.classSelect;
                if (str.startsWith(",")) {// 班级编码：去掉头、尾的逗号
                    str = str.substring(1, str.length());
                }
                if (str.endsWith(",")) {// 班级编码：去掉头、尾的逗号
                    str = str.substring(0, str.length() - 1);
                }
                String[] array = str.split(",", -1);
                this.classCode = array;
                return array;
            } else {
                return null;
            }
        }
    }
    //------由于sql和前台穿过来的参数类型不一致：这些字段用于类型转换-----
    //==================================================


    private String sort;
    private String order;

    // /**
    // * 导出：按照班级汇总
    // */
    // private String orderbyclass;

    /**
     * 16、 新版上网时段分布：｛周末、周一到周五2个24小时图｝
     * <p>
     * 1、weekend=weekend 只查询周六、周日的数据
     * <p>
     * 2、weekend=notweekend 只查询周一到周五的数据
     */
    private String weekend;//

    //学期
    private String gradeLevel;


    public String getClassSelect() {
        return classSelect;
    }

    public void setClassSelect(String classSelect) {
        if (StringUtils.isBlank(classSelect))
            classSelect = null;
        this.classSelect = classSelect;
    }

    public String getSort() {
        if (StringUtils.isNotBlank(sort))
            return sort;
        else {
            this.sort = null;
            return null;
        }

    }

    public void setSort(String sort) {
        if (StringUtils.isBlank(sort))
            sort = null;
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        if (StringUtils.isBlank(order))
            order = null;
        this.order = order;
    }

    // /**
    // * 根据第几页：返回查询limit的起始行
    // *
    // * @return
    // */
    // public Integer getPageNum() {
    // return (pageNumber - 1) * pageSize;
    // }
    //
    // public Integer getPageNumber() {
    // return pageNumber;
    // }

    // public void setPageNumber(Integer pageNumber) {
    // this.pageNumber = pageNumber;
    // }
    //
    // public Integer getPageSize() {
    // return pageSize;
    // }
    //
    // public void setPageSize(Integer pageSize) {
    // this.pageSize = pageSize;
    // }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        if (StringUtils.isBlank(schoolCode))
            schoolCode = null;
        this.schoolCode = schoolCode;
    }

    public String getFacultyCode() {
        return facultyCode;
    }

    public void setFacultyCode(String facultyCode) {
        if (StringUtils.isBlank(facultyCode))
            facultyCode = null;
        this.facultyCode = facultyCode;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        if (StringUtils.isBlank(majorCode))
            majorCode = null;
        this.majorCode = majorCode;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        if (StringUtils.isBlank(bdate))
            bdate = null;
        this.bdate = bdate;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        if (StringUtils.isBlank(edate))
            edate = null;
        this.edate = edate;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        if (StringUtils.isBlank(orgCode))
            orgCode = null;
        this.orgCode = orgCode;
    }

    public String getOutid() {
        return outid;
    }

    public void setOutid(String outid) {
        if (StringUtils.isBlank(outid))
            outid = null;
        this.outid = outid;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit == null ? Integer.valueOf(10) : limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (StringUtils.isBlank(type))
            type = null;
        this.type = type;
    }

    public String getScholarship() {
        return scholarship;
    }

    public void setScholarship(String scholarship) {
        if (StringUtils.isBlank(scholarship))
            scholarship = null;
        this.scholarship = scholarship;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        if (StringUtils.isBlank(likes))
            likes = null;
        this.likes = likes;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        if (StringUtils.isBlank(semester))
            semester = null;
        this.semester = semester;
    }

    public String getEnrollmentYear() {
        return enrollmentYear;
    }

    public void setEnrollmentYear(String enrollmentYear) {
        if (StringUtils.isBlank(enrollmentYear)) {
            enrollmentYear = null;
        }
        this.enrollmentYear = enrollmentYear;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        if (StringUtils.isBlank(borrowDate))
            borrowDate = null;
        this.borrowDate = borrowDate;
    }

    /*
     * private String schoolCode;// 校区code：：'学籍类型--slg 苏理工 jkd 江科大 private
     * String facultyCode;// 院系code private String majorCode;// 专业code private
     * String classCode;// 班级code：：班级只有code,没有名称 private String bdate;// 开始时间
     * private String edate;// 结束时间 private String orgCode;// 机构、单位编码 private
     * String outid;// 学号 private String type;// 贫困生类型为i private String likes;//
     * 模糊搜索条件：不同的表匹配不同字段 private Integer start;// 查询起始--由于前台bootstrap
     * table的start传参有问题，从1开始了，所以分页的时间做个处理，从0开始 private Integer limit;// 每页多少条数据
     * private String scholarship; //传参过来，即可；或者不传 private String
     * semester;//本学期0，上学期 -1 private String impoverish;//自定义：贫困生判断，传参过来，即可；或者不传
     * private String sort; private String order;
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(schoolCode, facultyCode, majorCode, Arrays.toString(classCode),
                classSelect, outid, scholarship, semester, impoverish, sort,
                order, bdate, edate, orgCode, type, likes, start, limit, name,
                someCode, sex, weekend, politicalCode, timeframe, gradeLevel, termtype,
                warnRuleId, warnRule, searchYear, enrollmentYear, eduStatus, termNum, outidArr);
    }

    public String getImpoverish() {
        return impoverish;
    }

    public void setImpoverish(String impoverish) {
        if (StringUtils.isBlank(impoverish))
            impoverish = null;
        this.impoverish = impoverish;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (StringUtils.isBlank(name))
            name = null;
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        if (StringUtils.isBlank(sex)) {
            this.sex = null;
            return;
        }
        if (sex.equals(SexEnum.MALE.getCode())) {
            sex = SexEnum.MALE.getValue();
        } else if (sex.equals(SexEnum.FEMALE.getCode())) {
            sex = SexEnum.FEMALE.getValue();
        } else {
            sex = null;
        }
        this.sex = sex;
    }

    public String getSomeCode() {
        return someCode;
    }

    public void setSomeCode(String someCode) {
        if (StringUtils.isBlank(someCode))
            someCode = null;
        this.someCode = someCode;
    }

    public String getWeekend() {
        return weekend;
    }

    public void setWeekend(String weekend) {
        if (StringUtils.isBlank(weekend))
            weekend = null;
        this.weekend = weekend;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        if (StringUtils.isBlank(gradeLevel))
            gradeLevel = null;
        this.gradeLevel = gradeLevel;
    }

    public String getTimeframe() {
        return timeframe;
    }

    public void setTimeframe(String timeframe) {
        if (StringUtils.isBlank(timeframe))
            timeframe = null;
        this.timeframe = timeframe;
    }

    public Integer getPoliticalCode() {
        return politicalCode;
    }

    public void setPoliticalCode(Integer politicalCode) {
        this.politicalCode = politicalCode;
    }

    public Integer getTermtype() {
        return termtype;
    }

    public void setTermtype(Integer termtype) {
        this.termtype = termtype;
    }

    // public String getOrderbyclass() {
    // return orderbyclass;
    // }
    //
    // public void setOrderbyclass(String orderbyclass) {
    // this.orderbyclass = orderbyclass;
    // }

    /*方便拼sql*/
    private String sql;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getWarnRuleId() {
        return warnRuleId;
    }

    public void setWarnRuleId(String warnRuleId) {
        this.warnRuleId = warnRuleId;
    }

    public String getWarnRule() {
        return warnRule;
    }

    public void setWarnRule(String warnRule) {
        this.warnRule = warnRule;
    }


    public String getSearchYear() {
        return searchYear;
    }

    public void setSearchYear(String searchYear) {
        this.searchYear = searchYear;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String[] getOutidArr() {
        return outidArr;
    }

    public void setOutidArr(String[] outidArr) {
        this.outidArr = outidArr;
    }
}
