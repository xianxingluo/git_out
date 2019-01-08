package com.ziyun.auth.controller;

import com.ziyun.auth.constant.Constant;
import com.ziyun.auth.service.IRoleService;
import com.ziyun.auth.service.IUserService;
import com.ziyun.common.entity.DataPermission;
import com.ziyun.common.entity.Role;
import com.ziyun.common.entity.User;
import com.ziyun.common.enums.StatusCodeEnum;
import com.ziyun.common.response.CommonResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author leyangjie
 * @Description: 用户controller
 * @date 2018/4/26 15:19
 */
@Api(tags = "用户模块", description = "用户模块相关api")
@RestController
@RequestMapping("/v2/auth")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    /**
     * 添加用户
     */
    @RequestMapping(value = "/user/save", method = RequestMethod.POST)
    @ApiOperation(value = "添加用户", notes = "添加用户", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "nickname", dataType = "String", required = true, value = "用户名字"),
            @ApiImplicitParam(paramType = "query", name = "username", dataType = "String", required = true, value = "用户名"),
            @ApiImplicitParam(paramType = "query", name = "password", dataType = "String", required = true, value = "密码"),
            @ApiImplicitParam(paramType = "query", name = "email", dataType = "String", required = true, value = "邮箱"),
            @ApiImplicitParam(paramType = "query", name = "mobile", dataType = "String", required = true, value = "手机号"),
            @ApiImplicitParam(paramType = "query", name = "createUserId", dataType = "Long", required = true, value = "创建者ID"),
            @ApiImplicitParam(paramType = "query", name = "createRoleId", dataType = "Long", required = true, value = "创建角色ID"),
            @ApiImplicitParam(paramType = "query", name = "roleIds", dataType = "Long[]", required = true, value = "可选多个角色id"),
    })
    public CommonResponse addUser(User user) {
        //创建返回对象
        CommonResponse commonResponse = new CommonResponse();
        try {
            //添加用户前，先校验用户名
            if (StringUtils.isBlank(user.getUsername())) {
                commonResponse.setMessage("用户名为空!");
                commonResponse.setStatusCode(200);
                return commonResponse;
            }
            //去掉两端空格
            user.setUsername(user.getUsername().trim());
            //使用正则判断用户名是否合法
            String regex = "[A-Za-z0-9_]{4,16}";
            if (!user.getUsername().matches(regex)) {
                commonResponse.setMessage("用户名不合法!");
                commonResponse.setStatusCode(200);
                return commonResponse;
            }
            // 判断用户名是否已经存在
            synchronized (Object.class) {
                int i = userService.queryUserByusername(user);
                if (1 == i) {
                    commonResponse.setMessage("用户名已经存在!");
                    commonResponse.setStatusCode(200);
                    return commonResponse;
                }
            }
            int size = userService.saveUser(user);
            return CommonResponse.success(size);
        } catch (Exception e) {
            return CommonResponse.failure("0");
        }
    }

    /**
     * 删除用户
     */
    @RequestMapping(value = "/user/remove", method = RequestMethod.POST)
    @ApiOperation(value = "删除用户,返回1，表示删除成功", notes = "使用ids来接收userId来删除用户，支持批量删除")
    @ApiImplicitParam(paramType = "query", name = "ids", dataType = "Long[]", required = true, value = "根据用户id来删除用户")
    public CommonResponse removeUser(User user) {
        try {
            //判断ids是否为空
            if (ArrayUtils.isEmpty(user.getIds())) {
                return CommonResponse.failure(StatusCodeEnum.INVALID_PARAM_ERROR);
            }
            userService.deleteUser(user);
            return CommonResponse.success("1");
        } catch (Exception e) {
            return CommonResponse.failure("0");
        }
    }

    /**
     * 查询用户
     */
    @ApiOperation(value = "查询用户列表", notes = "支持 登录用户名，用户名，手机号 模糊查询")
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "nickname", dataType = "String", value = "用户名"),
            @ApiImplicitParam(paramType = "query", name = "username", dataType = "String", value = "姓名"),
            @ApiImplicitParam(paramType = "query", name = "mobile", dataType = "String", value = "手机号"),
            @ApiImplicitParam(paramType = "query", name = "start", dataType = "Integer", value = "当前页数", required = true),
            @ApiImplicitParam(paramType = "query", name = "limit", dataType = "Integer", value = "每页显示的条数", required = true),
            @ApiImplicitParam(paramType = "query", name = "createUserId", dataType = "Integer", value = "手机号", required = true),
            @ApiImplicitParam(paramType = "query", name = "createRoleId", dataType = "Integer", value = "手机号", required = true)
    })
    public CommonResponse listUsers(User user) {
        //TODO 目前先这样做 isSuperAdmin属性为1就是超级管理员，可以查看所有用户
        if (roleService.checkIsSuperRole(user.getCreateRoleId())) {
            //如果为超级管理员，则将createUserId和createRoleId设置为null
            user.setCreateRoleId(null);
            user.setCreateUserId(null);
        }

        List<User> userList = userService.listUsers(user);
        return CommonResponse.success(userList);
    }

    /**
     * 查询用户size
     */
    @ApiOperation(value = "查询用户列表size", notes = "支持 登录用户名，用户名，手机号 模糊查询")
    @RequestMapping(value = "/user/size", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", dataType = "String", value = "用户名"),
            @ApiImplicitParam(paramType = "query", name = "nickname", dataType = "String", value = "昵称"),
            @ApiImplicitParam(paramType = "query", name = "mobile", dataType = "String", value = "手机号"),
            @ApiImplicitParam(paramType = "query", name = "createUserId", dataType = "Integer", value = "手机号", required = true),
            @ApiImplicitParam(paramType = "query", name = "createRoleId", dataType = "Integer", value = "手机号", required = true)
    })
    public CommonResponse listUserSize(User user) {
        //TODO 目前先这样做 isSuperAdmin属性为1就是超级管理员，可以查看所有用户
        if (roleService.checkIsSuperRole(user.getCreateRoleId())) {
            //如果为超级管理员，则将createUserId和createRoleId设置为null
            user.setCreateRoleId(null);
            user.setCreateUserId(null);
        }
        int size = userService.listUserSize(user);
        return CommonResponse.success(size);
    }


    /**
     * 修改用户
     */
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    @ApiOperation(value = "修改用户", notes = "根据用户id，修改用户，角色，数据权限")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", dataType = "Long", required = true, value = "用户id"),
            @ApiImplicitParam(paramType = "query", name = "nickname", dataType = "String", value = "用户名字"),
            @ApiImplicitParam(paramType = "query", name = "password", dataType = "String", value = "密码"),
            @ApiImplicitParam(paramType = "query", name = "email", dataType = "String", value = "邮箱"),
            @ApiImplicitParam(paramType = "query", name = "mobile", dataType = "String", value = "手机号"),
            @ApiImplicitParam(paramType = "query", name = "roleIds", dataType = "Long[]", value = "可选多个角色id"),
    })
    public CommonResponse updateUser(User user) {
        //创建返回对象
        CommonResponse commonResponse = new CommonResponse();
        try {
            // 判断用户名是否已经存在
            synchronized (Object.class) {
                int i = userService.queryUserByusername(user);
                if (i > 1) {
                    commonResponse.setMessage("用户名已经存在!");
                    commonResponse.setStatusCode(200);
                    return commonResponse;
                }
            }
            userService.updateUser(user);
            return CommonResponse.success("修改成功！");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return CommonResponse.failure("修改失败！");
        }
    }

    /**
     * 启用或禁用 用户
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/user/lock", method = RequestMethod.POST)
    @ApiOperation(value = "启用、禁用用户", notes = "根据用户id，启用或警用用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", dataType = "Long", required = true, value = "用户id"),
            @ApiImplicitParam(paramType = "query", name = "status", dataType = "Long", required = true, value = "用户状态：0 -- 禁用，1 --启用"),
    })
    public CommonResponse userLock(User user) {
        int i = userService.updateUserStatus(user);
        if (i > 0) {
            return CommonResponse.success("操作成功!");
        } else {
            return CommonResponse.success("操作失败!");
        }
    }


    /**
     * excel导入用户
     */
    @ApiOperation(value = "excel导入用户", notes = "excel导入用户", httpMethod = "POST")
    @RequestMapping(value = "/user/import", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", name = "nickname", dataType = "file", value = "文件名"),
            @ApiImplicitParam(paramType = "query", name = "createUserId", dataType = "String", value = "创建者ID")
    })
    public CommonResponse importUserByExcel(MultipartFile file, String createUserId) {
        try {
            String result = userService.importUserByExcel(file, createUserId);
            return StringUtils.isBlank(result) ? CommonResponse.success("导入成功！") : CommonResponse.failure(result);
        } catch (Exception e) {
            return CommonResponse.failure("导入失败！");
        }
    }

    /**
     * 校验用户名是否存在,返回结果为1，用户名已经存在
     */
    @ApiOperation(value = "校验用户是否存在", notes = "校验用户是否存在: 返回1 用户名已经存在，0 --》可以添加")
    @RequestMapping(value = "/user/usernameIsExist", method = RequestMethod.GET)
    @ApiImplicitParams({

            @ApiImplicitParam(paramType = "query", name = "username", dataType = "String", value = "用户名")
    })
    public CommonResponse usernameIsExist(User user) {
        //前段此处默认会将createUserId和createRoleId传给后台，将两个参数设置null，采用保证用户名唯一
        if (user.getCreateUserId() != null || user.getCreateRoleId() != null) {
            user.setCreateRoleId(null);
            user.setCreateUserId(null);
        }
        int size = userService.queryUserByusername(user);
        return CommonResponse.success(size);
    }

    /**
     * 返回数据权限树
     */
    @ApiOperation(value = "返回数据权限树", notes = "返回数据权限树")
    @RequestMapping(value = "/user/listDataPerfuncs", method = RequestMethod.GET)
    public CommonResponse listDataPerfuncs() {
        List<DataPermission> dataPermissionList = userService.listDataPerfuncs();
        return CommonResponse.success(dataPermissionList);
    }


    /**
     * 查询用户（单个）
     */
    @ApiOperation(value = "查询用户", notes = "根据用户名查询用户")
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", dataType = "String", value = "用户名")
    })
    public CommonResponse getUser(String username) {
        User queryUser = userService.getUser(username);
        return CommonResponse.success(queryUser);
    }

    /**
     * 获取当前登录用户名
     *
     * @param token
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取当前登录用户名", notes = "获取当前登录用户名", httpMethod = "POST")
    @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", value = "token")
    @RequestMapping(value = "/getUsername")
    @ResponseBody
    public String getUsername(String token) throws Exception {
        return userService.getUsername(token);
    }

    /**
     * 获取用户下的角色
     *
     * @return
     */
    @ApiOperation(value = "获取用户下的角色", notes = "获取用户下的角色", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", value = "token"),
            @ApiImplicitParam(paramType = "query", name = "createUserId", dataType = "String", value = "创建用户id"),
            @ApiImplicitParam(paramType = "query", name = "createRoleId", dataType = "String", value = "创建角色id")
    })
    @RequestMapping(value = "/getRoleByUser")
    @ResponseBody
    public CommonResponse getRoleByUser(User user) {
        if (roleService.checkIsSuperRole(user.getCreateRoleId())) {
            user.setCreateUserId(null);
            user.setCreateRoleId(null);
        }
        List<Role> roleList = roleService.getRoleByUser(user);
        Role role = new Role();
        role.setId(Constant.NO_ROLE_ID);
        role.setRoleName(Constant.NO_ROLE_NAME);
        roleList.add(0, role);
        if (CollectionUtils.isEmpty(roleList)) {
            return CommonResponse.success(new String[]{});
        }
        return CommonResponse.success(roleList);
    }

    /***  编辑用户的角色时：分为三步,这是第一步：
     * 修改用户的角色时，例如去掉一个角色，需要判断是否使用去掉的角色创建过用户和角色
     * @return
     */
    @ApiOperation(value = "修改用户的角色时，检查是否通过该角色创建用户或者角色，返回1：表示该角色创建过用户或者角色", notes = "修改用户的角色时，检查是否通过该角色创建用户或者角色", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", value = "token"),
            @ApiImplicitParam(paramType = "query", name = "userId", dataType = "String", value = "用户id"),
            @ApiImplicitParam(paramType = "query", name = "roleIds", dataType = "String", allowMultiple = true, value = "去掉的角色id")
    })
    @RequestMapping(value = "/checkRole")
    @ResponseBody
    public CommonResponse checkRole(User user) {
        try {

            if (ArrayUtils.isNotEmpty(user.getRoleIds())) {
                for (Long roleId : user.getRoleIds()) {
                    User paramUser = new User();
                    paramUser.setCreateUserId(user.getUserId());
                    paramUser.setCreateRoleId(roleId);
                    //判断：修改用户和当前角色下是否创建过用户和角色
                    List<User> userList = userService.selectUserByParam(paramUser);
                    if (CollectionUtils.isNotEmpty(userList)) {
                        return CommonResponse.success(1);
                    }
                    Role paramRole = new Role();
                    paramRole.setCreateUserId(user.getUserId());
                    paramRole.setCreateRoleId(roleId);
                    List<Role> roleList = roleService.selectByParam(paramRole);
                    if (CollectionUtils.isNotEmpty(roleList)) {
                        return CommonResponse.success(1);
                    }
                }
                //返回0，表示没有通过该角色创建用户和角色
                return CommonResponse.success(0);
            } else {
                return CommonResponse.success("角色id为空!");
            }
        } catch (Exception e) {
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            commonResponse.setMessage("查询失败!");
            return commonResponse;
        }

    }

    /**
     * 修改用户的第二步：
     * 删除用户下的角色
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "修改用户的角色时，先删除用户下的角色", notes = "修改用户的角色时，先删除用户下的角色", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", value = "token"),
            @ApiImplicitParam(paramType = "query", name = "userId", dataType = "Long", value = "用户id"),
            @ApiImplicitParam(paramType = "query", name = "roleIds", dataType = "Long", allowMultiple = true, value = "去掉的角色id")
    })
    @RequestMapping(value = "/deleteUserUnderRole")
    @ResponseBody
    public CommonResponse deleteUserUnderRole(User user) {
        int i = userService.deleteUserUnderRole(user);
        if (i == 1) {
            return CommonResponse.success(i);
        } else {
            return CommonResponse.success();
        }
    }
}

