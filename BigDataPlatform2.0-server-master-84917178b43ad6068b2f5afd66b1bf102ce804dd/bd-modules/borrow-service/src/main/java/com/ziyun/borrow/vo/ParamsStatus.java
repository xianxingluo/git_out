package com.ziyun.borrow.vo;

import org.apache.commons.lang.StringUtils;

/**
 * 归类信息专用查询参数
 */
public class ParamsStatus extends Params {

    private String startDate;

    private String endDate;

    private String sql;

    private String punishType;

    private String types;

    private String[] typeArr;

    private int[] id;

    private String base;

    private String lateTime;

    private Integer scholarshipType;// 0---->前三种奖学金的一种，1--->其他奖学金
    // 奖惩特征  获奖类型分类： 人民奖学金，科技竞赛类
    private String[] winningType;

    public String[] getWinningType() {
        return winningType;
    }

    public void setWinningType(String[] winningType) {
        this.winningType = winningType;
    }

    private String[] scholarshipName;//奖学金的名称，可能为一个或多个
    private Integer scholarshipNum;// 获奖次数
    private Integer ioflag;

    public Integer getIoflag() {
        return ioflag;
    }

    public void setIoflag(Integer ioflag) {
        this.ioflag = ioflag;
    }

    public Integer getScholarshipNum() {
        return scholarshipNum;
    }

    public void setScholarshipNum(Integer scholarshipNum) {
        this.scholarshipNum = scholarshipNum;
    }

    public Integer getScholarshipType() {
        return scholarshipType;
    }

    public void setScholarshipType(Integer scholarshipType) {
        this.scholarshipType = scholarshipType;
    }

    public String[] getScholarshipName() {
        return scholarshipName;
    }

    public void setScholarshipName(String[] scholarshipName) {
        this.scholarshipName = scholarshipName;
    }

    public String getLateTime() {
        return lateTime;
    }

    public void setLateTime(String lateTime) {
        this.lateTime = lateTime;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public int[] getId() {
        return id;
    }

    public void setId(int[] id) {
        this.id = id;
    }

    private String timeStamp;

    public String getStartDate() {
        return startDate == null ? super.getBdate() : startDate;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        if (StringUtils.isBlank(timeStamp))
            timeStamp = null;
        this.timeStamp = timeStamp;
    }

    public void setStartDate(String startDate) {
        if (StringUtils.isBlank(startDate))
            startDate = null;
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate == null ? super.getEdate() : endDate;
    }

    public void setEndDate(String endDate) {
        if (StringUtils.isBlank(endDate))
            endDate = null;
        this.endDate = endDate;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }


    public String getPunishType() {
        return punishType;
    }

    public void setPunishType(String punishType) {
        if (StringUtils.isBlank(punishType))
            punishType = null;
        this.punishType = punishType;
    }


    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        if (StringUtils.isBlank(types))
            types = null;
        this.types = types;
    }

    public String[] getTypeArr() {
		/*if(StringUtils.isNotEmpty(this.types)){
			String[] str = types.split(",");
			this.typeArr = str;
			return str;
		}else{
			return null;
		}*/
        return typeArr;
    }

    public void setTypeArr(String[] typeArr) {
        this.typeArr = typeArr;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + ((sql == null) ? 0 : sql.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((punishType == null) ? 0 : punishType.hashCode());
        result = prime * result + ((types == null) ? 0 : types.hashCode());
        result = prime * result + ((lateTime == null) ? 0 : lateTime.hashCode());
        result = prime * result + ((ioflag == null) ? 0 : ioflag.hashCode());
        return super.hashCode() + result;
    }
}
