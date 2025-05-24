package com.proje.proje;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
@ComponentScan(basePackages = {"com.proje.proje"}) 

@SpringBootApplication
public class ProjeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjeApplication.class, args);
	}

}
