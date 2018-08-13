package com.plateau.upload.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableFeignClients(basePackages={"com.plateau.upload.sample"})
@SpringCloudApplication
public class EurekaFeignUploadClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaFeignUploadClientApplication.class, args);
	}
}
