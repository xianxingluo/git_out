package com.ziyun.auth.controller;

import com.ziyun.auth.constant.Constant;
import com.ziyun.auth.service.IDataPermissionService;
import com.ziyun.auth.service.IFuncPermissionService;
import com.ziyun.auth.service.IRoleService;
import com.ziyun.auth.service.IUserService;
import com.ziyun.common.constant.AuthConstant;
import com.ziyun.common.entity.DataPermission;
import com.ziyun.common.entity.FuncPermission;
import com.ziyun.common.entity.Role;
import com.ziyun.common.entity.User;
import com.ziyun.common.enums.StatusCodeEnum;
import com.ziyun.common.response.CommonResponse;
import com.ziyun.common.tools.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author leyangjie
 * @Description: 角色Controller
 * @date 2018/4/26 20:54
 */
@Api(tags = "角色模块", description = "角色模块相关api")
@RestController
@RequestMapping("/v2/auth")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IFuncPermissionService funcPermissionService;

    @Autowired
    private IDataPermissionService dataPermissionService;


    @Autowired
    private IUserService userService;

    /**
     * 添加用户
     */
    @RequestMapping(value = "/role/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加角色", notes = "添加角色")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "roleName", dataType = "String", required = true, value = "角色名"),
            @ApiImplicitParam(paramType = "query", name = "remark", dataType = "String", required = true, value = "备注"),
            @ApiImplicitParam(paramType = "query", name = "funcPermissionId", dataType = "Long", allowMultiple = true, required = true, value = "多个功能权限id"),
            @ApiImplicitParam(paramType = "query", name = "dataPermissionId", dataType = "Long", allowMultiple = true, required = true, value = "多个数据权限id"),
            @ApiImplicitParam(paramType = "query", name = "createRoleId", dataType = "Long", required = true, value = "创建角色者id"),
            @ApiImplicitParam(paramType = "query", name = "createUserId", dataType = "Long", required = true, value = "创建用户者id"),
    })
    public CommonResponse addRole(Role role) {
        try {
            int size = roleService.save(role);
            return CommonResponse.success(size);
        } catch (Exception e) {
            return CommonResponse.failure("0");
        }
    }

    /**
     * 删除角色
     */
    @RequestMapping(value = "/role/remove", method = RequestMethod.POST)
    @ApiOperation(value = "删除角色", notes = "删除角色")
    @ApiImplicitParam(paramType = "query", name = "ids", dataType = "Long", allowMultiple = true, required = true, value = "存放多个角色id")
    public CommonResponse removeUser(HttpServletRequest request, Role role) {
        try {
            if (ArrayUtils.isEmpty(role.getIds())) {
                return CommonResponse.failure(StatusCodeEnum.INVALID_PARAM_ERROR);
            }
            List<Role> roleList = roleService.selectByIds(role.getIds());
            if (CollectionUtils.isEmpty(roleList)){
                return CommonResponse.success();
            }
            Map<String, Object> params = WebUtil.getParameterMap(request);
            for (Role r : roleList){
                if (!params.get(AuthConstant.LOGON_PARAM_USER_ID).equals(r.getCreateUserId().toString())
                        || !params.get(AuthConstant.LOGON_PARAM_ROLE_ID).equals(r.getCreateRoleId().toString())) {
                    return CommonResponse.failure(StatusCodeEnum.UNSUPPORTED_OPERATE);
                }
            }
            roleService.deleteRoles(role);
            return CommonResponse.success();
        } catch (Exception e) {
            return CommonResponse.failure();
        }
    }

    /**
     * 查询角色列表
     */
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    @ApiOperation(value = "查询角色列表", notes = "查询角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "roleName", dataType = "String", required = false, value = "根据角色名模糊查询"),
            @ApiImplicitParam(paramType = "query", name = "createRoleId", dataType = "String", required = true, value = "当前角色id"),
            @ApiImplicitParam(paramType = "query", name = "createUserId", dataType = "String", required = true, value = "当前用户id"),
            @ApiImplicitParam(paramType = "query", name = "start", dataType = "Integer", required = true, value = "当前页数"),
            @ApiImplicitParam(paramType = "query", name = "limit", dataType = "Integer", required = true, value = "每页显示的条数")
    })

    public CommonResponse listRoles(Role role) {
        if (roleService.checkIsSuperRole(role.getCreateRoleId())) {
            //TODO 如果用户当前角色是超级管理员角色，则将createUserId，和createRoleId设置为null
            role.setCreateRoleId(null);
            role.setCreateUserId(null);
        }
        List<Role> roleList = roleService.listRoles(role);
        return CommonResponse.success(roleList);
    }

    /**
     * 查询角色列表总长度
     */
    @RequestMapping(value = "/rolesSize", method = RequestMethod.GET)
    @ApiOperation(value = "查询角色列表总长度", notes = "查询角色列表总长度")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "roleName", dataType = "String", required = false, value = "根据角色名模糊查询"),
            @ApiImplicitParam(paramType = "query", name = "createRoleId", dataType = "String", required = true, value = "当前角色id"),
            @ApiImplicitParam(paramType = "query", name = "createUserId", dataType = "String", required = true, value = "当前用户id"),
    })

    public CommonResponse listRolesSize(Role role) {
        if (roleService.checkIsSuperRole(role.getCreateRoleId())) {
            //TODO 如果用户当前角色是超级管理员角色，则将createUserId，和createRoleId设置为null
            role.setCreateRoleId(null);
            role.setCreateUserId(null);
        }
        int size = roleService.listRolesSize(role);
        return CommonResponse.success(size);
    }

    /**
     * 跳转到修改页面
     *
     * @param role
     * @return
     */
    @RequestMapping(value = "/role/toEditPage", method = RequestMethod.POST)
    @ApiOperation(value = "跳转到修改页面", notes = "根据角色id，更改角色跳转到修改页面")
    @ApiImplicitParam(paramType = "query", name = "id", dataType = "Long", required = true, value = "根据角色id，查询角色信息，跳转到修改页面")
    public CommonResponse toEditPage(Role role) {
        return getRoleInfo(role);
    }

    /**
     * 修改用户
     */
    @RequestMapping(value = "/role/update", method = RequestMethod.POST)
    @ApiOperation(value = "更改角色", notes = "根据角色id，更改角色")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "roleId", dataType = "Long", required = true, value = "根据角色id，修改角色"),
            @ApiImplicitParam(paramType = "query", name = "roleName", dataType = "String", required = false, value = "角色名"),
            @ApiImplicitParam(paramType = "query", name = "remark", dataType = "String", required = false, value = "备注"),
            @ApiImplicitParam(paramType = "query", name = "funcPermissionId", dataType = "Long", allowMultiple = true, required = true, value = "多个功能权限id"),
            @ApiImplicitParam(paramType = "query", name = "dataPermissionId", dataType = "Long", allowMultiple = true, required = true, value = "多个数据权限id"),
    })
    public CommonResponse updateRole(HttpServletRequest request, Role role) {
        try {
            if (null == role.getRoleId()) {
                return CommonResponse.failure(StatusCodeEnum.INVALID_PARAM_ERROR);
            }
            Map<String, Object> params = WebUtil.getParameterMap(request);
            Role r = roleService.selectByKey(role.getRoleId());
            if (!params.get(AuthConstant.LOGON_PARAM_USER_ID).equals(r.getCreateUserId().toString())
                    || !params.get(AuthConstant.LOGON_PARAM_ROLE_ID).equals(r.getCreateRoleId().toString())) {
                return CommonResponse.failure(StatusCodeEnum.UNSUPPORTED_OPERATE);
            }
            roleService.updateRole(role);
            return CommonResponse.success("修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return CommonResponse.failure("修改失败！");
        }
    }

    /**
     * 查询角色下的用户列表
     */
    @RequestMapping(value = "/role/listUser", method = RequestMethod.GET)
    @ApiOperation(value = "查询角色下的用户列表", notes = "查询角色下的用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "Long", required = true, value = "根据角色名模糊查询"),
            @ApiImplicitParam(paramType = "query", name = "likes", dataType = "String", required = false, value = "用户查询筛选条件"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", dataType = "Integer", required = false, value = "当前页数"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "Integer", required = false, value = "每页显示的条数")
    })

    public CommonResponse listUser(Role role) {
        List<User> list = roleService.listUser(role);
        return CommonResponse.success(list);
    }

    /**
     * 查询角色下的用户列表总长度
     */
    @RequestMapping(value = "/role/listUserCount", method = RequestMethod.GET)
    @ApiOperation(value = "查询角色下的用户列表", notes = "查询角色下的用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "Long", required = true, value = "根据角色名模糊查询"),
            @ApiImplicitParam(paramType = "query", name = "likes", dataType = "String", required = false, value = "用户查询筛选条件")

    })

    public CommonResponse listUserCount(Role role) {
        int size = roleService.listUserCount(role);
        return CommonResponse.success(size);
    }

    /**
     * 查询当前角色账号不存在的用户列表
     */
    @RequestMapping(value = "/role/listUserNotExistRoleid", method = RequestMethod.GET)
    @ApiOperation(value = "查询当前角色账号不存在的用户列表", notes = "查询当前角色账号不存在的用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "Long", required = true, value = "根据角色名模糊查询"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", dataType = "Integer", required = false, value = "当前页数"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "Integer", required = false, value = "每页显示的条数")
    })

    public CommonResponse listUserNotExistRoleid(Role role) {
        List<User> list = roleService.listUserNotExistRoleid(role);
        return CommonResponse.success(list);
    }

    /**
     * 查询当前角色账号不存在的用户列表总长度
     */
    @RequestMapping(value = "/role/listUserNotExistRoleidCount", method = RequestMethod.GET)
    @ApiOperation(value = "查询当前角色账号不存在的用户列表总长度", notes = "查询当前角色账号不存在的用户列表总长度")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "Long", required = true, value = "根据角色名模糊查询"),
            @ApiImplicitParam(paramType = "query", name = "likes", dataType = "String", required = false, value = "用户查询筛选条件")

    })

    public CommonResponse listUserNotExistRoleidCount(Role role) {
        int size = roleService.listUserNotExistRoleidCount(role);
        return CommonResponse.success(size);
    }

    /**
     * 角色下添加用户列表
     */
    @RequestMapping(value = "/role/addUser", method = RequestMethod.POST)
    @ApiOperation(value = "角色下添加用户列表", notes = "角色下添加用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "Long", required = true, value = "角色id"),
            @ApiImplicitParam(paramType = "query", name = "userIds", dataType = "Long[]", required = true, value = "存放多个用户id")
    })

    public CommonResponse addUser(Role role) {
        try {
            roleService.addUser(role);
            return CommonResponse.success();
        } catch (Exception e) {
            return CommonResponse.failure();
        }
    }

    /**
     * 删除角色下用户列表
     */
    @RequestMapping(value = "/role/deleteUser", method = RequestMethod.POST)
    @ApiOperation(value = "删除角色下用户列表", notes = "删除角色下用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "Long", required = true, value = "角色id"),
            @ApiImplicitParam(paramType = "query", name = "userIds", dataType = "Long[]", required = true, value = "存放多个用户id")
    })

    public CommonResponse deleteUser(Role role) {
        try {
            roleService.deleteUser(role);
            return CommonResponse.success();
        } catch (Exception e) {
            return CommonResponse.failure();
        }
    }


    @RequestMapping(value = "/role/funcPermissionTree", method = RequestMethod.POST)
    @ApiOperation(value = "添加角色时获取功能权限树", notes = "添加角色时获取功能权限树")
    public CommonResponse getFuncPermissionTree(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        params.put("status", Constant.INT_ONE);
        params.put("privilege", Constant.INT_ONE);
        List<FuncPermission> funcPermission = funcPermissionService.getFuncPermissionTree(params);
        if (CollectionUtils.isEmpty(funcPermission)) {
            return CommonResponse.success(new String[]{});
        }
        return CommonResponse.success(funcPermission);
    }

    @RequestMapping(value = "/role/dataPermissionTree", method = RequestMethod.POST)
    @ApiOperation(value = "添加角色时获取数据权限树", notes = "添加角色时获取数据权限树")
    public CommonResponse getDataPermissionTree(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        params.put("status", Constant.INT_ONE);
        List<DataPermission> dataPermissions = dataPermissionService.getDataPermissionTree(params);
        if (CollectionUtils.isEmpty(dataPermissions)) {
            return CommonResponse.success(new String[]{});
        }
        return CommonResponse.success(dataPermissions);
    }

    @RequestMapping(value = "/role/checkRoleIsExist", method = RequestMethod.POST)
    @ApiOperation(value = "添加角色时校验角色名是否已经存在,返回：1-->角色名已经存在，不能添加；0--> 可以添加", notes = "添加角色是校验角色名是否已经存在")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "roleName", dataType = "String", required = true, value = "角色名字"),
            @ApiImplicitParam(paramType = "query", name = "createUserId", dataType = "Long", required = true, value = "创建用户者id"),
            @ApiImplicitParam(paramType = "query", name = "createRoleId", dataType = "Long", required = true, value = "创建角色则id"),
    })
    public CommonResponse checkRoleIsExist(Role role) {
        int i = roleService.checkRoleIsExist(role);
        return CommonResponse.success(i);
    }

    /**
     * 添加用户时，根据不同的角色获数据权限和功能权限
     *
     * @param role
     * @return
     */
    @RequestMapping(value = "/role/permissionByRole", method = RequestMethod.POST)
    @ApiOperation(value = "添加角色时，改变角色获取对应数据权限和功能权限", notes = "添加角色时，改变角色获取对应数据权限和功能权限")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "roleId", dataType = "Long", required = true, value = "角色id"),
    })
    public CommonResponse getPermissionByRole(Role role) {
        Map<String, Object> resultMap = new HashMap<>(Constant.INT_FOUR);
        List<DataPermission> dataPermissionList = dataPermissionService.getDataPermissionByRole(role);
        List<FuncPermission> funcPermissionList = funcPermissionService.getFuncPermissionByRole(role);
        resultMap.put("dataPermissionList", dataPermissionList);
        resultMap.put("funcPermissionList", funcPermissionList);
        return CommonResponse.success(resultMap);
    }

    @RequestMapping(value = "/role/roleInfo", method = RequestMethod.POST)
    @ApiOperation(value = "查看角色的详细信息", notes = "根据角色id，查看角色详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "roleId", dataType = "Long", required = true, value = "根据角色id，查询角色信息")
    })
    public CommonResponse getRoleInfo(Role role) {
        Role paramRole = roleService.selectByKey(role.getRoleId());
        if (role.getRoleId() != null) {
            paramRole.setRoleId(role.getRoleId());
        }
        List<FuncPermission> funcPermissionList = funcPermissionService.getFuncPermissionByRole(paramRole);
        List<Long> funcList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(funcPermissionList)) {
            funcList = funcPermissionList.stream().map(FuncPermission::getId).collect(Collectors.toList());
        }
        List<DataPermission> dataPermissionList = dataPermissionService.getDataPermissionByRole(paramRole);
        List<Long> dataList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dataPermissionList)) {
            dataList = dataPermissionList.stream().map(DataPermission::getId).collect(Collectors.toList());
        }
        Map<String, Object> resultMap = new HashMap<>(Constant.INT_FOUR);
        resultMap.put("role", paramRole);
        resultMap.put("funcPermissionList", funcList);
        resultMap.put("dataPermissionList", dataList);
        return CommonResponse.success(resultMap);
    }

    @RequestMapping(value = "/role/getRolesById", method = RequestMethod.POST)
    @ApiOperation(value = "添加角色时，角色模板", notes = "根据角色id，查看角色详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "createUserId", dataType = "Long", required = true, value = "创建用户id"),
            @ApiImplicitParam(paramType = "query", name = "createRoleId", dataType = "Long", required = true, value = "创建角色id"),
            @ApiImplicitParam(paramType = "query", name = "roleName", dataType = "String", required = false, value = "角色名字")
    })
    public CommonResponse getRolesById(Role role) {
        //判断是否是超级管理员，超级管理员看所有用户创建的角色
        if (roleService.checkIsSuperRole(role.getCreateRoleId())) {
            role.setCreateUserId(null);
            role.setCreateRoleId(null);
        }
        List<Role> roleList = roleService.getRolesById(role);
        if (CollectionUtils.isEmpty(roleList)) {
            return CommonResponse.success(new String[]{});
        }
        return CommonResponse.success(roleList);
    }

    @RequestMapping(value = "/role/getRolesByUser", method = RequestMethod.POST)
    @ApiOperation(value = "根据用户所拥有的角色", notes = "根据用户名，获取角色详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", dataType = "String", required = false, value = "角色名字")
    })
    public CommonResponse getRoleByUser(User user) {
        //登录时调用，根据用户名查询角色
        List<Role> roleList = roleService.findRoleByUser(user);
        List<User> userList = userService.selectUserByParam(user);
        if (CollectionUtils.isEmpty(roleList) && CollectionUtils.isNotEmpty(userList)) {
            Role role = new Role();
            role.setId(Constant.NO_ROLE_ID);
            role.setRoleName(Constant.NO_ROLE_NAME);
            roleList.add(role);
            return CommonResponse.success(roleList);
        }
        return CommonResponse.success(roleList);
    }

    @RequestMapping(value = "/role/insertRoleAndFunction", method = RequestMethod.GET)
    public CommonResponse insertRoleAndFunction(Role role) {
        int i = roleService.insertRoleAndFunction(role);
        return CommonResponse.success(i == 1 ? "操作成功！" : "操作失败！");
    }


}
