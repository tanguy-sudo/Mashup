package fr.univ.angers;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "virtual.proxies")
public class CustomProperties {
    private String names;
    private String username;
    private String password;
    private String url;
    private String query_url;
    private String client_id;
    private String client_secret;

    public String getNames() {
        return names;
    }
    public void setNames(String names) {
        this.names = names;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getQuery_url() {
        return query_url;
    }
    public void setQuery_url(String query_url) {
        this.query_url = query_url;
    }

    public String getClient_id() {
        return client_id;
    }
    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }
    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }
}
