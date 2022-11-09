package fr.univ.angers.service;

import fr.univ.angers.repository.InternalCRMRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

@Component
public class InternalCRMServiceImpl implements InternalCRMService {
    @Autowired
    private InternalCRMRepository internalCRMRepository;

    @Override
    public List<ws_crm.InternalClient> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) {
        List<ws_crm.InternalClient> list = new ArrayList<ws_crm.InternalClient>();
        for(ws_crm.InternalClient client : this.internalCRMRepository.getLeadTOs()) {
            if((client.getAnnualRevenue() >= lowAnnualRevenue) && (client.getAnnualRevenue() <= highAnnualRevenue) && (client.getState().equals(state))) {
                list.add(client);
            }
        }
        return list;
    }

    @Override
    public List<ws_crm.InternalClient> findLeadsByDate(XMLGregorianCalendar startDate, XMLGregorianCalendar endDate) {
        List<ws_crm.InternalClient> list = new ArrayList<ws_crm.InternalClient>();
        for(ws_crm.InternalClient client : this.internalCRMRepository.getLeadTOs()) {
            if((client.getCreationDate().compare(startDate) == DatatypeConstants.EQUAL) ||
                    (client.getCreationDate().compare(endDate) == DatatypeConstants.EQUAL)) {
                list.add(client);
            }
            else if((client.getCreationDate().compare(startDate) == DatatypeConstants.GREATER) &&
                    (client.getCreationDate().compare(endDate) == DatatypeConstants.LESSER)) {
                list.add(client);
            }
        }
        return list;
    }
}
