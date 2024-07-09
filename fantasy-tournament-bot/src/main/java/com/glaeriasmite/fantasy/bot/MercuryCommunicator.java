package com.glaeriasmite.fantasy.bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MercuryCommunicator {

    private String token;

    public MercuryCommunicator(String username, String password) throws IOException {

        System.out.println("HELLO WORLD");

        URL url = new URL("http://192.168.64.1:8001/api-authentication/");

        Dictionary<String, String> headers = new Hashtable<String, String>();
        headers.put("User-Agent", "Mozilla/5.0");
        headers.put("Content-Type", "application/json");

        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("username", "Peders");
        inputMap.put("password", "Pa55we1rd");

        ObjectMapper mapper = new ObjectMapper();
        String input = mapper.writeValueAsString(inputMap);

        JsonNode response = MercuryCommunicator.HttpPost(url, headers, input);

        this.token = response.get("token").asText();

        /*try {
            URL url = new URL("http://192.168.64.1:8001/api-authentication/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestProperty("Content-Type", "application/json");

            String jsonString = new JSONObject()
                .put("username", "Peders")
                .put("password", "Pa55we1rd")
                .toString();

            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonString.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // Success
                BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = input.readLine()) != null) {
                    response.append(inputLine);
                }
                input.close();

                System.out.println(response.toString());

                ObjectMapper mapper = new ObjectMapper();
                JsonNode unwrappedResponse = mapper.readTree(response.toString());

                this.token = unwrappedResponse.get("token").textValue();

            } else { // Error
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

        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

    public static JsonNode HttpPost(URL url, Dictionary<String, String> headers, String input) throws IOException {

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");

        Enumeration<String> keys = headers.keys();
        String key;

        while(keys.hasMoreElements()) {
            key = (String) keys.nextElement();
            conn.setRequestProperty(key, headers.get(key));
        }

        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(input.getBytes());
            os.flush();
        }

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            System.out.println(response.toString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode unwrappedResponse = mapper.readTree(response.toString());

            return unwrappedResponse;

        } else {
            System.exit(1);
        }

        return null;

    }

    public static JsonNode HttpGet(URL url, Dictionary<String, String> headers) throws IOException {

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        Enumeration<String> keys = headers.keys();
        String key;

        while(keys.hasMoreElements()) {
            key = (String) keys.nextElement();
            conn.setRequestProperty(key, headers.get(key));
        }

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            System.out.println(response.toString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode unwrappedResponse = mapper.readTree(response.toString());

            return unwrappedResponse;

        } else {
            System.out.println("ERROR ON GET");
            System.exit(1);
        }

        return null;

    }

    public String getToken() {
        return this.token;
    }

}
