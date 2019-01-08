package com.ziyun.borrow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableEurekaClient
@EnableFeignClients
@MapperScan(basePackages = "com.ziyun.borrow.mapper")
@SpringBootApplication
public class BorrowServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(BorrowServiceApplication.class, args);
	}
}
