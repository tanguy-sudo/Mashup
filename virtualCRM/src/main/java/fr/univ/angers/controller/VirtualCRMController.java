package fr.univ.angers.controller;

import fr.univ.angers.factories.ProxyFactory;
import fr.univ.angers.modele.GeographicPointTO;
import fr.univ.angers.modele.LeadTO;
import fr.univ.angers.proxies.Proxy;
import fr.univ.angers.service.VirtualCRMService;
import fr.univ.angers.service.VirtualCRMServiceImpl;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/virtualCRM")
public class VirtualCRMController {
    private final List<Proxy> proxies;
    private final VirtualCRMService virtualCRMService;
    private static Logger logger = Logger.getLogger(String.valueOf(VirtualCRMController.class));

    public VirtualCRMController() {
        this.virtualCRMService = new VirtualCRMServiceImpl();
        this.proxies = ProxyFactory.getProxies();
    }

    @GetMapping("/findLeads")
    public List<LeadTO> findLeads(@RequestParam(required = true) double lowAnnualRevenue,
                                  @RequestParam(required = true) double highAnnualRevenue,
                                  @RequestParam(required = true)  String state) {
        List<LeadTO> leadTOs = new ArrayList<LeadTO>();
        for(Proxy proxy : this.proxies) {
            leadTOs.addAll(proxy.findLeads(lowAnnualRevenue, highAnnualRevenue, state));
        }
        return callOpenStreetMap(leadTOs);
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

        List<LeadTO> leadTOs = new ArrayList<LeadTO>();
        for(Proxy proxy : this.proxies) {
            leadTOs.addAll(proxy.findLeadsByDate(start, end));
        }
        return callOpenStreetMap(leadTOs);
    }

    private static List<LeadTO> callOpenStreetMap(List<LeadTO> leadTOs) {
        for(LeadTO leadTO : leadTOs) {
            String uri = "https://nominatim.openstreetmap.org/search?adressdetails=1&";
            uri += "city=" + leadTO.getCity() +"&";
            uri += "country=" + leadTO.getCountry() + "&";
            uri += "postalcode=" + leadTO.getPostalCode() +"&";
            uri += "street=" + leadTO.getStreet().replaceAll(" ", "+") + "&";
            uri += "format=json&limit=1";

            String response = execute(uri);
            System.out.println(response);
            JSONArray jsonArray = new JSONArray(response);
            GeographicPointTO geographicPointTO = new GeographicPointTO();
            geographicPointTO.setLongitude(jsonArray.getJSONObject(0).getDouble("lon"));
            geographicPointTO.setLatitude(jsonArray.getJSONObject(0).getDouble("lat"));
            leadTO.setGeographicPointTO(geographicPointTO);
        }
        return leadTOs;
    }

    private static String execute(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return response.body();
    }
}
