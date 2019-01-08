package com.ziyun.academic.task;

import com.ziyun.academic.server.IFireWallFlowInfoServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("fwFlowTask")
public class FireWallFlowTask {

    @Autowired
    private IFireWallFlowInfoServer fwServer;

    public void grabFwFlowInfo() throws Exception {
        fwServer.grabFwflow();
    }
}
