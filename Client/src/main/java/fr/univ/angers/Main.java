package fr.univ.angers;

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
        while(!stop) {
            request = scanner.nextLine();
            if(request.equals("stop")) {
                break;
            }
            // http://localhost:8080/virtualCRM/findLeadsByDate?startDate=2021-05-10&endDate=2021-05-30
            // http://localhost:8080/virtualCRM/findLeads?lowAnnualRevenue=34000&highAnnualRevenue=40000&state=Pays%20de%20la%20loire
            String findLeads = execute(request);
            System.out.println(findLeads);
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
}
