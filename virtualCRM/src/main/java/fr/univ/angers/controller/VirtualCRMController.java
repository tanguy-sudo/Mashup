package fr.univ.angers.virtualCRM.controller;

import fr.univ.angers.virtualCRM.modele.LeadTO;
import fr.univ.angers.virtualCRM.service.VirtualCRMService;
import fr.univ.angers.virtualCRM.service.VirtualCRMServiceImpl;
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
    public List<LeadTO> findLeads(@RequestParam(defaultValue = "0") double lowAnnualRevenue,
                                  @RequestParam(defaultValue = "0") double highAnnualRevenue,
                                  @RequestParam(defaultValue = "")  String state) {
        return this.virtualCRMService.findLeads(lowAnnualRevenue, highAnnualRevenue, state);
    }

    @GetMapping("/findLeadsByDate")
    public List<LeadTO> findLeadsByDate(@RequestParam Calendar startDate, @RequestParam Calendar endDate) {
        return this.virtualCRMService.findLeadsByDate(startDate, endDate);
    }
}
