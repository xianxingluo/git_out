package com.ziyun.consume.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * sql查询返回结果的封装：常见可能返回的字段
 */
public class ResultData implements Serializable, Comparable<ResultData> {

    /**
     *
     */
    private static final long serialVersionUID = -8601829696870958110L;

    private Long id;//
    //
    private String schoolCode;// 校区code：：'学籍类型--slg 苏理工 jkd 江科大
    private String facultyCode;// 院系code
    private String majorCode;// 专业code
    private String classCode;// 班级code：：班级只有code,没有名称
    // --学生信息：
    private String outid;// 学号
    private String name;// 姓名
    private String sex;// 性别
    // --消费类型
    private Long acccode;// 消费类型：code
    private String dscrp;// 消费类型：名称
    //
    private BigDecimal sum1;// 某个消费类型的金额
    private BigDecimal sum2;// 某个消费类型的金额
    private BigDecimal sum3;// 某个消费类型的金额
    private BigDecimal sum4;// 某个消费类型的金额
    private BigDecimal sum5;// 某个消费类型的金额
    private BigDecimal sum6;// 某个消费类型的金额
    // --时间
    // ++用于计算：时间段内出现了多少个周几；从而算周几的平均
    private Date bdate;// 记录的最早时间
    private Date edate;// 记录的最晚时间
    // ++
    private Date datetime;// 时间
    private String datetimeStr;// 时间:字符串格式
    private Integer weekindex;// 星期几：
    private String week;// 星期:字符
    private String hour;// 小时
    // --汇总值
    private Long num;// 整数：：例如-数量
    private BigDecimal sum = BigDecimal.ZERO;// 浮点数：：例如-金额
    private BigDecimal avg;// （除以有记录的人数的）平均金额：： 浮点数：：例如-金额
    private BigDecimal allAvg;// （除以总人数的）平均金额：： 浮点数：：例如-金额
    //
    private BigDecimal times;// （除以总人数的）平均次数
    private BigDecimal avgTimes;// （除以有记录的人数的）平均次数
    private BigDecimal allAvgTimes;// （除以总人数的）平均次数
    //日均实际消费天数
    private BigDecimal dayAvgConsume;

    private BigDecimal monthAvg;// （除以有记录的人数的）月平均金额：： 浮点数：：例如-金额
    private BigDecimal allMonthAvg;// （除以总人数的）月平均金额：： 浮点数：：例如-金额

    private BigDecimal mansum = BigDecimal.ZERO;// 男：金额：： 浮点数：：例如-金额
    private BigDecimal womansum = BigDecimal.ZERO;// 女：金额：： 浮点数：：例如-金额
    private String manPercent;// 男：百分比
    private String womanPercent;// 女：百分比

    // // --借阅/书籍信息：
    // private String booktype;// 书籍类型
    // private String bookname;// 书名

    private String schoolName;// 校区Name：：'学籍类型--slg 苏理工 jkd 江科大
    private String facultyName;// 院系Name
    private String majorName;// 专业code
    private Integer peopleNum;// 专业code

    private int index;// 排序;从1开始

    private Short age;// 年龄
    private String sourceLocation; // 来源省份
    private String politicalStatus;// 政治面貌
    private String enrollmentYear;// 入学年份

    //平均消费天数
    private BigDecimal avgConsumeDays;
    /************ Add By Linxiaojun ************/
    // 比率
    private String ratio;

    // used for kylin
    // 映射 sum
    private BigDecimal totalDuration;

    // 映射 datetimeStr
    private Date acctstarttime;

    // 映射 name
    private String clienttype;

    // 上线的小时位置
    private int startHour;

    // 下线的小时位置
    private int stopHour;

    // 有效天数
    private int validDays;

    // 消费类目
    private String label;

    /*****************************************/

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getFacultyCode() {
        return facultyCode;
    }

