package com.ziyun.academic.server;

import java.util.List;
import java.util.Map;

/**
 * 消费行为分析:计算充值记录
 *
 * @Description:
 * @Created by liquan
 * @date 2017年5月22日 下午3:16:20
 */
public interface IEcardRechargeServer {

    /**
     * task:循环所有学生，计算充值记录｛根据m_recharge_mark｝
     *
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> taskRecharge() throws Exception;

}
