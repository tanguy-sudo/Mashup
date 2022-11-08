package fr.univ.angers.proxies;

import fr.univ.angers.modele.GeographicPointTO;
import fr.univ.angers.modele.LeadTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import javax.xml.datatype.XMLGregorianCalendar;
import fr.univ.angers.utility.CalendarConverter;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InternCRMProxy implements Proxy {
    private static final Logger logger = Logger.getLogger(String.valueOf(InternCRMProxy.class));

    @Override
    public List<LeadTO> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) {
        /* place your xml request from soap ui below with necessary changes in parameters */
        String xml="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws-crm\">\r\n" +
                "   <soapenv:Header/>\r\n" +
                "   <soapenv:Body>\r\n" +
                "      <ws:findLeadsRequest>\r\n" +
                "          <ws:highAnnualRevenue>" + highAnnualRevenue + "</ws:highAnnualRevenue>\r\n" +
                "          <ws:lowAnnualRevenue>" + lowAnnualRevenue + "</ws:lowAnnualRevenue>\r\n" +
                "          <ws:state>" + state +"</ws:state>\r\n" +
                "      </ws:findLeadsRequest>\r\n" +
                "   </soapenv:Body>\r\n" +
                "</soapenv:Envelope>";

        String response = callSoapService(xml);
        if(Objects.nonNull(response))
            return createLeads(response);
        else
            return new ArrayList<LeadTO>();
    }

    @Override
    public List<LeadTO> findLeadsByDate(XMLGregorianCalendar startDate, XMLGregorianCalendar endDate) {
        /* place your xml request from soap ui below with necessary changes in parameters */
        String xml="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws-crm\">\r\n" +
                "   <soapenv:Header/>\r\n" +
                "   <soapenv:Body>\r\n" +
                "      <ws:findLeadsByDateRequest>\r\n" +
                "          <ws:startDate>" + startDate.toString() + "</ws:startDate>\r\n" +
                "          <ws:endDate>" + endDate.toString() + "</ws:endDate>\r\n" +
                "      </ws:findLeadsByDateRequest>\r\n" +
                "   </soapenv:Body>\r\n" +
                "</soapenv:Envelope>";

        String response = callSoapService(xml);
        if(Objects.nonNull(response))
            return createLeads(response);
        else
            return new ArrayList<LeadTO>();
    }

    private static String callSoapService(String soapRequest) {
        try {
            String url = "http://localhost:8081/ws";
            URL obj = new URL(url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type","text/xml; charset=utf-8");
            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(soapRequest);
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        }
        catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }
    }

    private static List<LeadTO> createLeads(String response) {
        List<LeadTO> leadTOs = new ArrayList<>();
        try {
            System.out.println(response);
            JSONObject json = XML.toJSONObject(response);
            JSONObject jsonObject = json
                    .getJSONObject("SOAP-ENV:Envelope")
                    .getJSONObject("SOAP-ENV:Body")
                    .getJSONObject("ns2:findLeadsResponse");

            JSONArray jsonArray = new JSONArray();
            try {
                jsonArray = jsonObject.getJSONArray("ns2:internalClient");
            }catch (Exception e) {
                jsonArray = new JSONArray();
                if(jsonObject.isNull("ns2:internalClient")){
                    logger.log(Level.WARNING, "Sorry, no results");
                } else {
                    jsonArray.put(jsonObject.getJSONObject("ns2:internalClient"));
                }
            }
            for(int i = 0 ; i < jsonArray.length() ; i++) {
                LeadTO leadTO = new LeadTO();
                leadTO.setFirstName(jsonArray.getJSONObject(i).getString("ns2:firstAndLastName").split(",")[0]);
                leadTO.setLastName(jsonArray.getJSONObject(i).getString("ns2:firstAndLastName").split(",")[1]);
                leadTO.setAnnualRevenue(jsonArray.getJSONObject(i).getDouble("ns2:annualRevenue"));
                leadTO.setPhone(jsonArray.getJSONObject(i).getString("ns2:phone"));
                leadTO.setStreet(jsonArray.getJSONObject(i).getString("ns2:street"));
                leadTO.setPostalCode(String.valueOf(jsonArray.getJSONObject(i).getInt("ns2:postalCode")));
                leadTO.setCity(jsonArray.getJSONObject(i).getString("ns2:city"));
                leadTO.setCountry(jsonArray.getJSONObject(i).getString("ns2:country"));

                String creationDate = jsonArray.getJSONObject(i).getString("ns2:creationDate");
                // Exemple : 2021-06-15T20:00:00.324Z
                String date = creationDate.split("T")[0];
                String time = creationDate.split("T")[1];

                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.parseInt(date.split("-")[0]),
                             CalendarConverter.getMonth(Integer.parseInt(date.split("-")[1])),
                             Integer.parseInt(date.split("-")[2]),
                             Integer.parseInt(time.split(":")[0]),
                             Integer.parseInt(time.split(":")[1]),
                             Integer.parseInt(time.split(":")[2].split("\\.")[0]));
                leadTO.setCreationDate(calendar);

                leadTO.setCompany(jsonArray.getJSONObject(i).getString("ns2:company"));
                leadTO.setState(jsonArray.getJSONObject(i).getString("ns2:state"));
                GeographicPointTO geographicPointTO = new GeographicPointTO();
                leadTO.setGeographicPointTO(geographicPointTO);

                leadTOs.add(leadTO);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return leadTOs;
    }
}
