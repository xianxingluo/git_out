package com.ziyun.academic.server;

import com.ziyun.academic.entity.OwnOrgStudent;
import com.ziyun.academic.vo.AcademicParams;
import com.ziyun.academic.vo.Params;
import com.ziyun.utils.requests.CommResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@FeignClient(name = "basic-service")
@RequestMapping("/v2/student")
public interface IStudentServer {

    @RequestMapping("/common/selectByPrimaryKey")
    OwnOrgStudent selectByPrimaryKey(@RequestBody String outid);

    @RequestMapping(value = "/common/pagestudent")
    CommResponse getPageStudent(@RequestBody Params para) throws Exception;

    @RequestMapping("/common/studentsCount")
    CommResponse studentsCount(@RequestBody Params params);

    @RequestMapping("/common/getEnrollmentYearById")
    String getEnrollmentYearById(@RequestBody Params params);

    @RequestMapping("/common/selectByOutid")
    Map selectByOutid(@RequestBody String outid);

    @RequestMapping("/common/getStudentOutids")
    List<String> getStudentOutids(@RequestBody AcademicParams params);
}
