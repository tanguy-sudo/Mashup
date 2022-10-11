package fr.univ.angers.service;

import fr.univ.angers.modele.LeadTO;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class VirtualCRMServiceImpl implements  VirtualCRMService{

    @Override
    public List<LeadTO> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) {
        LeadTO leadTO = new LeadTO();
        List<LeadTO> leadTOs = new ArrayList<LeadTO>();
        leadTOs.add(leadTO);
        return leadTOs;
    }

    @Override
    public List<LeadTO> findLeadsByDate(Calendar startDate, Calendar endDate) {
        LeadTO leadTO = new LeadTO();
        List<LeadTO> leadTOs = new ArrayList<LeadTO>();
        leadTOs.add(leadTO);
        return leadTOs;
    }
}
