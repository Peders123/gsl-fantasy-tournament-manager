package com.tanukismite.fantasy.bot.communicators;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.tanukismite.fantasy.bot.HttpHandler;
import com.tanukismite.fantasy.bot.signup.CaptainSignupData;
import com.tanukismite.fantasy.bot.signup.PostData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CaptainCommunicator extends MercuryCommunicator {

    private static final Logger logger = LogManager.getLogger("ConsoleLogger");
    private static final String ID_ERROR = "Malformed captainId in request: expected integer, got {}";

    public CaptainCommunicator() {

        super();

        try {
            this.initialise();
        } catch (IOException error) {
            logger.error("Error on construction.", error);
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
        if (!(data instanceof CaptainSignupData)) {
            logger.error("Malformed captain signup data format.");
            return false;
        }
        return genericPost(new URL(this.getBaseEndpoint()), data.toMap());
    }

    @Override
    public <T> JsonNode getDetailed(T captainId) throws IOException {
        if (captainId instanceof Integer) {
            return genericDetailedGet(new URL(this.getBaseEndpoint() + captainId));
        } else {
            logger.error(ID_ERROR, captainId.getClass());
            return null;
        }
    }

    @Override
    public <T> boolean delete(T captainId) throws IOException {
        if (captainId instanceof Integer) {
            return genericDelete(new URL(this.getBaseEndpoint() + captainId));
        } else {
            logger.error(ID_ERROR, captainId.getClass());
            return false;
        }
    }

    @Override
    public <T> boolean put(T captainId, PostData data) throws IOException {
        if (captainId instanceof Integer) {
            if (!(data instanceof CaptainSignupData)) {
                logger.error("Malformed captain signup data format.");
                return false;
            }
            return genericPut(new URL(this.getBaseEndpoint() + captainId), data.toMap());
        } else {
            logger.error(ID_ERROR, captainId.getClass());
            return false;
        }
    }

    public JsonNode getCaptainUser(long userId) throws IOException {
        return genericDetailedGet(new URL(this.getBaseEndpoint() + "by-user/" + userId));
    }

    public boolean getCaptainUserExists(long userId) throws IOException {
        URL url = new URL(this.getBaseEndpoint() + "by-user/" + userId);
        HttpHandler getter = createHttpHandler(url, "GET", null);
        int responseCode = getter.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
            return false;
        } else if (responseCode == HttpURLConnection.HTTP_OK) {
            return true;
        }
        logger.error("Got unexpected response code: {}", responseCode);
        return false;
    }

}
