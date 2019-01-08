package com.ziyun.auth.controller;

import com.ziyun.auth.constant.Constant;
import com.ziyun.auth.enums.MenuType;
import com.ziyun.auth.model.UserMenu;
import com.ziyun.auth.service.IUserMenuService;
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
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @description: 用户自定义菜单
 * @author: FubiaoLiu
 * @date: 2018/9/19
 */
@Api(tags = "用户自定义菜单模块", description = "用户自定义菜单模块相关api")
@RestController
@RequestMapping("/v2/userMenu")
public class UserMenuController {

    @Autowired
    private IUserMenuService userMenuService;

    /**
     * 获取自定义菜单树(新增文件夹)
     */
    @RequestMapping(value = "/menuTreeOfFolder", method = RequestMethod.POST)
    @ApiOperation(value = "(新增文件夹)自定义菜单目录树", notes = "(新增文件夹)自定义菜单目录树")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", required = true, value = "token"),
            @ApiImplicitParam(paramType = "query", name = "module", dataType = "String", required = true, value = "模块")
    })
    public CommonResponse menuTreeOfFolder(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        if (null == params.get(Constant.FIELD_MODULE)) {
            return CommonResponse.failure(StatusCodeEnum.INVALID_PARAM_ERROR);
        }
        return CommonResponse.success(userMenuService.userMenuTree(params));
    }

    /**
     * 获取自定义菜单树(新增文件)
     */
    @RequestMapping(value = "/menuTreeOfFile", method = RequestMethod.POST)
    @ApiOperation(value = "(新增文件)自定义菜单目录树", notes = "(新增文件)自定义菜单目录树")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", required = true, value = "token"),
            @ApiImplicitParam(paramType = "query", name = "module", dataType = "String", required = true, value = "模块")
    })
    public CommonResponse menuTreeOfFile(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        if (null == params.get(Constant.FIELD_MODULE)) {
            return CommonResponse.failure(StatusCodeEnum.INVALID_PARAM_ERROR);
        }
        return CommonResponse.success(userMenuService.menuTreeOfFile(params));
    }

    /**
     * 自定义菜单树
     */
    @RequestMapping(value = "/menuTreeOfReport", method = RequestMethod.POST)
    @ApiOperation(value = "(分析报告)自定义菜单树(作废)", notes = "(分析报告)自定义菜单树(作废)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", required = true, value = "token"),
            @ApiImplicitParam(paramType = "query", name = "name", dataType = "String", value = "菜单name(模糊搜索)"),
            @ApiImplicitParam(paramType = "query", name = "module", dataType = "String", required = true, value = "模块(暂时默认为03)")
    })
    public CommonResponse menuTreeOfReport(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        if (null == params.get(Constant.FIELD_MODULE)) {
            return CommonResponse.failure(StatusCodeEnum.INVALID_PARAM_ERROR);
        }
        return CommonResponse.success(userMenuService.menuTreeOfReport(params));
    }

    /**
     * 保存自定义菜单
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "保存自定义菜单", notes = "保存自定义菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", required = true, value = "token"),
            @ApiImplicitParam(paramType = "query", name = "name", dataType = "String", required = true, value = "文件夹名称"),
            @ApiImplicitParam(paramType = "query", name = "parentId", dataType = "Long", value = "父级ID"),
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "Long", value = "ID(修改时必须)"),
            @ApiImplicitParam(paramType = "query", name = "menuType", dataType = "Integer", value = "菜单类型(0.文件夹；1.文件)"),
            @ApiImplicitParam(paramType = "query", name = "module", dataType = "String", required = true, value = "模块")
    })
    public CommonResponse save(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        Object id = params.get(Constant.FIELD_ID);
        Object parentId = params.get(Constant.FIELD_PARENT_ID);
        Object module = params.get(Constant.FIELD_MODULE);
        Object menuType = params.get(Constant.FIELD_MENU_TYPE);
        boolean paramInvalidFlag = (null == module || (null == id && (null == menuType || null == parentId)));
        if (paramInvalidFlag) {
            return CommonResponse.failure(StatusCodeEnum.INVALID_PARAM_ERROR);
        }

        if (null == params.get(Constant.FIELD_NAME)) {
            return CommonResponse.failure("文件夹名称不能为空！");
        }
        List<UserMenu> list = userMenuService.queryMenuByName(params);
        if (null != list) {
            if ((null == id && !list.isEmpty())
                    || (null != id
                    && (list.size() > 1
                    || (list.size() == 1
                    && !list.get(0).getId().toString().equals(id.toString()))))) {
                return CommonResponse.failure("文件或文件夹已存在！");
            } else if (null != id
                    && (list.size() == 1 && list.get(0).getId().toString().equals(id.toString()))) {
                return CommonResponse.success("保存成功！");
            }
        }
        userMenuService.save(params);
        return CommonResponse.success("保存成功！");
    }

    /**
     * 删除自定义菜单
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除自定义菜单", notes = "删除自定义菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", required = true, value = "token"),
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "Long", required = true, value = "ID")
    })
    public CommonResponse delete(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        if (null == params.get(Constant.FIELD_ID)) {
            return CommonResponse.failure(StatusCodeEnum.INVALID_PARAM_ERROR);
        }
        userMenuService.delete(Long.valueOf(params.get(Constant.FIELD_ID).toString()));
        return CommonResponse.success("删除成功！");
    }

    /**
     * 拖拽修改菜单目录、顺序
     */
    @RequestMapping(value = "/change", method = RequestMethod.POST)
    @ApiOperation(value = "拖拽修改菜单目录、顺序", notes = "拖拽修改菜单目录、顺序")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", required = true, value = "token"),
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "Long", required = true, value = "ID"),
            @ApiImplicitParam(paramType = "query", name = "parentId", dataType = "Long", required = true, value = "父级ID"),
            @ApiImplicitParam(paramType = "query", name = "toSeq", dataType = "Long", required = true, value = "目的排序位置(从0开始)"),
            @ApiImplicitParam(paramType = "query", name = "module", dataType = "String", required = true, value = "模块")
    })
    public CommonResponse change(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        Object id = params.get(Constant.FIELD_ID);
        Object parentId = params.get(Constant.FIELD_PARENT_ID);
        if (null == id || null == parentId) {
            return CommonResponse.failure(StatusCodeEnum.INVALID_PARAM_ERROR);
        }
        if (!parentId.toString().equals(Constant.STRING_ZERO)) {
            UserMenu toParentMenu = userMenuService.queryUserMenuById(params);
            if (null == toParentMenu) {
                return CommonResponse.failure("目标文件夹不存在！");
            }
            if (toParentMenu.getMenuType() != MenuType.FOLDER.getKey()) {
                return CommonResponse.failure("不能拖拽到文件！");
            }
        }
        userMenuService.change(params);
        return CommonResponse.success("修改成功！");
    }
}
