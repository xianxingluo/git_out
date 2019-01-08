package com.ziyun.borrow;

import com.ziyun.borrow.service.IEduBorrowService;
import com.ziyun.common.model.Params;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BorrowServiceApplication.class)
public class BorrowServiceApplicationTests {

    @Autowired
    public IEduBorrowService borrowServe;

	@Test
	public void contextLoads() {
	}

    @Test
    public void testKylinDataResources() throws Exception {
        Params para = new Params();
        para.setOutid("144576589");
        List<Map<String, Object>> mapList = borrowServe.preferenceList(para);
        mapList.stream().forEach(map -> {
            map.forEach((k, v) -> {
                System.out.println("k,v:" + k + "," + v);
            });
        });
    }
}
