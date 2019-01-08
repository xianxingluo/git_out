package com.ziyun.borrow.service;

import com.ziyun.borrow.model.OwnOrgStudent;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "basic-service")
@RequestMapping("/v2/student")
public interface OwnOrgStudentService {

    /**
     * 根据学号查询学生信息
     *
     * @param outid
     * @return
     */
    @RequestMapping("/common/selectByPrimaryKey")
    OwnOrgStudent selectByPrimaryKey(@RequestBody String outid);

}
