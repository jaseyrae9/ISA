package isa.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@EnableAsync
public class ServerAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerAppApplication.class, args);
	}
}
