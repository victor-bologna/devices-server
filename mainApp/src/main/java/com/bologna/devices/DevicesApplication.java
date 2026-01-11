package com.bologna.devices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = "com.bologna.devices")
@EntityScan(basePackages = "com.bologna.devices.persistence")
public class DevicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevicesApplication.class, args);
	}

}
