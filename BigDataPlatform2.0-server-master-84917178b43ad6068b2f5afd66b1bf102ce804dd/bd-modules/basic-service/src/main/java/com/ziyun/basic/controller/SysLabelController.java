package com.ziyun.basic.controller;

import com.ziyun.basic.entity.SysLabel;
import com.ziyun.basic.server.SysLabelServer;
import com.ziyun.basic.tools.WebUtil;
import com.ziyun.common.constant.Constant;
import com.ziyun.utils.requests.CommResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 系统标签控制类
 * @author: FubiaoLiu
 * @date: 2018/9/26
 */
@RestController
@Api(tags = "系统标签管理模块", description = "系统标签管理模块相关api")
@RequestMapping("/v2/label")
public class SysLabelController {
    @Autowired
    private SysLabelServer sysLabelServer;

    @RequestMapping(value = "/getLabelList", method = RequestMethod.POST)
    @ApiOperation(value = "获取标签列表", notes = "获取标签列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", required = true, value = "token"),
            @ApiImplicitParam(paramType = "query", name = "type", dataType = "String", value = "标签分类")
    })
    public CommResponse getLabelList(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        Map<String, Object> resultMap = new HashMap<>(2);
        List<SysLabel> resultList = sysLabelServer.getLabelList(params);
        /**
         * enableState:0.一键启用按钮不可用(置灰);1.一键启用按钮可用
         */
        int enableState = 0;
        if (CollectionUtils.isNotEmpty(resultList)) {
            for (SysLabel label : resultList) {
                if (label.getStatus() == 0) {
                    enableState = 1;
                }
            }
        }
        resultMap.put("enableState", enableState);
        resultMap.put("list", resultList);
        return CommResponse.success(resultMap);
    }

    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    @ApiOperation(value = "更改标签状态", notes = "启用、禁用")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", required = true, value = "token"),
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "Long", required = true, value = "标签ID"),
            @ApiImplicitParam(paramType = "query", name = "status", dataType = "Integer", required = true, value = "状态(0.禁用;1.启用)")
    })
    public CommResponse updateStatus(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        if (null == params.get(Constant.FIELD_ID) || null == params.get(Constant.FIELD_STATUS)) {
            return CommResponse.failure("参数异常，请联系管理员！");
        }
        sysLabelServer.updateStatus(params);
        return CommResponse.success("操作成功！");
    }

    @RequestMapping(value = "/enableAll", method = RequestMethod.POST)
    @ApiOperation(value = "一键启用标签", notes = "启用所有标签")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", required = true, value = "token"),
            @ApiImplicitParam(paramType = "query", name = "type", dataType = "String", value = "标签分类")
    })
    public CommResponse enableAll(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        sysLabelServer.enableAll(params);
        return CommResponse.success("操作成功！");
    }

    @RequestMapping(value = "/getLabelTypeList", method = RequestMethod.POST)
    @ApiOperation(value = "获取标签类型列表", notes = "获取标签类型列表")
    @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", required = true, value = "token")
    public CommResponse getLabelTypeList() {
        return CommResponse.success(sysLabelServer.getLabelTypeList());
    }
}
