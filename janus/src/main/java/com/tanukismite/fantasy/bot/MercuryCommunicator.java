package com.tanukismite.fantasy.bot;

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

        Dictionary<String, String> headers = new Hashtable<String, String>();
        headers.put("User-Agent", "Mozilla/5.0");
        headers.put("Content-Type", "application/json");

        HttpHandler tokenGrabber = new HttpHandler("http://192.168.64.1:8001/api-authentication/", "POST", headers);

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("username", username);
        inputMap.put("password", password);

        tokenGrabber.writeFromMap(inputMap);

        JsonNode response = tokenGrabber.readToJson();

        System.out.println(response.get("token").asText());

        this.token = response.get("token").asText();

        /* System.out.println("HELLO WORLD");

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

        this.token = response.get("token").asText(); */

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

    public JsonNode getUsers() throws IOException {

        Dictionary<String, String> headers = new Hashtable<String, String>();
        headers.put("User-Agent", "Mozilla/5.0");
        headers.put("Authorization", "Token " + this.token);

        HttpHandler userGetter = new HttpHandler("http://192.168.64.1:8001/api/users/", "GET", headers);

        int responseCode = userGetter.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_OK) {

            System.out.println("ERROR - NOT SUCCESSFUL GET");

            userGetter.readToJson();

            return null;

        }

        JsonNode response = userGetter.readToJson();

        return response;

    }

    public JsonNode getDetailedUser(long userId) throws IOException {

        String url = "http://192.168.64.1:8001/api/users/" + Long.toString(userId);

        Dictionary<String, String> headers = new Hashtable<String, String>();
        headers.put("User-Agent", "Mozilla/5.0");
        headers.put("Authorization", "Token " + this.token);

        HttpHandler userGetter = new HttpHandler(url, "GET", headers);

        int responseCode = userGetter.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NOT_FOUND){
            JsonNode response = userGetter.readToJson();
            return response;
        } else {
            System.out.println("ERROR - NOT SUCCESSFUL GET");
            userGetter.readToJson();
            return null;
        }

    }

    public boolean postUser(long user_id, String discord_name) throws IOException {

        Dictionary<String, String> headers = new Hashtable<String, String>();
        headers.put("User-Agent", "Mozilla/5.0");
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Token " + this.token);

        HttpHandler userPoster = new HttpHandler("http://192.168.64.1:8001/api/users/", "POST", headers);

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("user_id", user_id);
        inputMap.put("discord_name", discord_name);

        userPoster.writeFromMap(inputMap);

        int responseCode = userPoster.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_CREATED) {
            System.out.println("ERROR - NOT SUCCESSFUL POST");
            return false;
        }
        return true;
    }

    public boolean postPlayer(Map<String, Object> inputMap) throws IOException {

        Dictionary<String, String> headers = new Hashtable<String, String>();
        headers.put("User-Agent", "Mozilla/5.0");
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Token " + this.token);

        HttpHandler playerPoster = new HttpHandler("http://192.168.64.1:8001/api/players/", "POST", headers);

        playerPoster.writeFromMap(inputMap);

        int responseCode = playerPoster.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_CREATED) {
            System.out.println("ERROR - NOT SUCCESSFUL POST");
            playerPoster.readToJson();
            return false;
        }
        return true;
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