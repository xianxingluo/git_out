package com.ziyun.academic.tools;

import org.apache.commons.collections.map.HashedMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2018/1/23.
 */
public class UserDefinedUtils {

    /**
     * 将自定义人群属性的参数塞在map里面
     *
     * @return
     */
    public static Map<String, List<String>> getUserDefined() {
        Map<String, List<String>> userDefined = new HashedMap();
        List<String> graduateList = new ArrayList();
        graduateList.add("全部");
        graduateList.add("毕业生");
        graduateList.add("肄业");
        graduateList.add("其他");
        List<String> scholasticList = new ArrayList();
        scholasticList.add("全部");
        scholasticList.add("新生");
        scholasticList.add("老生");
        scholasticList.add("留级");
        scholasticList.add("休学");
        List<String> timeList = new ArrayList();
        timeList.add("在校期间");
        timeList.add("放假期间");
        userDefined.put("semester", null);
        userDefined.put("graduate", graduateList);
        userDefined.put("scholastic", scholasticList);
        userDefined.put("time", timeList);
        return userDefined;
    }
}
