package com.ziyun.dormitory;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@MapperScan(basePackages = "com.ziyun.dormitory.mapper")
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class
})
public class DormitoryServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(DormitoryServiceApplication.class, args);
	}
}
