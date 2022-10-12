package fr.univ.angers.proxies;

import fr.univ.angers.modele.GeographicPointTO;
import fr.univ.angers.modele.LeadTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.format.SignStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InternCRMProxy implements Proxy {
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

        List<LeadTO> leadTOs = new ArrayList<LeadTO>();
        try {
            String response = callSoapService(xml);
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
                jsonArray.put(jsonObject.getJSONObject("ns2:internalClient"));
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
                //leadTO.setCreationDate(jsonObject.getString("ns2:creationDate"));
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

    @Override
    public List<LeadTO> findLeadsByDate(Calendar startDate, Calendar endDate) {
        return null;
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
}
