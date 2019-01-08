package com.ziyun.academic.server;


import com.ziyun.academic.entity.OwnOrgStree;
import com.ziyun.academic.vo.Params;
import com.ziyun.academic.vo.ParamsStatus;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * 组织结构树，业务service接口
 */
@FeignClient(name = "basic-service")
@RequestMapping("/v2/ownOrgTree")
public interface IOwnOrgTreeService {

    @RequestMapping("/common/getPcodeByCcode")
    OwnOrgStree getPcodeByCcode(@RequestBody Params params);

    @RequestMapping("/common/getChildrenList")
    List<Map> getChildrenList(@RequestBody Params params);

    @RequestMapping("/common/getAllTopCategory")
    List<Map> getAllTopCategory(@RequestBody Params params);
}
