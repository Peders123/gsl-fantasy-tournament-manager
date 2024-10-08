package com.tanukismite.fantasy.bot.communicators;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanukismite.fantasy.bot.HttpHandler;
import com.tanukismite.fantasy.bot.signup.PostData;

public abstract class MercuryCommunicator {

    private String username;
    private String password;
    protected String token;
    protected String baseUrl;

    protected MercuryCommunicator() {

        JsonNode configNode;
        JsonNode secretNode;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            configNode = objectMapper.readTree(new File("config.json"));
            secretNode = objectMapper.readTree(new File("secrets.json"));
        } catch (IOException e) {
            System.out.println("Could not read config files.");
            e.printStackTrace();
            return;
        }
        
        this.username = configNode.get("proc").get("username").get(System.getenv("BUILD_TYPE")).asText();
        this.password = secretNode.get("passwords").get("proc").get(System.getenv("BUILD_TYPE")).asText();
        this.baseUrl = configNode.get("endpoint").get(System.getenv("BUILD_TYPE")).asText();

    }

    public void initialise() throws IOException {

        Dictionary<String, String> headers = new Hashtable<String, String>();
        headers.put("User-Agent", "Mozilla/5.0");
        headers.put("Content-Type", "application/json");

        HttpHandler tokenGrabber = new HttpHandler(new URL(this.baseUrl + "api-authentication/"), "POST", headers);

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("username", this.username);
        inputMap.put("password", this.password);

        tokenGrabber.writeFromMap(inputMap);

        JsonNode response = tokenGrabber.readToJson();

        System.out.println(response.get("token").asText());

        this.token = response.get("token").asText();

    }

    protected HttpHandler createHttpHandler(URL url, String method, Map<String, Object> inputMap) throws IOException {

        Dictionary<String, String> headers = new Hashtable<>();
        headers.put("User-Agent", "Mozilla/5.0");
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Token " + this.token);

        HttpHandler handler = new HttpHandler(url, method, headers);
        if (inputMap != null) {
            handler.writeFromMap(inputMap);
        }
        return handler;

    }

    protected JsonNode genericGet(URL url) throws IOException {

        HttpHandler getter = createHttpHandler(url, "GET", null);

        if (getter.getResponseCode() != HttpURLConnection.HTTP_OK) {
            System.out.println("ERROR - NOT SUCCESSFUL GET");
            return getter.readToJson();
        }
        return getter.readToJson();

    }

    protected boolean genericPost(URL url, Map<String, Object> inputMap) throws IOException {

        HttpHandler poster = createHttpHandler(url, "POST", inputMap);

        System.out.println(poster.getResponseCode());

        if (poster.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
            System.out.println("ERROR - NOT SUCCESSFUL POST");
            poster.readError();
            return false;
        }
        return true;
    }

    protected JsonNode genericDetailedGet(URL url) throws IOException {

        HttpHandler getter = createHttpHandler(url, "GET", null);
        int responseCode = getter.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
            return getter.readToJson();
        } else {
            System.out.println("ERROR - NOT SUCCESSFUL GET");
            return getter.readToJson();
        }

    }

    protected boolean genericDelete(URL url) throws IOException {

        HttpHandler deleter = createHttpHandler(url, "DELETE", null);
        return deleter.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT;

    }

    protected boolean genericPut(URL url, Map<String, Object> inputMap) throws IOException {

        HttpHandler putter = createHttpHandler(url, "PUT", inputMap);
        return putter.getResponseCode() == HttpURLConnection.HTTP_CREATED;

    }

    public abstract JsonNode get() throws IOException;
    public abstract boolean post(PostData data) throws IOException;
    public abstract <T> JsonNode getDetailed(T id) throws IOException;
    public abstract <T> boolean delete(T id) throws IOException;
    public abstract <T> boolean put(T id, PostData data) throws IOException;

    protected abstract String getBaseEndpoint();

    public String getToken() {
        return this.token;
    }

}