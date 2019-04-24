package com.magustek.szjh;

import com.magustek.szjh.config.ApplicationStartup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableScheduling
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
