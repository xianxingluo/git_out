package com.ziyun.borrow.service;

import com.ziyun.common.model.OwnSchoolOrg;
import com.ziyun.common.model.Params;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "basic-service")
@RequestMapping("/v2/school")
public interface OwnOrgSchoolService {

    /**
     * 根据参数获取所有班级信息     -- 暂不确定是否是有调用
     *
     * @param para
     * @return
     */
    @RequestMapping("/common/selectBy")
    List<OwnSchoolOrg> selectBy(@RequestBody Params para);

}
