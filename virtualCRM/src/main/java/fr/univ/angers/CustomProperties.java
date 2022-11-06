package fr.univ.angers;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "virtual.proxies")
public class CustomProperties {
    private String names;

    public String getNames() {
        return names;
    }
    public void setNames(String names) {
        this.names = names;
    }
}
