package fr.univ.angers.factories;

import fr.univ.angers.proxies.Proxy;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProxyFactory {
    private static Logger logger = Logger.getLogger(String.valueOf(ProxyFactory.class));

    public ProxyFactory(){}

    public static List<Proxy> getProxies() {
        List<Proxy> proxies = new ArrayList<Proxy>();
        Properties pros = new Properties();
        FileInputStream ip = null;

        try {
            // application.properties with jar
            ip = new FileInputStream("virtualCRM/src/main/resources/application.properties");
            // Charge le fichier de configuration
            pros.load(ip);
        } catch (Exception e) {
            logger.log(Level.WARNING, "wrong properties file");
        }

        String[] tabProxies = pros.getProperty("Proxies").split(",");

        for(String proxy : tabProxies) {
            try {
                Class c = Class.forName("fr.univ.angers.proxies." + proxy);
                java.lang.reflect.Constructor constructor = c.getConstructor();
                Proxy instance = (Proxy) constructor.newInstance();
                proxies.add(instance);
            } catch (Exception e) {
                logger.log(Level.WARNING, "class name does not exist");
            }
        }

        return proxies;
    }

}
