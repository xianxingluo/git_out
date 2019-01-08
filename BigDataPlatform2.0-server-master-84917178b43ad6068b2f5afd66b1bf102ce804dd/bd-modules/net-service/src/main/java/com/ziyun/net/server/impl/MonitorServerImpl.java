package com.ziyun.net.server.impl;


import com.ziyun.net.entity.A3Hotspot;
import com.ziyun.net.entity.FireWallFlowMonitor;
import com.ziyun.net.mapper.A3HotspotMapper;
import com.ziyun.net.mapper.MonitorMapper;
import com.ziyun.net.mapper.OnLineNumMapper;
import com.ziyun.net.server.MonitorServer;
import com.ziyun.net.tools.CalendarUtils;
import com.ziyun.net.tools.ParamUtils;
import com.ziyun.net.vo.*;
import com.ziyun.utils.cache.RedisUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class MonitorServerImpl implements MonitorServer {

    public static final String FlowListKEY = "flowList";

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private MonitorMapper monitorMapper;

    @Autowired
    private A3HotspotMapper a3HotspotMapper;
    @Autowired
    private OnLineNumMapper onLineNumMapper;

    /**
     * 查询实时在线人数
     */
    @Override
    public int getOnLineNum() {
        return monitorMapper.getOnLineNum();
    }


    /**
     * 在线人员信息
     */
    @Override
    public List<LinkedHashMap<String, Object>> getOnLinePersion() {
        return monitorMapper.getOnLinePersion();
    }

    @Override
//	@Cacheable(value="baseCache",key="#root.target.FlowListKEY")
    public Map<String, Object> getIpgeo() {
        Map<String, Object> result = new HashMap<>();
        // 查詢所有城市信息
        List<LinkedHashMap<String, String>> cityMaps = monitorMapper.getCityList();
        List<City> citylist = new ArrayList<>();
        City root = null;
        for (LinkedHashMap<String, String> linkedHashMap : cityMaps) {
            String cityName = linkedHashMap.get("city");
            if (cityName == null || "".equals(cityName)) {
                cityName = linkedHashMap.get("city_en");
            }
            if (cityName == null || "".equals(cityName)) {
                cityName = linkedHashMap.get("country");
            }
            City city = new City();
            city.setName(cityName);
            city.setSerNum(linkedHashMap.get("symbolSize"));
            if ("Zhangjiagang".equals(cityName) || "张家港".equals(cityName)) {
                city.setName("张家港");
                city.getValue()[0] = "120.57765";
                city.getValue()[1] = "31.897941";
                city.setSymbolSize("10");
                Map zhangjiagang = new HashMap();
                ItemStyle item = new ItemStyle();
                zhangjiagang.put("color", "#FFFFFF");
                item.setNormal(zhangjiagang);
                city.setItemStyle(item);
                root = city;
            } else {
                city.getValue()[0] = linkedHashMap.get("longitude");
                city.getValue()[1] = linkedHashMap.get("latitude");
            }
            citylist.add(city);
        }
        if (root == null) {
            root = new City();
            root.setName("张家港");
            root.getValue()[0] = "120.57765";
            root.getValue()[1] = "31.897941";
            root.setSymbolSize("10");
            Map zhangjiagang = new HashMap();
            ItemStyle item = new ItemStyle();
            zhangjiagang.put("color", "#FFFFFF");
            item.setNormal(zhangjiagang);
            root.setItemStyle(item);
        }

        // line
        List<MoveLine> lines = new ArrayList<>();
        for (City city : citylist) {
            MoveLine line = new MoveLine();
            line.setFromCoords(city.getValue());
            line.setToCoords(root.getValue());
            line.setFromName(city.getName());
            line.setToName(root.getName());
            line.setSerNum(city.getSerNum());
            lines.add(line);
        }
        result.put("citys", citylist);
        result.put("lines", lines);

        return result;
    }


    /**
     * 今日流量速率数据
     */
    @Override
    public List<FireWallFlowMonitor> getRate() {
        return monitorMapper.getRate();
    }

    /**
     * 今日在线人员热点访问信息
     */
    @Override
    public List<HotSpotVo> getTopHotSpot(Params para) {
        Date date = new Date();
        //创建时间戳。用来存取数据
        String timestamp = CalendarUtils.toYyyyMMdd(date);
        //获取本次更新的上限时间
        String edate = CalendarUtils.toYyyy2MM2ddHHmmss(date);
        para.setEdate(edate);
        // week sum 类型 Map<week, Map<类型,sum>> 数据类型 MAP<week List<>>
        Map<String, Object> hotspotMap = new HashMap();
        if (redisTemplate.hasKey(timestamp)) {
            hotspotMap = (Map)redisTemplate.opsForValue().get(timestamp);
            para.setBdate(String.valueOf(hotspotMap.get("bdate")));
        }
        List<A3Hotspot> spots = a3HotspotMapper.listToday(para);
        for (A3Hotspot a3Hotspot : spots) {// 遍历记录将结果累加
            cleanHotspotByTimes(hotspotMap, a3Hotspot);// 将记录中的详细信息
        }
        //将本次的上限时间存入map中。用做下次的下限时间
        hotspotMap.put("bdate",edate);
        redisTemplate.opsForValue().set(timestamp, hotspotMap, 43200, TimeUnit.SECONDS);
        List<HotSpotVo> volist = new ArrayList<>();
        String vHot="其他,p2p";
        for (Map.Entry<String, Object> entry : hotspotMap.entrySet()){
            String key = entry.getKey();
            if(vHot.contains(key) || "edate".equals(key) || "bdate".equals(key)){
                continue;
            }
            HotSpotVo vo = new HotSpotVo();
            vo.setType(key);
            vo.setNum(Integer.valueOf(String.valueOf(entry.getValue())));
            volist.add(vo);
        }
        Collections.sort(volist);
        if (null == volist || volist.size() == 0)
            return null;
        if (volist.size() > 5) {
            return volist.subList(0, 5);
        } else {
            return volist;
        }
    }

    /**
     * 深信服数据 根据人次累加
     *
     * @param map
     * @param a3Hotspot
     * @return
     */
    private void cleanHotspotByTimes(Map<String, Object> map,
                                     A3Hotspot a3Hotspot) {
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        Detail temp = a3Hotspot.getDetail();
        if (temp != null) {
            List<Spot> sport = temp.getData();
            for (Spot spot : sport) {
                String appName = spot.getApp();
                appName = appName.split(":")[0];
                Object i = map.get(appName);
                if (i == null) {
                    i = new Integer(1);
                } else {
                    Integer j = Integer.valueOf(String.valueOf(i));
                    i = ++j;
                }
                map.put(appName, i);
            }
        }
    }

    @Override
    public void insertOnLineNum() {
        onLineNumMapper.insertOnLineNum();
    }

    /**
     * 查询历史在线人数
     *
     * @return
     */
    @Override
    public List<Map> getOnLineNumHis() {
        List<Map> onLineNumList = onLineNumMapper.listOnLineNum();
        if (null == onLineNumList || onLineNumList.size() == 0)
            return null;
        //获取当前在线人数，判断是否大于0，大于0 的时候存入
        int nowStudent = monitorMapper.getOnLineNum();
        if (nowStudent > 0) {
            Map lastMap = onLineNumList.get(onLineNumList.size() - 1);
            lastMap.put("number", nowStudent);
            onLineNumList.set(onLineNumList.size() - 1, lastMap);
        }
        return onLineNumList;
    }

    @Override
    public Map getOnlineStudent(ParamsStatus param) {
        List<Map<String, Object>> lists;
        //判断前端是否传递时间戳没有时间戳不进行读取redis操作
        if (param.getTimeStamp() != null) {
            //判断是否存在redis缓存中没有的话读取数据并存入缓存
            if (redisTemplate.hasKey(param.getTimeStamp())) {
                lists = (List<Map<String, Object>>) redisTemplate.opsForValue().get(param.getTimeStamp());
            } else {
                lists = monitorMapper.getOnlineStudent();
                //设置缓存失效时间，由于一次显示5条每3秒刷新一次。因此时间为size/5*3
                int time = lists.size() / 4 * 3 > 0 ? lists.size() / 4 * 3 : 1;
                redisTemplate.opsForValue().set(param.getTimeStamp(), lists, time, TimeUnit.SECONDS);
            }
        } else {
            lists = monitorMapper.getOnlineStudent();
        }
        if (null == lists || lists.size() == 0) {
            return null;
        }
        List<Map<String, Object>> resultList = ParamUtils.limitPage(lists, param.getStart(), param.getLimit());
        int length = lists.size();
        Map<String, Object> map = new HashedMap();
        map.put("data", resultList);
        map.put("total", length);
        return map;
    }

    @Override
    public Map<String, Object> getWorldIp() {
        List<LinkedHashMap<String, String>> cityLists = monitorMapper.getCityList();
        if (null == cityLists || cityLists.size() == 0)
            return null;
        Map resultMap = new HashMap();
        List<Map<String, Object>> cityValue = new ArrayList<>();
        List<Map<String, Object>> cityLines = new ArrayList<>();
        cityLists.forEach(c -> {
            Map<String, Object> map = new HashMap<>();
            //传递给前端name的优先级city->city_en->country
            String[] value;
            String cityName = c.get("city");
            if (cityName == null || "".equals(cityName)) {
                cityName = c.get("city_en");
            }
            if (cityName == null || "".equals(cityName)) {
                cityName = c.get("country");
            }
            if ("Zhangjiagang".equals(cityName) || "张家港".equals(cityName)) {
                cityName = "张家港";
                value = new String[]{"120.57765", "31.897941"};
            } else {
                value = new String[]{c.get("longitude"), c.get("latitude")};
            }
            map.put("name", cityName);
            map.put("serNum", c.get("symbolSize"));
            map.put("value", value);
            cityValue.add(map);
            map = null;
        });
        cityValue.forEach(cr -> {
            Map<String, Object> map = new HashMap<>();
            List list = new ArrayList();
            list.add(cr.get("value"));
            list.add(new String[]{"120.57765", "31.897941"});
            map.put("fromName", cr.get("name"));
            map.put("serNum", cr.get("serNum"));
            map.put("toName", "张家港");
            map.put("coords", list);
            cityLines.add(map);
            map = null;
        });
        resultMap.put("citys", cityValue);
        resultMap.put("lines", cityLines);
        return resultMap;
    }

}
