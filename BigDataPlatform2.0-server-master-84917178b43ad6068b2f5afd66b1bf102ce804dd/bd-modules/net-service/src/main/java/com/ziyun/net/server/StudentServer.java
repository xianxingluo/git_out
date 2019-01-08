package com.ziyun.net.server;

import com.ziyun.net.vo.NetParams;
import com.ziyun.net.vo.Params;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@FeignClient(name = "basic-service")
@RequestMapping("/v2/student")
public interface StudentServer {

	/**
	 * 根据参数：分页查询：学生信息：
	 *
	 * @param para
	 * @return
	 */
	@RequestMapping("/common/ipagestudent")
	List<LinkedHashMap<String, Object>> getPageStudent(@RequestBody Params para);

	//	TODO 此处未完成
	List<Map<String, Object>> getStudentDetails(@RequestBody Params params);

}
