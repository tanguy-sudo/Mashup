package fr.univ.angers;

import org.joda.time.DateTime;
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
import java.sql.Timestamp;
import java.util.Objects;
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
                    // http://localhost:8080/virtualCRM/findLeadsByDate?startDate=2022-05-10T20:00:00&endDate=2023-05-30T20:00:00
                    // http://localhost:8080/virtualCRM/findLeads?lowAnnualRevenue=34000&highAnnualRevenue=40000&state=Pays%20de%20la%20loire
                    String findLeads = execute(request);
                    if(Objects.isNull(findLeads) || findLeads.isEmpty())
                        System.out.println("Response : Any results or invalid format");
                    else
                        System.out.println("Response : " + findLeads);
                    rss();
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
        DateTime endDate = new DateTime();
        Timestamp start = new Timestamp(endDate.getMillis() - (24 * 60 * 60 * 1000));
        DateTime starDate = new DateTime(start);
        String stringStart = starDate.toString().split("\\+")[0];
        String stringEnd = endDate.toString().split("\\+")[0];

        String url = "http://localhost:8080/virtualCRM/findLeadsByDate?startDate=" + stringStart+ "&endDate=" + stringEnd;
        String response = execute(url);

        String potentialclient = "<?xml version=\"1.0\" ?>\n" +
                                 "<rss version=\"2.0\">\n" +
                                 "<channel>\n" +
                                 "  <title>Last potential clients</title>\n" +
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
                               "      <description>\n" +
                                        "\t\t\tnom : " + nom + ", \n" +
                                        "\t\t\tprenom : " + prenom + ", \n" +
                                        "\t\t\tannualRevenue : " + annualRevenue + ", \n" +
                                        "\t\t\tphone : " + phone + ", \n" +
                                        "\t\t\tstreet : " + street + ", \n" +
                                        "\t\t\tpostalCode : " + postalCode + ", \n" +
                                        "\t\t\tcity : " + city + ", \n" +
                                        "\t\t\tcountry : " + country + ", \n" +
                                        "\t\t\tcreationDate : " + creationDate + ", \n" +
                                        "\t\t\tsociete : " + societe + ", \n" +
                                        "\t\t\tstate : " + state + ", \n" +
                                        "\t\t\tlatitude : " + latitude + ", \n" +
                                        "\t\t\tlongitude : " + longitude + " \n" +
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
            logger.log(Level.WARNING, e.getMessage());
        }
    }
}
