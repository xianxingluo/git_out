package com.ziyun.consume.vo;

/**
 * 消费 params
 */
public class ConsumeParams extends Params {
    /**
     * 消费类目详细模块：查询方式，区分调用service的方法
     */
    private Integer queryType;

    /**
     * 餐饮窗口名称
     */
    private String windowName;

    /**
     * 餐厅
     *
     * @return
     */
    private String topName;

    /**
     * 餐饮
     *
     * @return
     */
    private String dptName;

    public Integer getQueryType() {
        return queryType;
    }

    public void setQueryType(Integer queryType) {
        this.queryType = queryType;
    }

    public String getWindowName() {
        return windowName;
    }

    public void setWindowName(String windowName) {
        this.windowName = windowName;
    }

    public String getTopName() {
        return topName;
    }

    public void setTopName(String topName) {
        this.topName = topName;
    }

    public String getDptName() {
        return dptName;
    }

    public void setDptName(String dptName) {
        this.dptName = dptName;
    }
}
