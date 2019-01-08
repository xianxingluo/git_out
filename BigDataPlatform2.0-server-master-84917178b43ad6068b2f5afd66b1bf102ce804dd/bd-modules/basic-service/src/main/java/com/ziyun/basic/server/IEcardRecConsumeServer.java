package com.ziyun.basic.server;


import com.ziyun.basic.vo.Params;
import com.ziyun.basic.vo.ParamsStatus;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @Description: 消费行为分析
 * @Created by liquan
 * @date 2017年5月13日 上午11:39:31
 */
@FeignClient(name = "consume-service")
@RequestMapping("/v2/consume")
public interface IEcardRecConsumeServer {

    @RequestMapping("/common/avgConsumeByDay")
    Map avgConsumeByDay(Params params) throws Exception;

    @RequestMapping("/common/preferenceList")
    List<Map<String, Object>> preferenceList(@RequestBody Params params) throws Exception;

    @RequestMapping("/common/sumCollect")
    Map sumCollect(@RequestBody Params params) throws Exception;

    @RequestMapping("/common/consumeFeature")
    Map<String, Object> getConsumeFeature(@RequestBody ParamsStatus param) throws Exception;

}
