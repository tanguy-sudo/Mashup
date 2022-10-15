package fr.univ.angers.service;

import fr.univ.angers.modele.LeadTO;
import org.springframework.stereotype.Service;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

@Service
public class VirtualCRMServiceImpl implements  VirtualCRMService{

    public VirtualCRMServiceImpl() {
    }

    @Override
    public List<LeadTO> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) {
        return null;
    }

    @Override
    public List<LeadTO> findLeadsByDate(XMLGregorianCalendar startDate, XMLGregorianCalendar endDate) {
        return null;
    }
}
