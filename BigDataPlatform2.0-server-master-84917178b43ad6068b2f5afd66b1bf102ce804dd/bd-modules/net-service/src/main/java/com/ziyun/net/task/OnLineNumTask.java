package com.ziyun.net.task;

import com.ziyun.net.server.MonitorServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("onLineNumTask")
public class OnLineNumTask {

    @Autowired
    private MonitorServer monitorServer;

    //@Scheduled(cron = "30 */1 * * * ? ")    //每分钟执行一次
    public void taskOnLineNum() throws Exception {
        monitorServer.insertOnLineNum();
        //System.out.println("假装在处理定时任务:" + new Date());
    }
}
