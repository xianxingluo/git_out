package com.ziyun.basic.server.impl;


import com.ziyun.basic.mapper.EduStatusMapper;
import com.ziyun.basic.entity.EduStatus;
import com.ziyun.basic.server.EduStatusServer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EduStatusServerImpl implements EduStatusServer {

    private static Logger logger = Logger
            .getLogger(EduStatusServerImpl.class);

    @Autowired
    public EduStatusMapper eduStatusMapper;


    /**
     * 根据学号查询学籍信息
     *
     * @param outid
     * @return
     */
    @Override
    public EduStatus selectByOutid(String outid) {
        return eduStatusMapper.selectByOutid(outid);
    }
}
