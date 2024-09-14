package com.tanukismite.fantasy.bot.communicators;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tanukismite.fantasy.bot.HttpHandler;
import com.tanukismite.fantasy.bot.signup.PlayerSignupData;
import com.tanukismite.fantasy.bot.signup.PostData;


public class PlayerCommunicator extends MercuryCommunicator {

    private static final Logger logger = LogManager.getLogger("ConsoleLogger");
    private static final String ID_ERROR = "Malformed playerId in request: expected integer, got {}";

    public PlayerCommunicator() {

        super();

        try {
            this.initialise();
        } catch (IOException e) {
            logger.error("Error on construction.");
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
            logger.error("Malformed player signup data format.");
            return false;
        }
        return genericPost(new URL(this.getBaseEndpoint()), data.toMap());
    }

    @Override
    public <T> JsonNode getDetailed(T playerId) throws IOException {
        if (playerId instanceof Integer) {
            return genericDetailedGet(new URL(this.getBaseEndpoint() + playerId.toString()));
        } else {
            logger.error(ID_ERROR, playerId);
            return null;
        }
    }

    @Override
    public <T> boolean delete(T playerId) throws IOException {
        if (playerId instanceof Integer) {
            return genericDelete(new URL(this.getBaseEndpoint() + playerId.toString()));
        } else {
            logger.error(ID_ERROR, playerId);
            return false;
        }
    }

    @Override
    public <T> boolean put(T playerId, PostData data) throws IOException {
        if (playerId instanceof Integer) {
            if (!(data instanceof PlayerSignupData)) {
                logger.error("Malformed player signup data format.");
                return false;
            }
            return genericPut(new URL(this.getBaseEndpoint() + playerId), data.toMap());
        } else {
            logger.error(ID_ERROR, playerId);
            return false;
        }
    }

    public JsonNode getPlayerUser(long userId) throws IOException {
        return genericDetailedGet(new URL(this.getBaseEndpoint() + "by-user/" + userId));
    }

    public boolean getPlayerUserExists(long userId) throws IOException {
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
