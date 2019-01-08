package com.ziyun.tracking.controller;

import com.ziyun.common.response.CommonResponse;
import com.ziyun.tracking.server.EventTrackingServer;
import com.ziyun.common.tools.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @description: 埋点控制器
 * @author: FubiaoLiu
 * @date: 2018/9/25
 */
@Controller
@Api(tags = "数据埋点", description = "数据埋点")
@RequestMapping("/v2/eventTracking")
public class EventTrackingController {

    @Autowired
    public EventTrackingServer eventTrackingServer;

    /**
     * 保存埋点数据
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/save")
    @ApiOperation(value = "保存数据", notes = "保存埋点数据", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", paramType = "query", dataType = "String", required = true, value = "token"),
            @ApiImplicitParam(name = "code", paramType = "query", dataType = "String", required = true, value = "点击元素编码"),
            @ApiImplicitParam(name = "name", paramType = "query", dataType = "String", required = true, value = "点击元素名称"),
            @ApiImplicitParam(name = "type", paramType = "query", dataType = "String", required = true, value = "点击元素类型"),
    })
    public CommonResponse save(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        eventTrackingServer.save(params);
        return CommonResponse.success();
    }

}