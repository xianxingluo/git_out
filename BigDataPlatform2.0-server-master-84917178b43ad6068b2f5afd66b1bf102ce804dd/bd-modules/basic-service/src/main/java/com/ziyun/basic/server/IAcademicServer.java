package com.ziyun.basic.server;


import com.ziyun.basic.vo.AcademicParams;
import com.ziyun.basic.vo.Params;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Map;

@FeignClient(name = "academic-service")
@RequestMapping("/v2/academic")
public interface IAcademicServer {
    @RequestMapping("/common/featuresAcademic")
    Map<String, Object> featureAcademic(@RequestBody Params para) throws IOException;

    @RequestMapping("/common/getAcademicLabel")
    Map<String, Object> getAcademicLabel(@RequestBody Params params);
}
