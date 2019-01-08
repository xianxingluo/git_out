package com.ziyun.basic.server;

import com.ziyun.basic.entity.SysorgTree;
import com.ziyun.basic.vo.Params;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2018/1/23.
 */
public interface UserDefinedServer {
    Map getClasscodeByDefined(Params params);

    List<SysorgTree> getOrgans(Params params);

    List<SysorgTree> getDataAuthTree(Params params);

    List<Map<String, Object>> getEnrollmentYear();
}
