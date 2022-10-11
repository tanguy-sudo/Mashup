package fr.univ.angers.virtualCRM;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

//@ComponentScan(basePackages = "fr.univ.angers.virtualCRM")
@EntityScan(basePackages = "fr.univ.angers.virtualCRM")
@SpringBootApplication
public class VirtualCrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualCrmApplication.class, args);
	}

}
