package fr.univ.angers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
                if(r.length == 2 && r[0].equals("http://localhost:8083/")) {
                    // http://localhost:8083/virtualCRM/findLeadsByDate?startDate=2022-05-10T20:00:00&endDate=2023-05-30T20:00:00
                    // http://localhost:8083/virtualCRM/findLeads?lowAnnualRevenue=34000&highAnnualRevenue=40000&state=Pays%20de%20la%20loire
                    String findLeads = execute(request);
                    if(Objects.isNull(findLeads) || findLeads.isEmpty())
                        System.out.println("Response : Any results or invalid format");
                    else
                        System.out.println("Response : " + findLeads);
                } else {
                    System.out.println("Wrong url or port");
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
            return "";
        }
        return response.body();
    }

}
