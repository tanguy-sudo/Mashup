package internalCRM;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "fr.univ.angers.internalCRM")
public class ProducingWebServiceApplication {
    public static void main(String[] args) {
        System.setProperty("com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize", "true");
        SpringApplication.run(ProducingWebServiceApplication.class, args);
    }
}
