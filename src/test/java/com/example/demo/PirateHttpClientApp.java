package com.example.demo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Base64;

public class PirateHttpClientApp {
    public static void main(String[] args) throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();

        long startTime = System.currentTimeMillis();

        for(int i = 0; i < 10000; i++) {
            if (i == 123) {
                System.out.println("this password should match");
            }
            String password = "password" + i;
            String userPassword = "user:" + password;
            String userPasswordBase64 = Base64.getEncoder().encodeToString(userPassword.getBytes());
            if (i == 123 && !userPasswordBase64.equals("dXNlcjpwYXNzd29yZDEyMw==")) {
                System.out.println("unexpected");
            }
            HttpRequest request = HttpRequest.newBuilder()
                    .GET().uri(new URI("http://localhost:8080/index.html"))
                    .header("Accept", "text/html")
                    .header("Authorization", "Basic " + userPasswordBase64)
                    // .header("Connection", "keep-alive") // restricted header name!
                    .build();
            HttpResponse<Void> resp = httpClient.send(request, BodyHandlers.discarding());
            int statusCode = resp.statusCode();
            if (statusCode != 200) {
                // bad password
            } else {
                System.out.println("got http response:" + statusCode + " => password: " + password);
                break;
            }
        }

        long millis = System.currentTimeMillis() - startTime;
        System.out.println("Finished, took " + millis + " ms");
    }

}
