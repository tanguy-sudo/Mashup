package fr.univ.angers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

//@ComponentScan(basePackages = "fr.univ.angers.virtualCRM")
@EntityScan(basePackages = "fr.univ.angers.virtualCRM")
@SpringBootApplication
public class VirtualCrmApplication {
	public static void main(String[] args) {
		SpringApplication.run(VirtualCrmApplication.class, args);
	}
}
