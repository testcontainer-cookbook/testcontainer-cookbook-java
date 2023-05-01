package com.example.mockserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(clients = Client.class)
@SpringBootApplication
public class MockserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(MockserverApplication.class, args);
	}

}
