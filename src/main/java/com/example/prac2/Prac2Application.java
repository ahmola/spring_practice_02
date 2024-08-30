package com.example.prac2;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Properties;

@SpringBootApplication
public class Prac2Application {

	public static void main(String[] args) throws Exception{
		SpringApplication app = new SpringApplication(Prac2Application.class);

		Resource resource = new ClassPathResource("application_path.yml");
		YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
		yaml.setResources(resource);
		Properties properties = yaml.getObject();

		app.setDefaultProperties(properties);

		app.run(args);
	}

}
