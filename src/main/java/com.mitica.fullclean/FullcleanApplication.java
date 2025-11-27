package com.mitica.fullclean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import lombok.experimental.SuperBuilder;

@SpringBootApplication
@ServletComponentScan
public class FullcleanApplication {

	public static void main(String[] args) {
		SpringApplication.run(FullcleanApplication.class, args);
	}


}
