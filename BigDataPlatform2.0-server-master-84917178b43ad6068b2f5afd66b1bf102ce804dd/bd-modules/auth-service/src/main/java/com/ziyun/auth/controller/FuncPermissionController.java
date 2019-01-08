package com.ziyun.auth.controller;

import com.ziyun.auth.constant.Constant;
import com.ziyun.auth.enums.MenuType;
import com.ziyun.auth.service.IFuncPermissionService;
import com.ziyun.common.entity.FuncPermission;
import com.ziyun.common.enums.StatusCodeEnum;
import com.ziyun.common.response.CommonResponse;
import com.ziyun.common.tools.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @description: 功能权限相关api
 * @author: FubiaoLiu
 * @date: 2018/11/20
 */
@Api(tags = "功能权限", description = "功能权限相关api")
@RestController
@RequestMapping("/v2/funcPermission")
public class FuncPermissionController {
    @Autowired
    private IFuncPermissionService funcPermissionService;

    @ResponseBody
    @RequestMapping(value = "/getAllValidPermissions", method = RequestMethod.POST)
    @ApiOperation(value = "获取所有有效权限", notes = "获取所有有效权限(系统鉴权使用，与用户无关)", httpMethod = "POST")
    public List<FuncPermission> getAllValidPermissions() {
        return funcPermissionService.getAllValidPermissions();
    }

    /**
     * 获取所有权限树
     *
     * @param
     * @return 返回该权限下所有权限树形结构，如果id为0则返回所有。
     */
    @ApiOperation(value = "获取权限树", notes = "返回该权限下所有权限树形结构，如果id为0则返回所有", httpMethod = "POST")
    @RequestMapping(value = "/getPermissionTree", method = RequestMethod.POST)
    public CommonResponse getPermissionTree(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        List<FuncPermission> funcPermissionTree = funcPermissionService.getFuncPermissionTree(params);
        return CommonResponse.success(funcPermissionTree);
    }

    /**
     * 获取权限
     *
     * @param id 功能权限id
     * @return 功能权限
     */
    @ApiOperation(value = "获取权限", notes = "通过权限id查找权限", httpMethod = "GET")
    @ApiImplicitParam(paramType = "query", name = "id", value = "权限id", required = true, dataType = "Long")
    @RequestMapping(value = "/getPermission", method = RequestMethod.GET)
    public CommonResponse getPermission(Long id) {
        FuncPermission permission = funcPermissionService.selectByKey(id);
        return CommonResponse.success(permission);
    }

