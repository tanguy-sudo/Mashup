package fr.univ.angers.repository;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Component
public class InternalCRMRepository {
    private List<ws_crm.InternalClient> leadTOs = new ArrayList<ws_crm.InternalClient>();

    @PostConstruct
    public void initData() {

        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newDefaultInstance()
                .newXMLGregorianCalendar(2022, 10, 23, 9, 30, 0, 0, TimeZone.SHORT);

        DateTime dateTime = new DateTime();

        XMLGregorianCalendar currentDateTime = DatatypeFactory.newDefaultInstance()
                .newXMLGregorianCalendar(dateTime.getYear(),
                        dateTime.getMonthOfYear(),
                        dateTime.getDayOfMonth(),
                        dateTime.getHourOfDay(),
                        dateTime.getMinuteOfHour(),
                        dateTime.getSecondOfMinute(),
                        0,
                        TimeZone.SHORT);

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
        internalClient3.setCreationDate(currentDateTime);
        internalClient3.setCompany("Sopra");
        internalClient3.setState("Maine et loire");
        leadTOs.add(internalClient3);
    }

    public List<ws_crm.InternalClient> getLeadTOs() {
        return leadTOs;
    }
}
