package com.tanukismite.fantasy.bot.communicators;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.tanukismite.fantasy.bot.HttpHandler;
import com.tanukismite.fantasy.bot.signup.CaptainSignupData;
import com.tanukismite.fantasy.bot.signup.PostData;

public class CaptainCommunicator extends MercuryCommunicator {

    public CaptainCommunicator() {

        super();

        try {
            this.initialise();
        } catch (IOException e) {
            System.out.println("ERROR ON CONSTRUCTION");
            e.printStackTrace();
        }

    }

    @Override
    protected String getBaseEndpoint() {
        return this.baseUrl + "api/captains/";
    }

    @Override
    public JsonNode get() throws IOException {
        return genericGet(new URL(this.getBaseEndpoint()));
    }

    @Override
    public boolean post(PostData data) throws IOException {
        System.out.println(data.toMap());
        if (!(data instanceof CaptainSignupData)) {
            System.out.println("MALFORMED DATA SIGNUP FORMAT");
            return false;
        }
        return genericPost(new URL(this.getBaseEndpoint()), data.toMap());
    }

    @Override
    public <T> JsonNode getDetailed(T captainId) throws IOException {
        if (captainId instanceof Integer) {
            return genericDetailedGet(new URL(this.getBaseEndpoint() + captainId));
        } else {
            System.out.println("ERROR: Malformed captainId in request, expected integer.");
            return null;
        }
    }

    @Override
    public <T> boolean delete(T captainId) throws IOException {
        if (captainId instanceof Long) {
            return genericDelete(new URL(this.getBaseEndpoint() + captainId));
        } else {
            System.out.println("ERROR: Malformed captainId in request, expected long.");
            return false;
        }
    }

    @Override
    public <T> boolean put(T captainId, PostData data) throws IOException {
        if (captainId instanceof Long) {
            if (!(data instanceof CaptainSignupData)) {
                System.out.println("MALFORMED DATA SIGNUP FORMAT");
                return false;
            }
            return genericPut(new URL(this.getBaseEndpoint() + captainId), data.toMap());
        } else {
            System.out.println("ERROR: Malformed captainId in request, expected long.");
            return false;
        }
    }

    public JsonNode getCaptainUser(long userId) throws IOException {
        return genericDetailedGet(new URL(this.getBaseEndpoint() + "by-user/" + userId));
    }

    public boolean getCaptainUserExists(long userId) throws IOException {
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
