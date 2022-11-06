package fr.univ.angers.view;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Item;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class RssFeedView extends AbstractRssFeedView {

    private static Logger logger = Logger.getLogger(String.valueOf(RssFeedView.class));

    @Override
    protected void buildFeedMetadata(Map<String, Object> model, Channel feed, HttpServletRequest request) {
            feed.setTitle("Last potential clients");
            feed.setDescription("potential Clients");
            feed.setLink("http://www.acme.com/");
    }

    @Override
    protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DateTime endDate = DateTime.now();
        Timestamp start = new Timestamp(endDate.getMillis() - (24 * 60 * 60 * 1000));
        DateTime starDate = new DateTime(start);
        String stringStart = starDate.toString().split("\\+")[0];
        String stringEnd = endDate.toString().split("\\+")[0];

        //String url = "http://localhost:8083/virtualCRM/findLeadsByDate?startDate=" + stringStart+ "&endDate=" + stringEnd;
        // For test
        String url = "http://localhost:8083/virtualCRM/findLeadsByDate?startDate=2022-05-10T20:00:00&endDate=2023-05-30T20:00:00";
        String res = execute(url);

        List<Item> items = new ArrayList<Item>();

        if(Objects.isNull(res) || res.isEmpty()) {
            System.out.println("Response : Any results or invalid format");
        } else {
            JSONArray jsonArray = new JSONArray(res);

            for (int i = 0 ; i < jsonArray.length() ; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String nom = jsonObject.getString("lastName");
                String prenom = jsonObject.getString("firstName");
                Double annualRevenue = jsonObject.getDouble("annualRevenue");
                String phone = jsonObject.getString("phone");
                String street = jsonObject.getString("street");
                String postalCode = jsonObject.getString("postalCode");
                String city = jsonObject.getString("city");
                String country = jsonObject.getString("country");
                String creationDate = jsonObject.getString("creationDate");
                String societe = jsonObject.getString("company");
                String state = jsonObject.getString("state");
                JSONObject geographicPointTO = jsonObject.getJSONObject("geographicPointTO");
                Double latitude = geographicPointTO.getDouble("latitude");
                Double longitude = geographicPointTO.getDouble("longitude");

                Item entryOne = new Item();
                entryOne.setTitle(nom + " " + prenom + " - " + societe);
                entryOne.setLink("http://www.acme.com/" + nom);
                Description description = new Description();
                description.setValue(
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
                                "\t\t\tlongitude : " + longitude);
                entryOne.setDescription(description);

                String date = creationDate.split("T")[0];
                String time = creationDate.split("T")[1];
                MutableDateTime mutableDateTime = new MutableDateTime(DateTimeZone.UTC);
                mutableDateTime.setDateTime(Integer.parseInt(date.split("-")[0]),
                        Integer.parseInt(date.split("-")[1]),
                        Integer.parseInt(date.split("-")[2]),
                        Integer.parseInt(time.split(":")[0]),
                        Integer.parseInt(time.split(":")[1]),
                        Integer.parseInt(time.split(":")[2].split("\\.")[0]),
                        0);
                Date pubDate = new Date(mutableDateTime.getMillis() + 2 * 60 * 60 * 1000);
                entryOne.setPubDate(pubDate);

                items.add(entryOne);
            }
        }

        return items;
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

    private static int getMonth(int month) {
        switch (month) {
            case 1 :
                return Calendar.JANUARY;
            case 2 :
                return Calendar.FEBRUARY;
            case 3 :
                return Calendar.MARCH;
            case 4 :
                return Calendar.APRIL;
            case 5 :
                return Calendar.MAY;
            case 6 :
                return Calendar.JUNE;
            case 7 :
                return Calendar.JULY;
            case 8 :
                return Calendar.AUGUST;
            case 9 :
                return Calendar.SEPTEMBER;
            case 10 :
                return Calendar.OCTOBER;
            case 11 :
                return Calendar.NOVEMBER;
            case 12 :
                return Calendar.DECEMBER;
            default :
                return 13;
        }
    }

}
