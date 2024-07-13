package com.tanukismite.fantasy.bot.communicators;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.tanukismite.fantasy.bot.HttpHandler;
import com.tanukismite.fantasy.bot.signup.UserSignupData;

public class UserCommunicator extends MercuryCommunicator {

    public UserCommunicator() {

        super();

        try {
            this.initialise();
        } catch (IOException e) {
            System.out.println("ERROR ON CONSTRUCTION");
        }

    }

    @Override
    public JsonNode get() throws IOException {

        HttpHandler getter = this.genericGet(new URL("http://192.168.64.1:8001/api/users/"));

        int responseCode = getter.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_OK) {

            System.out.println("ERROR - NOT SUCCESSFUL GET");
            getter.readToJson();

            return null;

        }

        return getter.readToJson();

    }

    @Override
    public <T> boolean post(T data) throws IOException {

        if (!(data instanceof UserSignupData)) {
            System.out.println("MALFORMED DATA SIGNUP FORMAT");
            return false;
        }

        UserSignupData userData = (UserSignupData) data;

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("user_id", Long.parseLong(userData.getId()));
        inputMap.put("discord_name", userData.getDiscord());

        HttpHandler poster = this.genericPost(new URL("http://192.168.64.1:8001/api/users/"), inputMap);

        int responseCode = poster.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_CREATED) {
            System.out.println("ERROR - NOT SUCCESSFUL POST");
            return false;
        }
        return true;

    }

    public <T> JsonNode getDetailed(T userId) throws IOException {

        URL url;

        if (userId instanceof Long) {
            url = new URL("http://192.168.64.1:8001/api/users/" + Long.toString((Long) userId));
        } else {
            System.out.println("ERROR");
            System.out.println("Malformed userId in request, expected long.");

            return null;
        }

        HttpHandler getter = this.genericGet(url);
        int responseCode = getter.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NOT_FOUND){
            JsonNode response = getter.readToJson();
            return response;
        } else {
            System.out.println("ERROR - NOT SUCCESSFUL GET");
            getter.readToJson();
            return null;
        }

    }

}
