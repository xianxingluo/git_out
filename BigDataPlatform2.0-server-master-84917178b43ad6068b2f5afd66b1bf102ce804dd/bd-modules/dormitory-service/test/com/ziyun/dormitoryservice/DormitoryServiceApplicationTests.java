package com.ziyun.dormitoryservice;

import com.ziyun.common.model.ParamsStatus;
import com.ziyun.dormitory.DormitoryServiceApplication;
import com.ziyun.dormitory.service.IEcardAccessInoutService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DormitoryServiceApplication.class)
public class DormitoryServiceApplicationTests {
	//@Autowired
	//EcardAccessInoutMapper accessInoutMapper;
	@Autowired
	private IEcardAccessInoutService accessInoutService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testKylinDataResources(){
		ParamsStatus para = new ParamsStatus();
		para.setOutid("178111563141");
		Long size = accessInoutService.personNoComeBackSize(para);
		System.out.println("共有"+size+"条记录！");
	}

	@Test
	public void testRead() throws Exception {
		//Excel文件
		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(ResourceUtils.getFile("classpath:xls/dorm_record.xls")));
		//Excel工作表
		HSSFSheet sheet = wb.getSheetAt(0);

		//表头那一行
		HSSFRow titleRow = sheet.getRow(0);

		//表头那个单元格
		HSSFCell titleCell = titleRow.getCell(0);

		String title = titleCell.getStringCellValue();

		System.out.println("标题是："+title);
	}

	@Test
	public void testPython1() throws Exception {
		String text="呢。\r\n" +
				"　　他重新经过安检，回到了候机大厅。大厅里仍是一片嘈杂。他强迫自己镇静，在饮水机前喝了几口水，找了一处空椅子坐下，闭目养神。已经落网的赵德汉的形象适时浮现在眼前，他禁不住又沉浸到了对赵德汉的回忆中。昨天晚上，当此人捧着大海碗吃炸酱面时，老旧的木门“吱呀”一声开了，他代表命运来敲这位贪官的家门了。\r\n" +
				"　　贪官一脸憨厚相，乍看上去，不太像机关干部，倒像个刚";
		//定义个获取结果的变量
		String result="";
		try {
			//调用python，其中字符串数组对应的是python，python文件路径，向python传递的参数
			String[] strs=new String[] {"python","F:\\test1.py",text};
			//Runtime类封装了运行时的环境。每个 Java 应用程序都有一个 Runtime 类实例，使应用程序能够与其运行的环境相连接。
			//一般不能实例化一个Runtime对象，应用程序也不能创建自己的 Runtime 类实例，但可以通过 getRuntime 方法获取当前Runtime运行时对象的引用。
			// exec(String[] cmdarray) 在单独的进程中执行指定命令和变量。
			System.out.println("开始调用：" + new Date().getTime());
			Process pr = Runtime.getRuntime().exec(strs);
			System.out.println("调用结束：" + new Date().getTime());
			//使用缓冲流接受程序返回的结果
			BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream(),"GBK"));//注意格式
			//定义一个接受python程序处理的返回结果
			String line=" ";
			while((line=in.readLine())!=null) {
				//循环打印出运行的结果
				result+=line+"\n";
			}
			//关闭in资源
			in.close();
			pr.waitFor();
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("python传来的结果：");
		//打印返回结果
		System.out.println(result);
	}

	@Test
	public void testPython2() throws Exception {
		int a = 18;
		int b = 23;
		try {
			String[] args = new String[] { "python", "F:\\demo1.py", String.valueOf(a), String.valueOf(b) };
			System.out.println("开始调用：" + new Date().getTime());
			Process proc = Runtime.getRuntime().exec(args);// 执行py文件
			System.out.println("调用结束：" + new Date().getTime());
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
			in.close();
			proc.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}



}
