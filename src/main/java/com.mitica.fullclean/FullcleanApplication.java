package com.mitica.fullclean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.experimental.SuperBuilder;


@SpringBootApplication
public class FullcleanApplication {

	public static void main(String[] args) {
		SpringApplication.run(FullcleanApplication.class, args);
	}

	public final class TenantContext { private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>(); private TenantContext() {} 

}
