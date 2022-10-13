package fr.univ.angers.proxies;

import fr.univ.angers.modele.LeadTO;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
import java.util.List;

public interface Proxy {
    public List<LeadTO> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state);
    public List<LeadTO> findLeadsByDate(XMLGregorianCalendar startDate, XMLGregorianCalendar endDate);
}
