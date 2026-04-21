package com.ana.olist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {
        "com.ana.olist",
        "com.ana.common.security.libs"
})
//@ConfigurationPropertiesScan
//@SpringBootApplication
public class OlistServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OlistServiceApplication.class, args);
	}

}
