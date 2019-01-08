package com.ziyun.auth.controller;


import com.ziyun.auth.constant.Constant;
import com.ziyun.auth.service.IDataPermissionService;
import com.ziyun.common.entity.DataPermission;
import com.ziyun.common.response.CommonResponse;
import com.ziyun.common.tools.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 数据权限相关api
 * Created by Zeng on 2018/4/23.
 */
@Api(tags = "数据权限", description = "数据权限相关api")
@RestController
@RequestMapping("/v2/dataPermission")
public class DataPermissionController {

    @Autowired
    IDataPermissionService dataPermissionService;

    /**
     * 获取所有权限树
     *
     * @param
     * @return 返回该权限下所有权限树形结构，如果id为0则返回所有。
     */
    @ApiOperation(value = "获取数据权限树", notes = "返回该权限下所有权限树形结构，如果id为0则返回所有", httpMethod = "POST")
    @RequestMapping(value = "/getPermissionTree", method = RequestMethod.POST)
    public CommonResponse getAllPermissionTree(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        List<DataPermission> funcPermissionTree = dataPermissionService.getDataPermissionTree(params);
        return CommonResponse.success(funcPermissionTree);
    }

    /**
     * 获取数据权限
     *
     * @param id 数据权限id
     * @return 数据权限
     */
    @ApiOperation(value = "获取数据权限", notes = "通过权限id查找权限", httpMethod = "GET")
    @ApiImplicitParam(paramType = "query", name = "id", value = "权限id", required = true, dataType = "Long")
    @RequestMapping(value = "/getPermission", method = RequestMethod.GET)
    public CommonResponse getPermission(Long id) {
        DataPermission permission = dataPermissionService.selectByKey(id);
        return CommonResponse.success(permission);
    }

    /**
     * 获取对应用户数据权限
     *
     * @param userId 用户id
     * @return 数据权限
     */
    @ApiOperation(value = "获取用户对应数据权限", notes = "通过用户id查找权限", httpMethod = "GET")
    @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "Long")
    @RequestMapping(value = "/getPermissionByUserId", method = RequestMethod.GET)
    public CommonResponse getPermissionByUser(Long userId) {
        List<DataPermission> permissions = dataPermissionService.listDataPerm(userId);
        return CommonResponse.success(permissions);
    }

    /**
     * 修改数据权限
     *
     * @param permission 数据权限
     * @return 影响行数
     */
    @ApiOperation(value = "修改数据权限", notes = "修改数据权限", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "权限id", required = true),
            @ApiImplicitParam(paramType = "query", name = "parentId", value = "上级权限id"),
            @ApiImplicitParam(paramType = "query", name = "code", value = "权限唯一标识"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "权限名称"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "权限状态(0.禁用;1.启用)"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "权限描述")
    })
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResponse update(DataPermission permission) {
        int result = dataPermissionService.updateNotNull(permission);
        return CommonResponse.success(result);
    }

    /**
     * 修改数据权限状态
     *
     * @param request
     * @return 影响行数
     */
    @ApiOperation(value = "修改数据权限状态", notes = "启用/禁用", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "权限id", required = true),
            @ApiImplicitParam(paramType = "query", name = "status", value = "权限状态(0.禁用;1.启用)")
    })
    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    public CommonResponse changeStatus(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        int result = dataPermissionService.changeStatus(params);
        return CommonResponse.success(result);
    }

    /**
     * 删除数据权限
     *
     * @param id 数据权限id
     * @return 影响行数
     */
    @ApiOperation(value = "删除数据权限", notes = "通过权限id删除对应权限")
    @ApiImplicitParam(paramType = "query", name = "id", value = "权限id", required = true, dataType = "Long")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public CommonResponse deletePermission(Long id) {
        DataPermission permission = new DataPermission();
        permission.setId(id);
        permission.setDeleted(Constant.INT_ZERO);
        int result = dataPermissionService.updateNotNull(permission);
        return CommonResponse.success(result);
    }
}
