package com.ziyun.academic.server.impl;


import com.ziyun.academic.dao.FireWallFlowDao;
import com.ziyun.academic.entity.FireWallFlow;
import com.ziyun.academic.server.IFireWallFlowInfoServer;
import com.ziyun.utils.files.FileUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;


@Service
public class FireWallFlowInfoServerImpl implements IFireWallFlowInfoServer {

    @Autowired
    private FireWallFlowDao fwDao;

    /**
     * 重试次数
     */
    public static final int RETRY_TIMES = 10;

    /**
     * 防火墙
     */
    @Value(value = "${fire.wall.login.url}")
    private String firewallLoginUrl;//防火墙登录url

    /**
     * chorme driver路径
     */
    @Value(value = "${chrome.driver.path}")
    private String chromeDriverPath;

    /**
     * 生成的临时文件的路径
     */
    @Value(value = "${tempfile.path}")
    private String tempFilePath;

    /**
     * 抓取页面的tbody的id,会改变
     */
    @Value(value = "${tbody.id}")
    private String tbodyId;

    /**
     * 防火墙用户名
     */
    @Value(value = "${firewall.username}")
    private String username;

    /**
     * 防火墙密码
     */
    @Value(value = "${firewall.password}")
    private String password;

    @Override
    public void grabFwflow() throws Exception {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        WebDriver driver = new ChromeDriver();
        login(driver, 1);

        //移动
        FireWallFlow cmcc = new FireWallFlow();

        //电信
        FireWallFlow chinaNet = new FireWallFlow();

        try {
            //移动流量
            cmcc = getFlowInfo(driver, By.id(tbodyId), 10);
            //电信流量
            chinaNet = getFlowInfo(driver, By.id(tbodyId), 12);
        } catch (Exception e) {
            //
        } finally {
            //获取数据后退出
            if (null != driver) {
                driver.quit();
            }
        }

        //获取当前时间
        Date now = new Date();
        cmcc.setDate(now);
        cmcc.setType("CMCC");

        chinaNet.setDate(now);
        chinaNet.setType("CHINANET");

        //插入数据库
        if (StringUtils.isNotBlank(cmcc.getAllflow())) {
            cmcc.setName("xethernet2/0");
            fwDao.insert(cmcc);
        }

        //插入数据库
        if (StringUtils.isNotBlank(chinaNet.getAllflow())) {
            chinaNet.setName("xethernet2/2");
            fwDao.insert(chinaNet);
        }

        //删除零时文件
        deleteDirectorys(tempFilePath);


    }


    /**
     * 模拟登录Hillstone
     *
     * @param driver
     */
    private void login(WebDriver driver, int flag) {
        int retryCount = -1;
        String loginUrl = firewallLoginUrl;
        do {
            try {
                driver.get(loginUrl);
                WebElement userNameElem = driver.findElement(By.id("login_username"));
                WebElement passwordElem = driver.findElement(By.id("login_passwd"));
                userNameElem.sendKeys(username);
                passwordElem.sendKeys(password);
                WebElement loginElem = driver.findElement(By.id("login_button"));
                loginElem.click();
                waitLoad(25000);
            } catch (Exception e) {
                flag = 0;
                e.printStackTrace();
                if (null != driver) {
                    driver.quit();
                }
            } finally {
                if (flag == 0) {
                    flag = 1;
                    retryCount++;
                    if (retryCount == RETRY_TIMES) {
                        if (null != driver) {
                            driver.quit();
                        }
                        break;
                    }
                } else {
                    flag = 0;
                    //logger.debug("Success login!");
                }
            }
        } while (flag == 1);
    }

    /**
     * 等待页面加载时间
     *
     * @param time
     */
    private void waitLoad(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取table一行中所有列的text. 参数rowIndex为行, 行列从0开始.
     *
     * @param driver
     * @param by
     * @param
     * @return
     */
    public FireWallFlow getFlowInfo(WebDriver driver, By by, int rowIndex) {

        //获取table元素对象  
        WebElement table = driver.findElement(by);
        //获取table表中所有行对象  
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        //获取具体的某一行对象
        WebElement theRow = rows.get(rowIndex);

        //一行的所有td
        List<WebElement> cells;

        FireWallFlow fw = new FireWallFlow();
        if (theRow.findElements(By.tagName("td")).size() > 0) {
            cells = theRow.findElements(By.tagName("td"));
            if (null != cells) {
                fw.setState(cells.get(1).getAttribute("data-qtip"));//状态
                fw.setIp(cells.get(2).getText());//IP

                String upflow = cells.get(3).getText();//上行
                String downflow = cells.get(4).getText();//下行
                String allflow = cells.get(5).getText();//总流量

                fw.setUpflow(changeFlow(upflow));
                fw.setDownflow(changeFlow(downflow));
                fw.setAllflow(changeFlow(allflow));
            }
        }

        return fw;
    }

    /**
     * 把单位Gbps 转换成Mbps
     *
     * @param flowInfo "204.13 Mbps" or "1.65 Gbps"
     * @return
     */
    public String changeFlow(String flowInfo) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.CEILING);
        String flow = "";
        if (StringUtils.isNotBlank(flowInfo)) {

            String flowstr = flowInfo.split(" ")[0];
            String unit = flowInfo.split(" ")[1];
            if ("Gbps".equals(unit)) {
                flow = df.format(Double.valueOf(flowstr) * 1000);
            } else {
                flow = flowstr;
            }
        }

        return flow;
    }

    ;

    /**
     * 删除目录下的以scoped_dir开头的文件夹及子文件
     *
     * @param
     */
    public void deleteDirectorys(String dirName) {
        String dirNames = dirName;
        if (!dirNames.endsWith(File.separator)) {
            dirNames = dirNames + File.separator;
        }
        File dirFile = new File(dirNames);
        if (dirFile.isDirectory()) {
            String[] subs = dirFile.list();
            if (ArrayUtils.isEmpty(subs)){
                return;
            }

            for (String subName : subs) {
                if (subName.indexOf("scoped_dir") != -1) {
                    String dir = dirName + "\\" + subName;
                    FileUtil.deleteDirectory(dir);
                }
            }
        }
    }
}
