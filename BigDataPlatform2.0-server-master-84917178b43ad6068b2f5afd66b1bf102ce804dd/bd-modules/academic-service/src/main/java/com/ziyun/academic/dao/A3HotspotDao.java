package com.ziyun.academic.dao;


import com.ziyun.academic.entity.A3Hotspot;
import com.ziyun.academic.vo.Params;
import com.ziyun.academic.vo.ResultData;

import java.util.List;
import java.util.Map;

public interface A3HotspotDao {
    A3Hotspot selectByPrimaryKey(Integer id);

    List<A3Hotspot> listHotspot(Params para);

    /**
     * 1、上网访问内容top10
     *
     * @param para
     * @return
     */
    List<Map> hotTypeList(Params para);

    /**
     * {上网行为分析}4、上网热力情况:统计上网分类：频次
     *
     * @param para
     * @return
     */
    List<ResultData> hotList(Params para);

    /**
     * 新的上网热力：{上网行为分析}4、上网热力情况:只统计学校关系的分类
     *
     * @param para
     * @return
     */
    List<ResultData> hotListOn(Params para);

    /**
     * {上网行为分析}8、上网内容类型{修改为按照人数：统计具体应用}:top10显示，其他都合并到“其他”里面去
     *
     * @param para
     * @return
     */
    List<ResultData> preferenceList(Params para);

    /**
     * {上网行为分析}8、上网内容类型 给表格导出提供详细数据
     *
     * @param para
     * @return
     */
    List<ResultData> preferenceList4Excel(Params para);

    /**
     * 新增的 8、上网内容类型{修改为按照人数：统计学校关注的几个分类：例如游戏、炒股等}
     *
     * @param para
     * @return
     */
    List<ResultData> preferenceTypeList(Params para);

    List<ResultData> preferenceTypeList4Excel(Params para);

    /**
     * {学生行为分析}3.1、上网偏好
     * <p>
     * top4+其他
     *
     * @param para
     * @return
     */
    List<ResultData> preferenceOtherList(Params para);

    /**
     * {学生行为分析}3.2、周一到周末的：上网偏好
     * <p>
     * top4+其他
     *
     * @param para
     * @return
     */
    List<ResultData> weekPreferenceOtherList(Params para);

    /**
     * {学生行为分析}3.2、周一到周末的：上网偏好{记录数}:最早、最晚记录时间（用于计算周几的平均）
     * <p>
     * 用于按照学号查询的时候：来确定统计的起始、结束日期
     *
     * @param para
     * @return
     */
    ResultData weekPreferenceOtherTimes(Params para);

    /**
     * {首页} 今日访问行为
     *
     * @param para
     * @return
     */
    List<A3Hotspot> listToday(Params para);

}