package fr.univ.angers.controller;

import fr.univ.angers.modele.LeadTO;
import fr.univ.angers.service.VirtualCRMService;
import fr.univ.angers.service.VirtualCRMServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@RestController
@RequestMapping("/virtualCRM")
public class VirtualCRMController {
    private final VirtualCRMService virtualCRMService;

    public VirtualCRMController() {
        this.virtualCRMService = new VirtualCRMServiceImpl();
    }

    @GetMapping("/findLeads")
    public List<LeadTO> findLeads(@RequestParam(required = true) double lowAnnualRevenue,
                                  @RequestParam(required = true) double highAnnualRevenue,
                                  @RequestParam(required = true)  String state) {
        return this.virtualCRMService.findLeads(lowAnnualRevenue, highAnnualRevenue, state);
    }

    @GetMapping("/findLeadsByDate")
    public List<LeadTO> findLeadsByDate(@RequestParam(required = true) String startDate,
                                        @RequestParam(required = true) String endDate) {
        String[] sTab = startDate.split("-");
        String[] eTab = endDate.split("-");
        XMLGregorianCalendar start = DatatypeFactory.newDefaultInstance()
                .newXMLGregorianCalendarDate(Integer.valueOf(sTab[0]),
                                             Integer.valueOf(sTab[1]),
                                             Integer.valueOf(sTab[2]),
                                             TimeZone.SHORT);
        XMLGregorianCalendar end = DatatypeFactory.newDefaultInstance()
                .newXMLGregorianCalendarDate(Integer.valueOf(eTab[0]),
                                             Integer.valueOf(eTab[1]),
                                             Integer.valueOf(eTab[2]),
                                             TimeZone.SHORT);
        return this.virtualCRMService.findLeadsByDate(start, end);
    }
}
