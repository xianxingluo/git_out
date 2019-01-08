package com.ziyun.consume.server;


import com.ziyun.consume.entity.OwnOrgStudent;
import com.ziyun.consume.entity.OwnOrgStudentType;
import com.ziyun.consume.vo.Params;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@FeignClient(name = "basic-service")
@RequestMapping("/v2/student")
public interface StudentServer {

    @RequestMapping("/common/getStudents")
    List<Map<String, Object>> getStudents(@RequestBody Params params);

    /**
     * 获取学生总人数
     *
     * @param params
     * @return
     */
    @RequestMapping("/common/getStudentSize")
    Long getStudentSize(@RequestBody Params params);

    @RequestMapping("/common/selectAllDetail")
    List<OwnOrgStudentType> selectAllDetail(@RequestBody Params params);

    @RequestMapping("/common/selectByPrimaryKey")
    OwnOrgStudent selectByPrimaryKey(@RequestBody String outid);
}
