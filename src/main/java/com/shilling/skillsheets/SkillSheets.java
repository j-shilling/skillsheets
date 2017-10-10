package com.shilling.skillsheets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:beans.xml")
public class SkillSheets {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SkillSheets.class, args);
	}
}
