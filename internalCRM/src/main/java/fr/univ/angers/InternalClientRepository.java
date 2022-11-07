package fr.univ.angers;

import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

@Component
public class InternalClientRepository {
    private static final List<ws_crm.InternalClient> leadTOs = new ArrayList<ws_crm.InternalClient>();

    @PostConstruct
    public void initData() {

        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newDefaultInstance()
                .newXMLGregorianCalendar(2022, 10, 23, 9, 30, 0, 0, TimeZone.SHORT);

        ws_crm.InternalClient internalClient = new ws_crm.InternalClient();
        internalClient.setFirstAndLastName("tanguy,jouvin");
        internalClient.setAnnualRevenue(38000.0);
        internalClient.setPhone("0123456789");
        internalClient.setStreet("19 rue du laurier");
        internalClient.setPostalCode("49000");
        internalClient.setCity("Angers");
        internalClient.setCountry("France");
        internalClient.setCreationDate(xmlGregorianCalendar);
        internalClient.setCompany("CapGemini");
        internalClient.setState("Pays de la loire");
        leadTOs.add(internalClient);

        ws_crm.InternalClient internalClient2 = new ws_crm.InternalClient();
        internalClient2.setFirstAndLastName("guillaume,grenon");
        internalClient2.setAnnualRevenue(36000.0);
        internalClient2.setPhone("0458569545");
        internalClient2.setStreet("19 rue de bretagne");
        internalClient2.setPostalCode("49000");
        internalClient2.setCity("Angers");
        internalClient2.setCountry("France");
        internalClient2.setCreationDate(xmlGregorianCalendar);
        internalClient2.setCompany("Sopra");
        internalClient2.setState("Pays de la loire");
        leadTOs.add(internalClient2);


        ws_crm.InternalClient internalClient3 = new ws_crm.InternalClient();
        internalClient3.setFirstAndLastName("julien, laffeli");
        internalClient3.setAnnualRevenue(41000.0);
        internalClient3.setPhone("04582569545");
        internalClient3.setStreet("3 rue du champ de bataille");
        internalClient3.setPostalCode("49000");
        internalClient3.setCity("Angers");
        internalClient3.setCountry("France");
        internalClient3.setCreationDate(xmlGregorianCalendar);
        internalClient3.setCompany("Sopra");
        internalClient3.setState("Maine et loire");
        leadTOs.add(internalClient3);
    }

    public List<ws_crm.InternalClient> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) {
        List<ws_crm.InternalClient> list = new ArrayList<ws_crm.InternalClient>();
        for(ws_crm.InternalClient client : leadTOs) {
            if((client.getAnnualRevenue() >= lowAnnualRevenue) && (client.getAnnualRevenue() <= highAnnualRevenue) && (client.getState().equals(state))) {
                list.add(client);
            }
        }
        return list;
    }

    public List<ws_crm.InternalClient> findLeadsByDate(XMLGregorianCalendar startDate, XMLGregorianCalendar endDate) {
        List<ws_crm.InternalClient> list = new ArrayList<ws_crm.InternalClient>();
        for(ws_crm.InternalClient client : leadTOs) {
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
