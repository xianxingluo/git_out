package com.ziyun.basic.server;


import com.ziyun.basic.entity.EduStatus;

public interface EduStatusServer {

    /**
     * 根据学号查询学籍信息
     *
     * @param outid
     * @return
     */
    EduStatus selectByOutid(String outid);

}
