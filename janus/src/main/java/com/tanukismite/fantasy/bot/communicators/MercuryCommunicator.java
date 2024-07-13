package com.tanukismite.fantasy.bot.communicators;

import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.tanukismite.fantasy.bot.HttpHandler;

public abstract class MercuryCommunicator {

    private String username;
    private String password;
    protected String token;

    protected MercuryCommunicator() {
        
        this.username = "Peders";
        this.password = "Pa55we1rd";

    }

    public void initialise() throws IOException {

        Dictionary<String, String> headers = new Hashtable<String, String>();
        headers.put("User-Agent", "Mozilla/5.0");
        headers.put("Content-Type", "application/json");

        HttpHandler tokenGrabber = new HttpHandler(new URL("http://192.168.64.1:8001/api-authentication/"), "POST", headers);

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("username", this.username);
        inputMap.put("password", this.password);

        tokenGrabber.writeFromMap(inputMap);

        JsonNode response = tokenGrabber.readToJson();

        System.out.println(response.get("token").asText());

        this.token = response.get("token").asText();

    }

    protected HttpHandler genericGet(URL url) throws IOException {

        Dictionary<String, String> headers = new Hashtable<String, String>();
        headers.put("User-Agent", "Mozilla/5.0");
        headers.put("Authorization", "Token " + this.token);

        HttpHandler getter = new HttpHandler(url, "GET", headers);

        return getter;

    }

    protected HttpHandler genericPost(URL url, Map<String, Object> inputMap) throws IOException {

        Dictionary<String, String> headers = new Hashtable<String, String>();
        headers.put("User-Agent", "Mozilla/5.0");
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Token " + this.token);

        HttpHandler poster = new HttpHandler(url, "POST", headers);

        poster.writeFromMap(inputMap);

        return poster;

    }

    public abstract JsonNode get() throws IOException;
    public abstract <T> boolean post(T data) throws IOException;
    public abstract <T> JsonNode getDetailed(T id) throws IOException;

    /* public boolean postPlayer(Map<String, Object> inputMap) throws IOException {

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
    } */

    public String getToken() {
        return this.token;
    }

}
