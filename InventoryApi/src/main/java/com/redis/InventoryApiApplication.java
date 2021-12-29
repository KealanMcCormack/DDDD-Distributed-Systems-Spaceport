package com.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories
public class InventoryApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryApiApplication.class, args);
	}

}
