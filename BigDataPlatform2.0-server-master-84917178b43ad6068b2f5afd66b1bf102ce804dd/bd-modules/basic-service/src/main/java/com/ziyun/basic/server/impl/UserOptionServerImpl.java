package com.ziyun.basic.server.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.ziyun.basic.mapper.UserOptionMapper;
import com.ziyun.basic.server.UserOptionServer;
import com.ziyun.common.model.auth.User;
import com.ziyun.utils.cache.TokenCasheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserOptionServerImpl implements UserOptionServer {
    @Autowired
    private UserOptionMapper userOptionMapper;

    @Override
    public int saveUserLabel(Map<String, Object> map) {
        map = convertTokentoUsername(map);
        //判断是否是新增。如果存在id则是修改。修改采用逻辑是删除原有id，新增一条记录
        if (map.get("id") != null) {
            //删除当前这个id的数据
            userOptionMapper.deleteUserLabel(map);
            return userOptionMapper.insertUserLabel(map);
        } else {
            return userOptionMapper.insertUserLabel(map);
        }
    }

    @Override
    public int deleteUserLabel(Map<String, Object> map) {
        map = convertTokentoUsername(map);
        return userOptionMapper.deleteUserLabel(map);
    }

    @Override
    public List<Map<String, Object>> getUserLabel(String token) {
        if (com.ziyun.utils.common.StringUtils.isBlank(token))
            return null;
        User user = (User) TokenCasheManager.getUserCache(token);
        // 生成session对象,获取当前用户的username
        return userOptionMapper.getUserLabel(user.getUsername());
    }

    @Override
    public List<Map<String, Object>> getUserReferenceLabel(String base) {
        if (com.ziyun.utils.common.StringUtils.isBlank(base))
            return null;
        User user = (User) TokenCasheManager.getUserCache(base);
        // 生成session对象,获取当前用户的username
        return userOptionMapper.getUserLabel(user.getUsername() + " reference");
    }

    private Map<String, String> convertMapValue(Map<String, Object> map) {
        //用来存放结果的map
        Map<String, String> resultMap = null;

        if (map != null) {
            resultMap = new HashMap<>();
            for (Map.Entry<String, Object> entry : map.entrySet()){
                String key = entry.getKey();
                //判断是否是label标签是的话进行json转换,其他的直接转换成string存入
                if (StringUtils.equals("label", key)
                        || StringUtils.equals("locator", key)
                        || StringUtils.equals("params", key)) {

                    resultMap.put(key, JSON.toJSONString(entry.getValue()));
                } else {
                    resultMap.put(key, String.valueOf(entry.getValue()));
                }
            }
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> getLabelById(Long id) {
        return userOptionMapper.getLabelById(id);
    }

    private Map<String, Object> convertTokentoUsername(Map<String, Object> map) {
        User user = (User) TokenCasheManager.getUserCache(map.get("token").toString());
        if (map.get("queryType") != null && StringUtils.equals(String.valueOf(map.get("queryType")), "reference"))
            map.put("username", user.getUsername() + " reference");
        else
            map.put("username", user.getUsername());
        return map;
    }
}
