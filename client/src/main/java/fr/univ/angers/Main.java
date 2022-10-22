package fr.univ.angers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static Logger logger = Logger.getLogger(String.valueOf(Main.class));

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean stop = false;
        String request;
        System.out.print("Request : " );

        while(!stop) {
            request = scanner.nextLine();
            if(request.equals("stop")) {
                break;
            } else {
                String[] r = request.split("virtualCRM");
                if(r.length == 2 && r[0].equals("http://localhost:8080/")) {
                    // http://localhost:8080/virtualCRM/findLeadsByDate?startDate=2021-05-10&endDate=2021-05-30
                    // http://localhost:8080/virtualCRM/findLeads?lowAnnualRevenue=34000&highAnnualRevenue=40000&state=Pays%20de%20la%20loire
                    rss();
                    String findLeads = execute(request);
                    System.out.println("Response : " + findLeads);
                } else {
                    System.out.println("Wrong url");
                }
                System.out.print("Request : " );
            }
        }
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

    private static void rss() {
        String url = "http://localhost:8080/virtualCRM/findLeadsByDate?startDate=2021-05-10&endDate=2021-05-30";
        String response = execute(url);

        String potentialclient = "<?xml version=\"1.0\" ?>\n" +
                                 "<rss version=\"2.0\">\n" +
                                 "<channel>\n" +
                                 "  <title>Mashup</title>\n" +
                                 "  <link>http://www.acme.com/</link>\n" +
                                 "  <description>potential Clients</description>\n";

        JSONArray jsonArray = new JSONArray(response);
        for (int i = 0 ; i < jsonArray.length() ; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String nom = jsonObject.getString("lastName");
            String prenom = jsonObject.getString("firstName");
            Double annualRevenue = jsonObject.getDouble("annualRevenue");
            String phone = jsonObject.getString("phone");
            String street = jsonObject.getString("street");
            String postalCode = jsonObject.getString("postalCode");
            String city = jsonObject.getString("city");
            String country = jsonObject.getString("country");
            String creationDate = jsonObject.getString("creationDate") ;
            String societe = jsonObject.getString("company");
            String state = jsonObject.getString("state");
            JSONObject geographicPointTO = jsonObject.getJSONObject("geographicPointTO");
            Double latitude = geographicPointTO.getDouble("latitude");
            Double longitude = geographicPointTO.getDouble("longitude");
            potentialclient += "   <item>\n" +
                               "      <title>" + nom + " " + prenom + " - " + societe + "</title>\n" +
                               "      <link>http://www.acme.com/" + nom + "</link> \n" +
                               "      <description>" +
                                        "nom : " + nom + ", " +
                                        "prenom : " + prenom + ", " +
                                        "annualRevenue : " + annualRevenue + ", " +
                                        "phone : " + phone + ", " +
                                        "street : " + street + ", " +
                                        "postalCode : " + postalCode + ", " +
                                        "city : " + city + ", " +
                                        "country : " + country + ", " +
                                        "creationDate : " + creationDate + ", " +
                                        "societe : " + societe + ", " +
                                        "state : " + state + ", " +
                                        "latitude : " + latitude + ", " +
                                        "longitude : " + longitude + ", " +
                               "      </description>\n" +
                               "      <pubDate>" + creationDate + "</pubDate>\n" +
                               "   </item>\n";
        }

        potentialclient += "</channel>\n" +
                           "</rss>";

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(potentialclient)));

            FileWriter writer = new FileWriter(new File("potentialClient.rss"));
            StreamResult result = new StreamResult(writer);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.transform(source, result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
