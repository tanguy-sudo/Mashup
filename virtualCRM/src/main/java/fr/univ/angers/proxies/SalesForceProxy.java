package fr.univ.angers.proxies;

import fr.univ.angers.modele.LeadTO;

import java.util.Calendar;
import java.util.List;

public class SalesForceProxy implements Proxy {
    @Override
    public List<LeadTO> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) {
        return null;
    }

    @Override
    public List<LeadTO> findLeadsByDate(Calendar startDate, Calendar endDate) {
        return null;
    }
}
