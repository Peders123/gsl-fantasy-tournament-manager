package com.glaeriasmite.fantasy.bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpHandler {

    private URL url;
    private HttpURLConnection conn;

    public HttpHandler(String url, String requestMethod, Dictionary<String, String> headers) throws IOException {

        this.url = new URL(url);
        this.conn = (HttpURLConnection) this.url.openConnection();
        this.conn.setRequestMethod(requestMethod);

        Enumeration<String> keys = headers.keys();
        String key;

        while(keys.hasMoreElements()) {
            key = (String) keys.nextElement();
            conn.setRequestProperty(key, headers.get(key));
        }

    }

    public void writeFromMap(Map<String, Object> inputMap) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        String input = mapper.writeValueAsString(inputMap);

        this.conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(input.getBytes());
            os.flush();
        }
        
    }

    public int getResponseCode() throws IOException {

        return this.conn.getResponseCode();

    }

    public JsonNode readToJson() throws IOException {

        int responseCode = this.conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(this.conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode unwrappedResponse = mapper.readTree((response.toString()));

            return unwrappedResponse;

        } else {

            BufferedReader errorInput = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            String inputLine;
            StringBuilder errorResponse = new StringBuilder();

            while ((inputLine = errorInput.readLine()) != null) {
                errorResponse.append(inputLine);
            }
            errorInput.close();

            System.out.println("POST request not worked");
            System.out.println("Response Code: " + responseCode);
            System.out.println("Error Response: " + errorResponse.toString());
        }

        return null;

    }

}
