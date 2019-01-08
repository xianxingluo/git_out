package com.ziyun.basic.controller;

import com.ziyun.basic.server.UserOptionServer;
import com.ziyun.utils.requests.CommResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/v2/option")
public class UserOptionController {
    @Autowired
    private UserOptionServer userOptionServer;

    /**
     * @api {POST} /api/v2/option/addition
     * @apiName addition
     * @apiGroup option
     * @apiVersion 2.0.0
     * @apiDescription 用户自定义标签的增加和修改操作
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/option/addition
     * @apiParam {String} [basic] basic：必须填写
     * @apiParam {String} [label] 存储的选择器内容，存储在数据库
     * @apiParam {int} [id] id如果传则代表是修改，不传是新增
     * @apiParamExample {json} 请求例子:
     * {
     * "basic": sdaczx
     * "label":{"name":sss}
     * }
     * @apiSuccess (200) {String} message 提示信息code 根据code去查找错误提示信息
     * @apiSuccess (200) {int} statusCode 200 操作成功,400 操作失败
     * @apiSuccessExample {json} 返回样例:
     * {
     * "statusCode": 200,
     * "message": "操作成功"
     * }
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": 提示信息code 根据code去查找错误提示信息
     * }
     */
    @RequestMapping("/addition")
    @ResponseBody
    public CommResponse addOrsaveUserLabel(@RequestParam Map<String, Object> map) {
        if (map != null && map.keySet().size() >= 5) {
            userOptionServer.saveUserLabel(map);
            return CommResponse.success();
        } else {
            return CommResponse.failure();
        }
    }

    /**
     * @api {POST} /api/v2/option/reference/addition
     * @apiName referenceaddition
     * @apiGroup option
     * @apiVersion 2.0.0
     * @apiDescription 用户自定义标签的增加和修改操作
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/option/reference/addition
     * @apiParam {String} [basic] basic：必须填写
     * @apiParam {String} [label] 存储的选择器内容，存储在数据库
     * @apiParam {int} [id] id如果传则代表是修改，不传是新增
     * @apiParamExample {json} 请求例子:
     * {
     * "basic": sdaczx
     * "label":{"name":sss}
     * }
     * @apiSuccess (200) {String} message 提示信息code 根据code去查找错误提示信息
     * @apiSuccess (200) {int} statusCode 200 操作成功,400 操作失败
     * @apiSuccessExample {json} 返回样例:
     * {
     * "statusCode": 200,
     * "message": "操作成功"
     * }
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": 提示信息code 根据code去查找错误提示信息
     * }
     */
    @RequestMapping("/reference/addition")
    @ResponseBody
    public CommResponse addOrsaveUserReferenceLabel(@RequestParam Map<String, Object> map) {
        if (map != null && map.keySet().size() >= 5) {
            map.put("queryType", "reference");
            userOptionServer.saveUserLabel(map);
            return CommResponse.success();
        } else {
            return CommResponse.failure();
        }
    }

    @ApiOperation(value = "查询存储的搜索器", notes = "用户根据自己的base码来查询存储的搜索器", httpMethod = "POST")
    @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", required = true, value = "token：必须填写")
    @RequestMapping("/label")
    @ResponseBody
    public CommResponse getUserLabel(String token) {
        return CommResponse.success(userOptionServer.getUserLabel(token));
    }

    /**
     * @api {POST} /api/v2/option/reference/label
     * @apiName referencelabel
     * @apiGroup option
     * @apiVersion 2.0.0
     * @apiDescription 用户根据自己的base码来查询存储的搜索器
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/option/reference/label
     * @apiParam {String} [token] token：必须填写
     * @apiParamExample {json} 请求例子:
     * {
     * "basic": sdaczx
     * }
     * @apiSuccess (200) {String} message 提示信息code 根据code去查找错误提示信息
     * @apiSuccess (200) {int} statusCode 200 操作成功,400 操作失败
     * @apiSuccessExample {json} 返回样例:
     * {
     * "statusCode": 200,
     * "message": "操作成功"
     * "data":[{alias=测试, id=1, label={class:111,name:111}}]
     * alias 搜索器的名称 id 编码 label 存储的内容
     * }
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": 提示信息code 根据code去查找错误提示信息
     * }
     */
    @RequestMapping("/reference/label")
    @ResponseBody
    public CommResponse getUserReferenceLabel(String token) {
        return CommResponse.success(userOptionServer.getUserReferenceLabel(token));
    }

    /**
     * @api {POST} /api/v2/option/cancel
     * @apiName cancel
     * @apiGroup option
     * @apiVersion 2.0.0
     * @apiDescription 删除用户定义的搜索器不支持批量删除
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/option/cancel
     * @apiParam {String} [id] id：要删除的id
     * @apiParamExample {json} 请求例子:
     * {
     * "id": 1
     * }
     * @apiSuccess (200) {String} message 提示信息code 根据code去查找错误提示信息
     * @apiSuccess (200) {int} statusCode 200 操作成功,400 操作失败
     * @apiSuccessExample {json} 返回样例:
     * {
     * "statusCode": 200,
     * "message": "操作成功"
     * }
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": 提示信息code 根据code去查找错误提示信息
     * }
     */
    @RequestMapping("/cancel")
    @ResponseBody
    public CommResponse deleteUserLabel(@RequestParam Map<String, Object> map) {
        if (map.get("id") != null && map.get("token") != null) {
            userOptionServer.deleteUserLabel(map);
            return CommResponse.success();
        } else {
            return CommResponse.failure();
        }
    }

    /**
     * @api {POST} /api/v2/option/reference/cancel
     * @apiName referencecancel
     * @apiGroup option
     * @apiVersion 2.0.0
     * @apiDescription 删除用户定义的搜索器不支持批量删除
     * @apiPermission 登录用户
     * @apiSampleRequest http://10.130.254.15:28808/#/api/v2/option/reference/cancel
     * @apiParam {String} [id] id：要删除的id
     * @apiParamExample {json} 请求例子:
     * {
     * "id": 1
     * }
     * @apiSuccess (200) {String} message 提示信息code 根据code去查找错误提示信息
     * @apiSuccess (200) {int} statusCode 200 操作成功,400 操作失败
     * @apiSuccessExample {json} 返回样例:
     * {
     * "statusCode": 200,
     * "message": "操作成功"
     * }
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 400
     * "message": 提示信息code 根据code去查找错误提示信息
     * }
     */
    @RequestMapping("/reference/cancel")
    @ResponseBody
    public CommResponse deleteUserReferenceLabel(@RequestParam Map<String, Object> map) {
        if (map.get("id") != null && map.get("token") != null) {
            map.put("queryType", "reference");
            userOptionServer.deleteUserLabel(map);
            return CommResponse.success();
        } else {
            return CommResponse.failure();
        }
    }

    @RequestMapping("/backlabel")
    @ResponseBody
    public Map<String, Object> backLabel(@RequestParam Map<String, Object> map) {
        return map;
    }

    @RequestMapping("/getlabelByid")
    @ResponseBody
    public Map<String, Object> getLabelByid(Long id) {
        return userOptionServer.getLabelById(id);
    }
}
