package com.kstd.talk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.kstd.talk")
public class TalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(TalkApplication.class, args);
	}

}
