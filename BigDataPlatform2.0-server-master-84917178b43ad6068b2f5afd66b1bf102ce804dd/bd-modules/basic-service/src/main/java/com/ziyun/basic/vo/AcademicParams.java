package com.ziyun.basic.vo;

import org.apache.commons.lang.StringUtils;

public class AcademicParams extends Params {

    /**
     * 课程属性参数  0-全部，1-必修，2-选修，3-同属必修选修
     */
    private String courseProperties;

    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 教室名 -->针对考勤打卡
     *
     * @return
     */
    private String className;

    /**
     * 上考勤打卡，按照学号排序 0  正序  1  倒叙
     *
     * @return
     */
    private String outidSort;

    /**
     * 上课考勤打卡 ,按照打卡排序 0  正序  1  倒叙
     *
     * @return
     */
    private String timeSort;

    public String getOutidSort() {
        return outidSort;
    }

    public void setOutidSort(String outidSort) {
        this.outidSort = outidSort;
    }

    public String getTimeSort() {
        return timeSort;
    }

    public void setTimeSort(String timeSort) {
        this.timeSort = timeSort;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * 课程性质
     */
    private String courseNatures;

    private String sort;//该字段用来指明排序  0---- asc   1--- desc  默认是1

    @Override
    public String getSort() {
        return sort;
    }

    @Override
    public void setSort(String sort) {
        this.sort = sort;
    }

    /**
     * 课程分类
     */
    private String courseCategory;
    /**
     * 学期多选   例如  2012-2013-1  2012-2013-2
     */
    private String[] semesterArr;


    public String[] getSemesterArr() {
        return semesterArr;
    }

    public void setSemesterArr(String[] semesterArr) {
        this.semesterArr = semesterArr;
    }

    /**
     * 该字段用来无挂科的课程
     */
    private Integer pass;

    public Integer getPass() {
        return pass;
    }

    public void setPass(Integer pass) {
        this.pass = pass;
    }

    /**
     * 学业特征的学期字段，为了区分自定义中的学期字段
     */
    private Integer termNumAcademic;
    private String failCourseNum;
    private Integer typeNum;//用来表示广科属是否大于10，0---小于10，1---大于10

    private String courseName;// 课程名


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        if (StringUtils.isNotBlank(courseName)) {
            //springmvc 接收带小括号的参数时，将"("  转义成 "&#40;" ,将")" 转义成 "&#41;"  对转义的处理
            courseName = courseName.replace("&#40;", "(").replace("&#41;", ")");
        }
        this.courseName = courseName;
    }

    public Integer getTypeNum() {
        return typeNum;
    }

    public void setTypeNum(Integer typeNum) {
        this.typeNum = typeNum;
    }

    private String courseNo;

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getFailCourseNum() {
        return failCourseNum;
    }

    public void setFailCourseNum(String failCourseNum) {
        this.failCourseNum = failCourseNum;
    }

    public String getCourseProperties() {
        return courseProperties;
    }

    public void setCourseProperties(String courseProperties) {
        this.courseProperties = courseProperties;
    }

    public String getCourseNatures() {
        return courseNatures;
    }

    public void setCourseNatures(String courseNatures) {
        if (StringUtils.isBlank(courseNatures)) {
            courseNatures = null;
        }
        this.courseNatures = courseNatures;
    }

    public String getCourseCategory() {
        return courseCategory;
    }

    public void setCourseCategory(String courseCategory) {
        if (StringUtils.isBlank(courseCategory)) {
            courseCategory = null;
        }
        this.courseCategory = courseCategory;
    }

    public Integer getTermNumAcademic() {
        return termNumAcademic;
    }

    public void setTermNumAcademic(Integer termNumAcademic) {
        this.termNumAcademic = termNumAcademic;
    }
}
