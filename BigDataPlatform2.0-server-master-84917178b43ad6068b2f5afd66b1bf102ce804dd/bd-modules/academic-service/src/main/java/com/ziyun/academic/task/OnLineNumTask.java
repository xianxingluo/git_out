package com.ziyun.academic.task;

import com.ziyun.academic.server.MonitorServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("onLineNumTask")
public class OnLineNumTask {

    @Autowired
    private MonitorServer monitorServer;

    public void taskOnLineNum() throws Exception {
        //
        monitorServer.insertOnLineNum();
        //System.out.println(new Date());
//		rechargeServer.taskRecharge();
    }
}
