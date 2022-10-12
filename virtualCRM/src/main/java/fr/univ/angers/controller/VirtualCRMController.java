package fr.univ.angers.controller;

import fr.univ.angers.modele.LeadTO;
import fr.univ.angers.service.VirtualCRMService;
import fr.univ.angers.service.VirtualCRMServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Calendar;
import java.util.List;

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
    public List<LeadTO> findLeadsByDate(@RequestParam(required = true) Calendar startDate,
                                        @RequestParam(required = true) Calendar endDate) {
        return this.virtualCRMService.findLeadsByDate(startDate, endDate);
    }
}
