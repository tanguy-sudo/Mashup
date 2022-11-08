package fr.univ.angers.proxies;

import fr.univ.angers.modele.GeographicPointTO;
import fr.univ.angers.modele.LeadTO;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Calendar;
import fr.univ.angers.utility.CalendarConverter;

public class SalesForceProxy implements Proxy {

    private static final Logger logger = Logger.getLogger(String.valueOf(SalesForceProxy.class));
    private static final String USERNAME = "grenong07crm@mail.com";
    private static final String PASSWORD = "Password49";
    private static final String URL = "https://login.salesforce.com/services/oauth2/token";
    private static final String QUERY_URL = "https://ggcrm2-dev-ed.develop.my.salesforce.com/services/data/v56.0/query";
    private static final String CLIENT_ID = "3MVG9QV5z8XnLDvxLmZ3oxWSJzPouZ2ripP2QdWcDWDWu.tefYJz38l.7OMlNI1HPS9Q._zmrmIUWZvQKgAh.";
    private static final String CLIENT_SECRET = "EBF3A82289F4483CA0EEEB7E40ACC5869A6FD84D1C5907D6FE7BD077363156B0";
    private static final String QUERY_DATAS = "FirstName, LastName, AnnualRevenue, Phone, Street, City, Country, CreatedDate, Company, State, PostalCode";

    @Override
    public List<LeadTO> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) {
        // FindLeadsByRevenue
        String query = "Select " + QUERY_DATAS + " from Lead where AnnualRevenue > " + new BigDecimal(lowAnnualRevenue).toPlainString() 
            + " AND AnnualRevenue < " + new BigDecimal(highAnnualRevenue).toPlainString() + " AND State = '" + state + "'";
        return createLeads(queryLeads(getToken(), query));
    }
    /*

            ENCODE >=

    */

    @Override
    public List<LeadTO> findLeadsByDate(XMLGregorianCalendar startDate, XMLGregorianCalendar endDate) {
        DateTime start = new DateTime(startDate.getYear(), startDate.getMonth(), startDate.getDay(), startDate.getHour(), startDate.getMinute());
        DateTime end = new DateTime(endDate.getYear(), endDate.getMonth(), endDate.getDay(), endDate.getHour(), endDate.getMinute());
        String query = "Select " + QUERY_DATAS + " from Lead where CreatedDate >= " + start.toString() + " AND CreatedDate <= " + end;
        return createLeads(queryLeads(getToken(), query));
    }

    private static String callSalesforceApi() {
        System.out.println("---------------------------------------------------- Starting Salesforce ----------------------------------------------------");
        HttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(URL);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");

        List<NameValuePair> logins = new ArrayList<>();

        logins.add(new BasicNameValuePair("grant_type", "password"));
        logins.add(new BasicNameValuePair("client_id", CLIENT_ID));
        logins.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
        logins.add(new BasicNameValuePair("username", USERNAME));
        logins.add(new BasicNameValuePair("password", PASSWORD));

        try{
            httpPost.setEntity(new UrlEncodedFormEntity(logins));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            String responseEntity = EntityUtils.toString(httpResponse.getEntity());
            JSONParser jsonParser = new JSONParser();
            JSONObject accessToken = (JSONObject) jsonParser.parse(responseEntity);
            System.out.println("Access token : " + accessToken);
            return accessToken.toString();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static String queryLeads(String accessToken, String query){
        HttpClient httpClient = HttpClients.createDefault();

        HttpGet httpRequest = new HttpGet(QUERY_URL);
        httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpRequest.addHeader("Authorization", "Bearer " + accessToken);
        httpRequest.addHeader("Accept", "application/json");

        URIBuilder uriBuilder = new URIBuilder(httpRequest.getURI());
        uriBuilder.addParameter("q", query);

        try{
            System.out.println(uriBuilder.build());
            httpRequest.setURI(uriBuilder.build());

            HttpResponse httpResponse = httpClient.execute(httpRequest);

            String responseEntity = EntityUtils.toString(httpResponse.getEntity());
            JSONParser jsonParser = new JSONParser();
            JSONObject queryResult = (JSONObject) jsonParser.parse(responseEntity);
            return queryResult.toString();

        } catch (IOException | URISyntaxException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getToken(){
        String accessToken = callSalesforceApi();
        return accessToken.split("\"")[3];
    }

    private static List<LeadTO> createLeads(String response) {
        List<LeadTO> leadTOs = new ArrayList<>();
        try {
            System.out.println(response);
            org.json.JSONObject jsonObject = new org.json.JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("records");

            for(int i = 0 ; i < jsonArray.length() ; i++) {
                LeadTO leadTO = new LeadTO();
                leadTO.setFirstName(jsonArray.getJSONObject(i).getString("FirstName"));
                leadTO.setLastName(jsonArray.getJSONObject(i).getString("LastName"));
                if(jsonArray.getJSONObject(i).isNull("AnnualRevenue")) {
                    break;
                }
                else {
                    BigDecimal revenue = jsonArray.getJSONObject(i).getBigDecimal("AnnualRevenue");
                    leadTO.setAnnualRevenue(revenue.doubleValue());
                }
                if(jsonArray.getJSONObject(i).isNull("Phone")) {
                    break;
                }
                else leadTO.setPhone(jsonArray.getJSONObject(i).getString("Phone"));
                if(jsonArray.getJSONObject(i).isNull("Street")) {
                    break;
                }
                else leadTO.setStreet(jsonArray.getJSONObject(i).getString("Street"));
                if(jsonArray.getJSONObject(i).isNull("PostalCode")) {
                    break;
                }
                else leadTO.setPostalCode(String.valueOf(jsonArray.getJSONObject(i).getInt("PostalCode")));
                if(jsonArray.getJSONObject(i).isNull("City")) {
                    break;
                }
                else leadTO.setCity(jsonArray.getJSONObject(i).getString("City"));
                if(jsonArray.getJSONObject(i).isNull("Country")) {
                    break;
                }
                else leadTO.setCountry(jsonArray.getJSONObject(i).getString("Country"));

                if(jsonArray.getJSONObject(i).isNull("CreatedDate")) {
                    break;
                }
                else {
                    String creationDate = jsonArray.getJSONObject(i).getString("CreatedDate");
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
                }


                if(jsonArray.getJSONObject(i).isNull("Company")) {
                    break;
                }
                else leadTO.setCompany(jsonArray.getJSONObject(i).getString("Company"));
                if(jsonArray.getJSONObject(i).isNull("State")) {
                    leadTO.setState("");
                }
                else leadTO.setState(jsonArray.getJSONObject(i).getString("State"));
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
