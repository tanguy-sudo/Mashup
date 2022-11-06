package fr.univ.angers.factories;

import fr.univ.angers.CustomProperties;
import fr.univ.angers.proxies.Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ProxyFactory {
    private static Logger logger = Logger.getLogger(String.valueOf(ProxyFactory.class));
    @Autowired
    private CustomProperties customProperties;
    private List<Proxy> proxies;

    @PostConstruct
    private void postConstruct() {
        this.proxies = new ArrayList<Proxy>();
        String[] tabProxies = {};

        try {
            tabProxies = customProperties.getNames().split(",");
        } catch (Exception e) {
            logger.log(Level.WARNING, "no proxies" + e.getMessage());
        }

        for(String proxy : tabProxies) {
            try {
                Class c = Class.forName("fr.univ.angers.proxies." + proxy);
                java.lang.reflect.Constructor constructor = c.getConstructor();
                Proxy instance = (Proxy) constructor.newInstance();
                logger.log(Level.INFO, "class " + instance.getClass().getName() + " has generated");
                this.proxies.add(instance);
            } catch (Exception e) {
                logger.log(Level.WARNING, "class name does not exist");
            }
        }
    }

    public List<Proxy> getProxies() {
        return proxies;
    }
}