    /**
     * 修改权限
     *
     * @param permission 功能权限
     * @return 影响行数
     */
    @ApiOperation(value = "修改功能权限", notes = "修改功能权限", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "权限id", required = true),
            @ApiImplicitParam(paramType = "query", name = "parentId", value = "上级权限id"),
            @ApiImplicitParam(paramType = "query", name = "code", value = "权限唯一标识"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "权限名称"),
            @ApiImplicitParam(paramType = "query", name = "menuType", value = "权限类型(0.目录;1.菜单;2.按钮;3.图表;9.列表总数)"),
            @ApiImplicitParam(paramType = "query", name = "url", value = "权限url"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "权限状态(0.禁用;1.启用)"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "权限描述")
    })
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResponse update(FuncPermission permission) {
        int result = funcPermissionService.updateNotNull(permission);
        return CommonResponse.success(result);
    }

    /**
     * 修改功能权限状态
     *
     * @param permission 功能权限
     * @return 影响行数
     */
    @ApiOperation(value = "修改功能权限状态", notes = "启用/禁用", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "权限id", required = true),
            @ApiImplicitParam(paramType = "query", name = "status", value = "权限状态(0.禁用;1.启用)")
    })
    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    public CommonResponse changeStatus(FuncPermission permission) {
        int result = funcPermissionService.changeStatus(permission);
        return CommonResponse.success(result);
    }

    /**
     * 插入功能权限
     *
     * @param permission 功能权限
     * @return 影响行数
     */
    @ApiOperation(value = " 插入功能权限", notes = " 插入功能权限", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "parentId", value = "上级权限id", required = true),
            @ApiImplicitParam(paramType = "query", name = "code", value = "权限唯一标识", required = true),
            @ApiImplicitParam(paramType = "query", name = "name", value = "权限名称", required = true),
            @ApiImplicitParam(paramType = "query", name = "menuType", value = "权限类型(0.目录;1.菜单;2.按钮;3.图表;9.列表总数)", required = true),
            @ApiImplicitParam(paramType = "query", name = "url", value = "权限url"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "权限状态(0.禁用;1.启用)"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "权限描述")
    })
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResponse savePermission(FuncPermission permission) {
        if (null == permission.getParentId() || null == permission.getCode() || null == permission.getMenuType()) {
            return CommonResponse.failure(StatusCodeEnum.INVALID_PARAM_ERROR);
        }
        FuncPermission parent = funcPermissionService.selectByKey(permission.getParentId());
        if (null == parent) {
            return CommonResponse.failure("上级权限不存在！");
        }
        permission.setLevel(parent.getLevel() + 1);
        int save = funcPermissionService.save(permission);
        return CommonResponse.success(save);
    }

    /**
     * 删除功能权限
     *
     * @param id 功能权限id
     * @return 功能权限
     */
    @ApiOperation(value = "删除功能权限", notes = "通过权限id删除对应权限")
    @ApiImplicitParam(paramType = "query", name = "id", value = "权限id", required = true, dataType = "Long")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public CommonResponse deletePermission(Long id) {
        FuncPermission permission = new FuncPermission();
        permission.setId(id);
        permission.setDeleted(Constant.INT_ZERO);
        int result = funcPermissionService.updateNotNull(permission);
        return CommonResponse.success(result);
    }

    /**
     * 查看某个用户所拥有的功能权限
     */
    @ApiOperation(value = "获取某个用户所拥有权限", notes = "获取某个用户所拥有权限", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "Long"),

    })
    @RequestMapping(value = "/getPermissionByUserId", method = RequestMethod.GET)
    public CommonResponse getPermissionByUserId(Long userId) {
        List<FuncPermission> permissions = funcPermissionService.selectPermissionByUser(userId);
        return CommonResponse.success(permissions);
    }

    /**
     * 权限菜单树
     */
    @RequestMapping(value = "/getPermissionByModule", method = RequestMethod.POST)
    @ApiOperation(value = "查询模块权限菜单树", notes = "查询模块权限菜单树")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", required = true, value = "token"),
            @ApiImplicitParam(paramType = "query", name = "name", dataType = "String", value = "菜单name(模糊搜索)"),
            @ApiImplicitParam(paramType = "query", name = "module", dataType = "String", required = true, value = "模块")
    })
    public CommonResponse getPermissionByModule(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        if (null == params.get(Constant.FIELD_MODULE)) {
            return CommonResponse.failure(StatusCodeEnum.INVALID_PARAM_ERROR);
        }
        List<FuncPermission> permissionList = getPermission(params);
        if (null == permissionList || permissionList.size() != 1) {
            return CommonResponse.failure(StatusCodeEnum.DATA_ABNORMITY_ERROR);
        }
        params.put("parentId", permissionList.get(0).getId());
        List<FuncPermission> list = funcPermissionService.getPermissionByModule(params);
        return CommonResponse.success(list);
    }

    /**
     * 权限菜单树
     */
    @RequestMapping(value = "/getPermissionOfChart", method = RequestMethod.POST)
    @ApiOperation(value = "查询图表库权限菜单树", notes = "查询图表库权限菜单树")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", required = true, value = "token"),
            @ApiImplicitParam(paramType = "query", name = "module", dataType = "String", required = true, value = "模块")
    })
    public CommonResponse getPermissionOfChart(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        if (null == params.get(Constant.FIELD_MODULE)) {
            return CommonResponse.failure(StatusCodeEnum.INVALID_PARAM_ERROR);
        }
        List<FuncPermission> permissionList = getPermission(params);
        if (null == permissionList || permissionList.size() != 1) {
            return CommonResponse.failure(StatusCodeEnum.DATA_ABNORMITY_ERROR);
        }
        params.put("parentId", permissionList.get(0).getId());
        List<FuncPermission> list = funcPermissionService.getPermissionOfChart(params);
        return CommonResponse.success(list);
    }

    /**
     * 根据module(即权限菜单对应code)获取对应权限
     *
     * @param params
     * @return
     */
    private List<FuncPermission> getPermission(Map<String, Object> params) {
        Example example = new Example(FuncPermission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("code", params.get(Constant.FIELD_MODULE).toString());
        criteria.andEqualTo("menuType", MenuType.FOLDER.getKey());

        return funcPermissionService.selectByExample(example);
    }
}
