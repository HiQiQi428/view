package org.luncert.view.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Request {

    public static String get(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        HttpURLConnection httpConnection = (HttpURLConnection)connection;

        httpConnection.setRequestProperty("Accept-Charset", "utf-8");
        httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        if (httpConnection.getResponseCode() >= 300) {
            throw new RuntimeException("HTTP reponse code: " + httpConnection.getResponseCode());
        }

        StringBuilder rep = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()))) {
            String line = null;
            while ((line = reader.readLine()) != null) rep.append(line);
        }

        return rep.toString();
    }
    
}