    public void setFacultyCode(String facultyCode) {
        this.facultyCode = facultyCode;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getOutid() {
        return outid;
    }

    public void setOutid(String outid) {
        this.outid = outid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Long getAcccode() {
        return acccode;
    }

    public void setAcccode(Long acccode) {
        this.acccode = acccode;
    }

    public String getDscrp() {
        return dscrp;
    }

    public void setDscrp(String dscrp) {
        this.dscrp = dscrp;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDatetimeStr() {
        return datetimeStr;
    }

    public void setDatetimeStr(String datetimeStr) {
        this.datetimeStr = datetimeStr;
    }

    public Integer getWeekindex() {
        return weekindex;
    }

    public void setWeekindex(Integer weekindex) {
        this.weekindex = weekindex;
    }

    public Date getBdate() {
        return bdate;
    }

    public void setBdate(Date bdate) {
        this.bdate = bdate;
    }

    public Date getEdate() {
        return edate;
    }

    public void setEdate(Date edate) {
        this.edate = edate;
    }

    public BigDecimal getAvg() {
        return avg;
    }

    public void setAvg(BigDecimal avg) {
        this.avg = avg;
    }

    @Override
    public int compareTo(ResultData o) {
        return this.sum.compareTo(o.sum);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    // public String getBooktype() {
    // return booktype;
    // }
    //
    // public void setBooktype(String booktype) {
    // this.booktype = booktype;
    // }
    //
    // public String getBookname() {
    // return bookname;
    // }
    //
    // public void setBookname(String bookname) {
    // this.bookname = bookname;
    // }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
    }

    public BigDecimal getMansum() {
        return mansum;
    }

    public void setMansum(BigDecimal mansum) {
        this.mansum = mansum;
    }

    public BigDecimal getWomansum() {
        return womansum;
    }

    public void setWomansum(BigDecimal womansum) {
        this.womansum = womansum;
    }

    public String getManPercent() {
        return manPercent;
    }

    public void setManPercent(String manPercent) {
        this.manPercent = manPercent;
    }

    public String getWomanPercent() {
        return womanPercent;
    }

    public void setWomanPercent(String womanPercent) {
        this.womanPercent = womanPercent;
    }

    public Short getAge() {
        return age;
    }

    public void setAge(Short age) {
        this.age = age;
    }

    public String getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(String sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public String getPoliticalStatus() {
        return politicalStatus;
    }

    public void setPoliticalStatus(String politicalStatus) {
        this.politicalStatus = politicalStatus;
    }

    public String getEnrollmentYear() {
        return enrollmentYear;
    }

    public void setEnrollmentYear(String enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

    public BigDecimal getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(BigDecimal totalDuration) {
        this.totalDuration = totalDuration;
    }

    public String getClienttype() {
        return clienttype;
    }

    public void setClienttype(String clienttype) {
        this.clienttype = clienttype;
    }

    public Date getAcctstarttime() {
        return acctstarttime;
    }

    public void setAcctstarttime(Date acctstarttime) {
        this.acctstarttime = acctstarttime;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStopHour() {
        return stopHour;
    }

    public void setStopHour(int stopHour) {
        this.stopHour = stopHour;
    }

    public int getValidDays() {
        return validDays;
    }

    public void setValidDays(int validDays) {
        this.validDays = validDays;
    }

    public BigDecimal getSum1() {
        return sum1;
    }

    public void setSum1(BigDecimal sum1) {
        this.sum1 = sum1;
    }

    public BigDecimal getSum2() {
        return sum2;
    }

    public void setSum2(BigDecimal sum2) {
        this.sum2 = sum2;
    }

    public BigDecimal getSum3() {
        return sum3;
    }

    public void setSum3(BigDecimal sum3) {
        this.sum3 = sum3;
    }

    public BigDecimal getSum4() {
        return sum4;
    }

    public void setSum4(BigDecimal sum4) {
        this.sum4 = sum4;
    }

    public BigDecimal getSum5() {
        return sum5;
    }

    public void setSum5(BigDecimal sum5) {
        this.sum5 = sum5;
    }

    public BigDecimal getSum6() {
        return sum6;
    }

    public void setSum6(BigDecimal sum6) {
        this.sum6 = sum6;
    }

    public BigDecimal getAllAvg() {
        return allAvg;
    }

    public void setAllAvg(BigDecimal allAvg) {
        this.allAvg = allAvg;
    }

    public BigDecimal getMonthAvg() {
        return monthAvg;
    }

    public void setMonthAvg(BigDecimal monthAvg) {
        this.monthAvg = monthAvg;
    }

    public BigDecimal getAllMonthAvg() {
        return allMonthAvg;
    }

    public void setAllMonthAvg(BigDecimal allMonthAvg) {
        this.allMonthAvg = allMonthAvg;
    }

    public BigDecimal getTimes() {
        return times;
    }

    public void setTimes(BigDecimal times) {
        this.times = times;
    }

    public BigDecimal getAvgTimes() {
        return avgTimes;
    }

    public void setAvgTimes(BigDecimal avgTimes) {
        this.avgTimes = avgTimes;
    }

    public BigDecimal getAllAvgTimes() {
        return allAvgTimes;
    }

    public void setAllAvgTimes(BigDecimal allAvgTimes) {
        this.allAvgTimes = allAvgTimes;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BigDecimal getAvgConsumeDays() {
        return avgConsumeDays;
    }

    public void setAvgConsumeDays(BigDecimal avgConsumeDays) {
        this.avgConsumeDays = avgConsumeDays;
    }

    public BigDecimal getDayAvgConsume() {
        return dayAvgConsume;
    }

    public void setDayAvgConsume(BigDecimal dayAvgConsume) {
        this.dayAvgConsume = dayAvgConsume;
    }
}
