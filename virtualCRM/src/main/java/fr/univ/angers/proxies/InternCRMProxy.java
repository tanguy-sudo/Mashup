package fr.univ.angers.proxies;

import fr.univ.angers.modele.GeographicPointTO;
import fr.univ.angers.modele.LeadTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InternCRMProxy implements Proxy {
    private static Logger logger = Logger.getLogger(String.valueOf(InternCRMProxy.class));

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
        return createLeads(response);
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
        return createLeads(response);
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

            String finalvalue= response.toString();
            return finalvalue;
        }
        catch (Exception e) {
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
                System.out.println(jsonArray.getJSONObject(i).toString());
                LeadTO leadTO = new LeadTO();
                leadTO.setFirstName(jsonArray.getJSONObject(i).getString("ns2:firstAndLastName").split(",")[0]);
                leadTO.setLastName(jsonArray.getJSONObject(i).getString("ns2:firstAndLastName").split(",")[1]);
                leadTO.setAnnualRevenue(jsonArray.getJSONObject(i).getDouble("ns2:annualRevenue"));
                leadTO.setPhone(jsonArray.getJSONObject(i).getString("ns2:phone"));
                leadTO.setStreet(jsonArray.getJSONObject(i).getString("ns2:street"));
                leadTO.setPostalCode(String.valueOf(jsonArray.getJSONObject(i).getInt("ns2:postalCode")));
                leadTO.setCity(jsonArray.getJSONObject(i).getString("ns2:city"));
                leadTO.setCountry(jsonArray.getJSONObject(i).getString("ns2:country"));

                String date = jsonArray.getJSONObject(i).getString("ns2:creationDate");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.valueOf(date.split("-")[0]),
                        Integer.valueOf(date.split("-")[1]),
                        Integer.valueOf(date.split("-")[2].replace("Z", "")));
                leadTO.setCreationDate(calendar);
                leadTO.setCompany(jsonArray.getJSONObject(i).getString("ns2:company"));
                leadTO.setState(jsonArray.getJSONObject(i).getString("ns2:state"));

                JSONObject obj = jsonArray.getJSONObject(i).getJSONObject("ns2:geographicPointTO");
                GeographicPointTO geographicPointTO = new GeographicPointTO();
                geographicPointTO.setLatitude(obj.getInt("ns2:latitude"));
                geographicPointTO.setLongitude(obj.getInt("ns2:longitude"));
                leadTO.setGeographicPointTO(geographicPointTO);

                leadTOs.add(leadTO);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return leadTOs;
    }
}
