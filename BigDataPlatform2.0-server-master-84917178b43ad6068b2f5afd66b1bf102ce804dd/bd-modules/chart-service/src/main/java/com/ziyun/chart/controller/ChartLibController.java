package com.ziyun.chart.controller;

import com.ziyun.chart.entity.ChartRelation;
import com.ziyun.chart.service.ChartRelationService;
import com.ziyun.common.constant.Constant;
import com.ziyun.common.enums.StatusCodeEnum;
import com.ziyun.common.response.CommonResponse;
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
 * @description: 图表库控制器
 * @author: FubiaoLiu
 * @date: 2018/9/25
 */
@Controller
@Api(tags = "图表库", description = "图表库")
@RequestMapping("/v2/chartLib")
public class ChartLibController {

    @Autowired
    public ChartRelationService chartRelationService;

    /**
     * 保存模块图表关系
     *
     * @param relation
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveChartRelation")
    @ApiOperation(value = "保存模块图表关系", notes = "保存模块图表关系", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", paramType = "query", dataType = "String", required = true, value = "token"),
            @ApiImplicitParam(name = "code", paramType = "query", dataType = "String", required = true, value = "模块编码"),
            @ApiImplicitParam(name = "chartType", paramType = "query", dataType = "String", required = true, value = "图表类型(00. 柱状图;01. 折线图;02. 饼状图;03. 仪表盘)")
    })
    public CommonResponse saveChartRelation(ChartRelation relation) {
        if (null == relation.getCode() || null == relation.getChartType()) {
            return CommonResponse.failure("请选择模块和图表类型！");
        }
        chartRelationService.save(relation);
        return CommonResponse.success("保存成功！");
    }

    /**
     * 获取模块图表关系
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getChartRelation")
    @ApiOperation(value = "获取模块图表类型", notes = "保存模块图表类型", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", paramType = "query", dataType = "String", required = true, value = "token"),
            @ApiImplicitParam(name = "code", paramType = "query", dataType = "String", required = true, value = "模块编码")
    })
    public CommonResponse getChartRelation(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        if (null == params.get(Constant.FIELD_CODE)) {
            return CommonResponse.failure(StatusCodeEnum.INVALID_PARAM_ERROR);
        }
        ChartRelation relation = chartRelationService.selectByCode(params.get(Constant.FIELD_CODE).toString());
        if (null == relation) {
            return CommonResponse.failure("请配置图表类型！");
        }
        return CommonResponse.success(relation);
    }

}