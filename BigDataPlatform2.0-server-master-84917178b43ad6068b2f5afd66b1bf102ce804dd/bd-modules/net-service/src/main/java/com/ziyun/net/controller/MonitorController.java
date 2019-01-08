package com.ziyun.net.controller;

import com.ziyun.net.entity.FireWallFlowMonitor;
import com.ziyun.net.server.MonitorServer;
import com.ziyun.net.vo.Params;
import com.ziyun.net.vo.ParamsStatus;
import com.ziyun.utils.requests.CommResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/v2/monitor")
public class MonitorController {

    @Autowired
    public MonitorServer monitorServer;

    @ApiOperation(value = "监控-在线人数", notes = "在线人数", httpMethod = "POST")
    @RequestMapping("/onlineNum")
    @ResponseBody
    public CommResponse onlineNum() {
        Map<String, Object> result = new HashMap<>();
        //查询在线人数
        int numObj = monitorServer.getOnLineNum();
        if (numObj == 0)
            return CommResponse.success(null);
        result.put("numObj", numObj);
        return CommResponse.success(result);
    }

    @ApiOperation(value = "监控-查询历史在线人数", notes = "查询历史在线人数", httpMethod = "POST")
    @RequestMapping("/onlinelist")
    @ResponseBody
    public CommResponse getOnlineNumList() {
        //查询历史在线人数
        return CommResponse.success(monitorServer.getOnLineNumHis());
    }

    @ApiOperation(value = "监控-速率统计", notes = "速率统计", httpMethod = "POST")
    @RequestMapping("/rate")
    @ResponseBody
    public CommResponse getRate() {
        //timeData
        Set<String> timeDate = new HashSet<>();
        //cmcc
        List<String> cmcc = new ArrayList<>();
        //chinaNet
        List<String> chinaNet = new ArrayList<>();
        List<FireWallFlowMonitor> result = monitorServer.getRate();
        if (null == result || result.size() == 0)
            return CommResponse.success(null);
        for (FireWallFlowMonitor fireWallFlow : result) {
            timeDate.add(fireWallFlow.getTime());
            String type = fireWallFlow.getType();
            if (type.equals("CHINANET")) {
                chinaNet.add(fireWallFlow.getAllflow());
            }else if (type.equals("CMCC")) {
                cmcc.add(fireWallFlow.getAllflow());
            }
        }
        Map resultMap = new HashMap();
        resultMap.put("chinaNet", chinaNet);
        resultMap.put("cmcc", cmcc);
        List<String> sortDate = new ArrayList<>(timeDate);
        Collections.sort(sortDate);
        resultMap.put("timeDate", sortDate);
        return CommResponse.success(resultMap);
    }

    @ApiOperation(value = "监控-今日热点访问信息", notes = "今日热点访问信息", httpMethod = "POST")
    @RequestMapping("/topHotSpot")
    @ResponseBody
    public CommResponse getTop(Params para) {
        return CommResponse.success(monitorServer.getTopHotSpot(para));
    }


    /**
     * 在线人员信息
     *
     * @return List<LinkedHashMap<String,Object>>
     * @throws IOException
     */
    @ApiOperation(value = "监控-在线人员信息", notes = "在线人员信息", httpMethod = "POST")
    @RequestMapping("/olPersion")
    @ResponseBody
    public List<LinkedHashMap<String, Object>> getolPersion() {
        return monitorServer.getOnLinePersion();
    }

    /**
     * 地理位置信息
     * 苏州理工学院 经纬度 120.57765,31.897941
     *
     * @return List<LinkedHashMap<String,Object>>
     * @throws IOException
     */
    @ApiOperation(value = "监控-地理位置信息", notes = "地理位置信息", httpMethod = "POST")
    @RequestMapping("/geo")
    @ResponseBody
    public Map<String, Object> getGeo() {
        return monitorServer.getIpgeo();
    }

    /**
     * 大地图
     * @return
     */
    @ApiOperation(value = "监控-大地图", notes = "大地图", httpMethod = "POST")
    @RequestMapping("/worldip")
    @ResponseBody
    public CommResponse getNewIpgeo() {
        return CommResponse.success(monitorServer.getWorldIp());
    }

    /**
     * 今日登入人数
     * @param param
     * @return
     */
    @ApiOperation(value = "监控-今日登入人数", notes = "今日登入人数", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "timeStamp", dataType = "String", value = "时间戳"),
            @ApiImplicitParam(paramType = "query", name = "start", dataType = "Integer", value = "分页下标（0, 10, 20...）"),
            @ApiImplicitParam(paramType = "query", name = "limit", dataType = "Integer", value = "分页参数：每页多少条数据")
    })
    @RequestMapping("/olstudent")
    @ResponseBody
    public CommResponse getOnlineStudent(ParamsStatus param) {
        if (param.getStart() == null) {
            param.setStart(0);
            param.setLimit(5);
        }
        return CommResponse.success(monitorServer.getOnlineStudent(param));
    }

}
