package com.ziyun.net.service;

import com.ziyun.net.NetApplication;
import com.ziyun.net.server.IA3RadacctTimeServer;
import com.ziyun.net.server.StudentServer;
import com.ziyun.net.vo.NetParams;
import com.ziyun.net.vo.Params;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * 测试上网模块service
 */
@SpringBootTest(classes = NetApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class NetServiceTest {
    @Autowired
    private StudentServer studentServer;
    @Autowired
    private IA3RadacctTimeServer radacctTimeService;

    @Test
    public void testMysql() {
        /*String outid = "134555216";
        ParamsStatus params = new ParamsStatus();
        params.setOutid(outid);
        List<String> list = studentServer.getPersonalStatus(params);
        //System.out.println(bigDecimal);
        list.stream().forEach(s -> {
            System.out.println(s);
        });*/

        try {
            Params para = new Params();
            Map<Integer, Integer> map = radacctTimeService.hourList(para);
            map.forEach((k, v) -> {
                System.out.println("k,v:" + k + "," + v);
            });
            System.out.println("----------------");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testKylin() {
        try {
            NetParams netParams = new NetParams();
            List<Map<String, Object>> mapList = radacctTimeService.getDurationTopTable(netParams);
            mapList.stream().forEach(m -> {
                m.forEach((k, v) -> {
                    System.out.println("k,v:" + k + "," + v);
                });
                System.out.println("----------------");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*@Test
    public void testRedis() {
        try {
            NetParams para = new NetParams();
            para.setOutid("124575019");
            Map<String, String> mapList = radacctTimeService.setRedis(para);
            mapList.forEach((k, v) -> {
                System.out.println("k,v:" + k + "," + v);
            });
            System.out.println("----------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Test
    public void testFeign() {
        try {
            NetParams netParams = new NetParams();

            List<Map<String, Object>> mapList = radacctTimeService.getNetStudentList(netParams);
            mapList.stream().forEach(m -> {
                m.forEach((k, v) -> {
                    System.out.println("k,v:" + k + "," + v);
                });
                System.out.println("----------------");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
