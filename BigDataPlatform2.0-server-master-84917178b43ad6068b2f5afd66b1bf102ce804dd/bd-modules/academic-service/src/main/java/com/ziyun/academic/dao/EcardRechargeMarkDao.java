package com.ziyun.academic.dao;


import com.ziyun.academic.entity.EcardRechargeMark;
import com.ziyun.academic.vo.EcardRechargeMarkVo;

import java.util.List;

/**
 * 充值记录计算记录表：：记录每次计算到学生的哪个卡，哪个消费记录；用于下次查询的时候继续查；不用每次查询全部记录
 * <p>
 * 每次记录最后查询到的记录；下次查询的时候直接查询下次操作记录（或者下张卡的记录）；如果有则跟当前记录比较，总额大于上次的余额，则是充值记录
 *
 * @Description:
 * @Created by liquan
 * @date 2017年5月22日 上午11:13:02
 */
public interface EcardRechargeMarkDao {
    int deleteByPrimaryKey(Long id);

    int insert(EcardRechargeMark record);

    int insertSelective(EcardRechargeMark record);

    EcardRechargeMark selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EcardRechargeMark record);

    int updateByPrimaryKey(EcardRechargeMark record);

    /**
     * 查询出准备要进行充值计算的m_recharge_mark表数据：1、包含mark表中不存在的学生记录；这些记录要按照默认设置插入到mark表中
     * ，从而定时任务能进行计算学生的充值
     * <p>
     * student_outid是关联查询出的edu_status表的outid学号字段
     * ：如果mark表没有该学号的记录，通过该字段给新增一个默认的
     *
     * @param record
     * @return
     */
    List<EcardRechargeMarkVo> needToRechargeMark(EcardRechargeMark record);


    EcardRechargeMark selectByOutid(String outid);//根据学号查询

}