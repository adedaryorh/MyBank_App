package com.adedayo.AdexBank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Adex Banking Application",
				description = "This is a Backend REST for AdxeBank",
				version = "v1.0",
				contact = @Contact(
						name = "ADEDAYO ADEYEMI",
						email = "adedaryorh@gmail.com",
						url = "github_repo"
				),
				license = @License(
						name = "ADEDARYOR",
						url = "github_repo"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Adedayo Bank App Documentation",
				url = "github_repo"
		)
)
public class AdexBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdexBankApplication.class, args);
	}

}
