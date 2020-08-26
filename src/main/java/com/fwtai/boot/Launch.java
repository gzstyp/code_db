package com.fwtai.boot;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Properties;

@ComponentScan({"com.fwtai"})
@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class Launch extends SpringBootServletInitializer {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Launch.class,args);
		System.out.println("应用启动成功");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Launch.class);
	}

	@Bean
	public DatabaseIdProvider getDatabaseIdProvider() {
		final DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
		final Properties p = new Properties();
		p.setProperty("Oracle", "oracle");
		p.setProperty("MySQL", "mysql");
		databaseIdProvider.setProperties(p);
		return databaseIdProvider;
	}
}