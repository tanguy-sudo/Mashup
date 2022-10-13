package fr.univ.angers.service;

import fr.univ.angers.modele.LeadTO;
import fr.univ.angers.proxies.Proxy;
import org.springframework.stereotype.Service;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class VirtualCRMServiceImpl implements  VirtualCRMService{
    private List<Proxy> proxies;
    private static Logger logger = Logger.getLogger(String.valueOf(VirtualCRMServiceImpl.class));

    public VirtualCRMServiceImpl() {
        this.proxies = new ArrayList<Proxy>();
        Properties pros = new Properties();
        FileInputStream ip = null;

        try {
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
    }

    @Override
    public List<LeadTO> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) {
        List<LeadTO> leadTOs = new ArrayList<LeadTO>();
        for(Proxy proxy : this.proxies) {
            leadTOs.addAll(proxy.findLeads(lowAnnualRevenue, highAnnualRevenue, state));
        }
        return leadTOs;
    }

    @Override
    public List<LeadTO> findLeadsByDate(XMLGregorianCalendar startDate, XMLGregorianCalendar endDate) {
        List<LeadTO> leadTOs = new ArrayList<LeadTO>();
        for(Proxy proxy : this.proxies) {
            leadTOs.addAll(proxy.findLeadsByDate(startDate, endDate));
        }
        return leadTOs;
    }
}
