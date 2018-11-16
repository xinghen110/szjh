package com.magustek.szjh;

import com.magustek.szjh.config.ApplicationStartup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SzjhApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(SzjhApplication.class);
		springApplication.addListeners(new ApplicationStartup());
		springApplication.run(args);
	}

	@Bean
	ReloadableResourceBundleMessageSource messageSource(){
		ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
		source.setBasename("classpath:messages_zh_CN");
		return source;
	}
}
