package com.ziyun.auth.controller;

import com.ziyun.auth.model.OrgTree;
import com.ziyun.auth.service.IOrgTreeService;
import com.ziyun.common.response.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author leyangjie
 * @Description: 组织机构树
 * @date 2018/4/27 16:13
 */
@RestController
@RequestMapping("/v2/auth")
@Api(tags = "组织机构模块", description = "组织机构模块相关api")
public class OrgTreeController {

    @Autowired
    private IOrgTreeService orgTreeService;

    /**
     * 获取组织机构树
     *
     * @param orgTree
     * @return
     */
    @RequestMapping(value = "/orgSyss", method = RequestMethod.GET)
    @ApiOperation(value = "查询组织机构树", notes = "根据parentCode获取组织机构树")
    @ApiImplicitParam(paramType = "query", name = "parentCode", type = "String", required = true, value = "父组织机构树")
    public CommonResponse listOrgSyss(OrgTree orgTree) {
        List<Map<String, Object>> orgTreeList = orgTreeService.listOrgSys(orgTree);
        return CommonResponse.success(orgTreeList);
    }

    /**
     * 根据组织机构编号，删除组织机构
     */
    @RequestMapping(value = "/org/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除组织机构树", notes = "根据orgCode删除组织机构树")
    @ApiImplicitParam(paramType = "query", name = "id", type = "String", required = true, value = "父组织机构树")
    public CommonResponse delete(String id) {
        try {
            orgTreeService.deleteOrg(id);
            return CommonResponse.success();
        } catch (Exception e) {
            return CommonResponse.failure();
        }

    }

    /**
     * 获取组织机构树
     *
     * @param orgLevel
     * @return
     */
    @RequestMapping(value = "/orgParentOrgs", method = RequestMethod.GET)
    @ApiOperation(value = "查询父组织机构和父组织机构以上节点", notes = "根据orgLevel父组织机构和父组织机构以上节点  ")
    @ApiImplicitParam(paramType = "query", name = "orgLevel", type = "Integer", required = true, value = "父组织机构树")
    public CommonResponse getParentOrgs(Integer orgLevel) {
        List<Map<String, Object>> orgTreeList = orgTreeService.getParentOrgs(orgLevel);
        return CommonResponse.success(orgTreeList);
    }

    /**
     * 保存组织机构树
     */
    @RequestMapping(value = "/org/save", method = RequestMethod.POST)
    /*@ApiOperation(value = "保存组织机构树", notes = "保存组织机构树")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "orgCode", type = "String", required = true, value = "组织机构编号"),
            @ApiImplicitParam(paramType = "query", name = "orgName", type = "String", required = true, value = "组织机构名"),
            @ApiImplicitParam(paramType = "query", name = "parentCode", type = "String", required = true, value = "父组织机构编号"),
            @ApiImplicitParam(paramType = "query", name = "orgLevel", type = "String", required = true, value = "组织机构层级"),
            @ApiImplicitParam(paramType = "query", name = "remark", type = "String", required = true, value = "备注")
    })*/
    public CommonResponse save(OrgTree orgTree) {
        try {
            orgTreeService.save(orgTree);
            return CommonResponse.success();
        } catch (Exception e) {
            return CommonResponse.failure();
        }

    }

    /**
     * 修改组织机构
     */
    @RequestMapping(value = "/org/update", method = RequestMethod.POST)
    @ApiOperation(value = "保存组织机构树", notes = "保存组织机构树")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", type = "Long", required = true, value = "组织机构id"),
            @ApiImplicitParam(paramType = "query", name = "orgCode", type = "String", required = false, value = "组织机构编号"),
            @ApiImplicitParam(paramType = "query", name = "orgName", type = "String", required = false, value = "组织机构名"),
            @ApiImplicitParam(paramType = "query", name = "parentCode", type = "String", required = false, value = "父组织机构编号"),
            @ApiImplicitParam(paramType = "query", name = "orgLevel", type = "Long", required = true, value = "组织机构层级"),
            @ApiImplicitParam(paramType = "query", name = "remark", type = "String", required = false, value = "备注")
    })
    public CommonResponse update(OrgTree orgTree) {
        try {
            orgTreeService.updateNotNull(orgTree);
            return CommonResponse.success();
        } catch (Exception e) {
            return CommonResponse.failure();
        }

    }

}
