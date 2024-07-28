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
    protected String getBaseEndpoint() {
        return this.baseUrl + "api/players/";
    }

    @Override
    public JsonNode get() throws IOException {
        return genericGet(new URL(this.getBaseEndpoint()));
    }

    @Override
    public boolean post(PostData data) throws IOException {
        if (!(data instanceof PlayerSignupData)) {
            System.out.println("MALFORMED DATA SIGNUP FORMAT");
            return false;
        }
        return genericPost(new URL(this.getBaseEndpoint()), data.toMap());
    }

    @Override
    public <T> JsonNode getDetailed(T playerId) throws IOException {
        if (playerId instanceof Integer) {
            return genericDetailedGet(new URL(this.getBaseEndpoint() + playerId.toString()));
        } else {
            System.out.println("ERROR: Malformed playerId in request, expected integer.");
            return null;
        }
    }

    @Override
    public <T> boolean delete(T playerId) throws IOException {
        if (playerId instanceof Integer) {
            return genericDelete(new URL(this.getBaseEndpoint() + playerId.toString()));
        } else {
            System.out.println("ERROR: Malformed playerId in request, expected integer.");
            return false;
        }
    }

    @Override
    public <T> boolean put(T playerId, PostData data) throws IOException {
        if (playerId instanceof Integer) {
            if (!(data instanceof PlayerSignupData)) {
                System.out.println("MALFORMED DATA SIGNUP FORMAT");
                return false;
            }
            return genericPut(new URL(this.getBaseEndpoint() + playerId), data.toMap());
        } else {
            System.out.println("ERROR: Malformed playerId in request, expected integer.");
            return false;
        }
    }

    public JsonNode getPlayerUser(long userId) throws IOException {
        return genericDetailedGet(new URL(this.getBaseEndpoint() + "by-user/" + userId));
    }

    public boolean getPlayerUserExists(long userId) throws IOException {
        URL url = new URL(this.getBaseEndpoint() + "by-user/" + userId);
        HttpHandler getter = createHttpHandler(url, "GET", null);

        if (getter.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
            return false;
        } else if (getter.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return true;
        }
        System.out.println("ERROR");
        return false;
    }

}
