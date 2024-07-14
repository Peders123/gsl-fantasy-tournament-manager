package com.tanukismite.fantasy.bot.communicators;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.tanukismite.fantasy.bot.HttpHandler;
import com.tanukismite.fantasy.bot.signup.PlayerSignupData;
import com.tanukismite.fantasy.bot.signup.PostData;

public class PlayerCommunicator extends MercuryCommunicator {

    public PlayerCommunicator() {

        super();

        try {
            this.initialise();
        } catch (IOException e) {
            System.out.println("ERROR ON CONSTRUCTION");
        }

    }

    @Override
    public JsonNode get() throws IOException {

        HttpHandler getter = this.genericGet(new URL("http://192.168.64.1:8001/api/players/"));

        int responseCode = getter.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_OK) {

            System.out.println("ERROR - NOT SUCCESSFUL GET");
            getter.readToJson();

            return null;

        }

        return getter.readToJson();

    }

    @Override
    public boolean post(PostData data) throws IOException {

        if (!(data instanceof PlayerSignupData)) {
            System.out.println("MALFORMED DATA SIGNUP FORMAT");
            return false;
        }

        HttpHandler poster = this.genericPost(new URL("http://192.168.64.1:8001/api/players/"), data.toMap());

        int responseCode = poster.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_CREATED) {
            System.out.println("ERROR - NOT SUCCESSFUL POST");
            return false;
        }
        return true;

    }

    public <T> JsonNode getDetailed(T playerId) throws IOException {

        URL url;

        if (playerId instanceof Integer) {
            url = new URL("http://192.168.64.1:8001/api/players/" + Integer.toString((Integer) playerId));
        } else {
            System.out.println("ERROR");
            System.out.println("Malformed playerId in request, expected long.");

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

    @Override
    public <T> boolean delete(T playerId) throws IOException {
        
        URL url;

        if (playerId instanceof Long) {
            url = new URL("http://192.168.64.1:8001/api/players/" + Long.toString((Long) playerId));
        } else {
            System.out.println("ERROR");
            System.out.println("Malformed playerId in request, expected long.");

            return false;
        }

        HttpHandler deleter = this.genericDelete(url);
        int responseCode = deleter.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
            return true;
        }

        return false;
    }

    @Override
    public <T> boolean put(T playerId, PostData data) throws IOException {

        URL url;

        if (playerId instanceof Long) {
            url = new URL("http://192.168.64.1:8001/api/players/" + Long.toString((Long) playerId));
        } else {
            System.out.println("ERROR");
            System.out.println("Malformed playerId in request, expected long.");

            return false;
        }

        if (!(data instanceof PlayerSignupData)) {
            System.out.println("MALFORMED DATA SIGNUP FORMAT");
            return false;
        }

        HttpHandler putter = this.genericPut(url, data.toMap());

        int responseCode = putter.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_CREATED) {
            System.out.println("ERROR - NOT SUCCESSFUL PUT");
            return false;
        }
        return true;

    }

}
