package com.ziyun.basic.controller;

import com.ziyun.basic.entity.EduStatus;
import com.ziyun.basic.server.EduStatusServer;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/v2/eduStatus")
public class EduStatusController {

    @Autowired
    public EduStatusServer eduStatusServer;

    /**
     * 根据学号获取学籍信息
     *
     * @param outid
     * @return
     */
    @ResponseBody
    @RequestMapping("/common/selectByOutid")
    @ApiOperation(value = "微服务提供接口", notes = "根据学号查询学籍信息")
    @ApiImplicitParam(name = "outid", paramType = "query", dataType = "String", required = true, value = "学号")
    public EduStatus selectByOutid(@RequestBody String outid) {
        return eduStatusServer.selectByOutid(outid);
    }

}