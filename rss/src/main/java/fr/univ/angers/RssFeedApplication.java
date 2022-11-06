package fr.univ.angers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = "fr.univ.angers.rss")
@SpringBootApplication
public class RssFeedApplication {
    public static void main(final String[] args) {
        SpringApplication.run(RssFeedApplication.class, args);
    }
}
