package com.ziyun.academic.tools;

/**
 * 数据库中周几的序号和对应的中文名称
 * <p>
 * 数据库中周几的序号是从：周日0-周六6
 *
 * @Description:
 * @Created by liquan
 * @date 2017年5月18日 上午9:27:56
 */
public class Week {

    /**
     * 周几在中文中排序
     */
    int cn_index;

    /**
     * 周几在英文中排序
     */
    int en_index;
    /**
     * 中文名称
     */
    String cn_name;
    /**
     * 周几出现的次数
     */
    int count;

    public Week(int cn_index, int en_index, String cn_name) {
        super();
        this.cn_index = cn_index;
        this.en_index = en_index;
        this.cn_name = cn_name;
    }

    public int getEn_index() {
        return en_index;
    }

    public void setEn_index(int en_index) {
        this.en_index = en_index;
    }

    public String getCn_name() {
        return cn_name;
    }

    public void setCn_name(String cn_name) {
        this.cn_name = cn_name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCn_index() {
        return cn_index;
    }

    public void setCn_index(int cn_index) {
        this.cn_index = cn_index;
    }

}
