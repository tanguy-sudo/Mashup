package fr.univ.angers.service;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

public interface InternalCRMService {

    public List<ws_crm.InternalClient> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state);
    public List<ws_crm.InternalClient> findLeadsByDate(XMLGregorianCalendar startDate, XMLGregorianCalendar endDate);
}
