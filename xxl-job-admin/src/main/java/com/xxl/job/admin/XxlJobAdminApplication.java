package com.xxl.job.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author xuxueli 2018-10-28 00:38:13
 */
@SpringBootApplication
@ServletComponentScan("com.xxl.job.admin.*")
public class XxlJobAdminApplication {

	public static void main(String[] args) {
        SpringApplication.run(XxlJobAdminApplication.class, args);
	}

}