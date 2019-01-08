package com.ziyun.academic.server;


import com.ziyun.academic.entity.OwnSchoolOrg;
import com.ziyun.academic.vo.Params;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;

@FeignClient(name = "basic-service")
@RequestMapping("/v2/school")
public interface OwnSchoolOrgServer {

    /**
     * 根据条件：查询,获取班级的全部信息
     *
     * @param para 前台传过来的查询条件
     * @return 根据查询条件：获取该条件对应的班级列表
     */
    @RequestMapping("/common/selectBy")
    List<OwnSchoolOrg> selectBy(@RequestBody Params para);

    /**
     * 根据当前参数获得该参数的班级集合
     *
     * @param para 参数中的schoolCode、facultyCode、majorCode、classSelect
     * @return classCode的set集
     */
    @RequestMapping("/common/selectOwnClasscode")
    Set<String> selectOwnClasscode(Params para);
}
