package com.ziyun.basic.controller;

import com.ziyun.basic.entity.OwnSchoolOrg;
import com.ziyun.basic.server.OwnSchoolOrgServer;
import com.ziyun.basic.vo.Params;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/v2/school")
public class OwnSchoolOrgController {

    @Autowired
    public OwnSchoolOrgServer ownSchoolOrgServer;

    /**
     * 根据参数获取所有班级信息     -- 暂不确定是否是有调用
     *
     * @param para
     * @return
     */
    @ResponseBody
    @RequestMapping("/common/selectBy")
    @ApiOperation(value = "微服务提供接口", notes = "根据参数获取所有班级信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", value = " 1-->贫困生"),
            @ApiImplicitParam(name = "bdate", paramType = "query", dataType = "String", value = " 开始时间"),
            @ApiImplicitParam(name = "edate", paramType = "query", dataType = "String", value = " 结束时间"),
            @ApiImplicitParam(name = "name", paramType = "query", dataType = "String", value = " 学生姓名"),
    })
    public List<OwnSchoolOrg> selectBy(@RequestBody Params para) {
        return ownSchoolOrgServer.selectBy(para);
    }


    @ResponseBody
    @RequestMapping("/common/selectOwnClasscode")
    @ApiOperation(value = "微服务提供接口", notes = "查询所有的班级编号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schoolCode", paramType = "query", dataType = "String", value = "校区编号：如slg"),
            @ApiImplicitParam(name = "facultyCode", paramType = "query", dataType = "String", value = "院系编号"),
            @ApiImplicitParam(name = "majorCode", paramType = "query", dataType = "String", value = "专业编号"),
            @ApiImplicitParam(name = "classSelect", paramType = "query", dataType = "String[]", value = "班级编号"),
            @ApiImplicitParam(name = "yearArr", paramType = "query", dataType = "String[]", value = "入学年份"),
            @ApiImplicitParam(name = "education", paramType = "query", dataType = "Integer", value = "1-->本科"),
            @ApiImplicitParam(name = "eduStatus", paramType = "query", dataType = "String", value = "1-0 --> 正常生  1-3 --> 留级生 1-4 --> 休学生  2-5 --> 毕业生 2-6 --> 肄业 2-7 --> 其他"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "String", value = "0-> 男 1-->女"),
            @ApiImplicitParam(name = "politicalCode", paramType = "query", dataType = "Integer", value = " 1、团员，2、预备党员，3、党员"),
            @ApiImplicitParam(name = "impoverish", paramType = "query", dataType = "String", value = " 1-->贫困生"),
            @ApiImplicitParam(name = "bdate", paramType = "query", dataType = "String", value = " 开始时间"),
            @ApiImplicitParam(name = "edate", paramType = "query", dataType = "String", value = " 结束时间"),
            @ApiImplicitParam(name = "name", paramType = "query", dataType = "String", value = " 学生姓名"),
    })
    public Set<String> selectOwnClasscode(Params para) {
        return ownSchoolOrgServer.selectOwnClasscode(para);
    }


}
