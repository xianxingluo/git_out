package com.ziyun.basic.controller;

import com.ziyun.basic.entity.OwnOrgStree;
import com.ziyun.basic.server.IOwnOrgTreeService;
import com.ziyun.basic.vo.Params;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 组织机构树Controller
 */
@Controller
@RequestMapping("/v2/ownOrgTree")
@Api(tags = "组织机构树", description = "组织机构树相关api")
public class OwnOrgTreeController {

    @Autowired
    private IOwnOrgTreeService ownOrgTreeService;

    @RequestMapping("/common/getPcodeByCcode")
    @ResponseBody
    @ApiOperation(value = "微服务提供接口", notes = "根据组织机构编号，查询对应的组织机构信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public OwnOrgStree getPcodeByCcode(@RequestBody Params params) {
        return ownOrgTreeService.getPcodeByCcode(params);
    }

    @RequestMapping("/common/getChildrenList")
    @ResponseBody
    @ApiOperation(value = "微服务提供接口", notes = "根据父级机构 code，查询子组织机构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public List<Map> getChildrenList(@RequestBody Params params) {
        return ownOrgTreeService.getChildrenList(params);
    }

    @RequestMapping("/common/getAllTopCategory")
    @ResponseBody
    @ApiOperation(value = "微服务提供接口", notes = "根据父级机构 code，查询子组织机构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", required = false, value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", required = false, value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", required = false, value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", required = false, value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", required = false, value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", required = false, value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", required = false, value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", required = false, value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", required = false, value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", required = false, value = " 1-->贫困生"),
            @ApiImplicitParam(name = "semesterArr", paramType = "query", dataType = "String[]", required = false, value = " 学业特征时间"),
    })
    public List<Map> getAllTopCategory(@RequestBody Params params) {
        return ownOrgTreeService.getAllTopCategory(params);
    }
}
