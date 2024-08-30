package com.tanukismite.fantasy.bot.communicators;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.tanukismite.fantasy.bot.signup.PostData;
import com.tanukismite.fantasy.bot.signup.UserSignupData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserCommunicator extends MercuryCommunicator {

    private static final Logger logger = LogManager.getLogger("ConsoleLogger");
    private static final String ID_ERROR = "Malformed userId in request: expected long, got {}";

    public UserCommunicator() {

        super();

        try {
            this.initialise();
        } catch (IOException e) {
            logger.error("Error on construction.");
        }

    }

    @Override
    protected String getBaseEndpoint() {
        return this.baseUrl + "api/users/";
    }

    @Override
    public JsonNode get() throws IOException {
        return genericGet(new URL(this.getBaseEndpoint()));
    }

    @Override
    public boolean post(PostData data) throws IOException {
        if (!(data instanceof UserSignupData)) {
            logger.error("Malformed user signup data format.");
            return false;
        }
        return genericPost(new URL(this.getBaseEndpoint()), data.toMap());
    }

    public <T> JsonNode getDetailed(T userId) throws IOException {
        if (userId instanceof Long) {
            return genericDetailedGet(new URL(this.getBaseEndpoint() + userId));
        } else {
            logger.error(ID_ERROR, userId);
            return null;
        }
    }

    @Override
    public <T> boolean delete(T userId) throws IOException {
        if (userId instanceof Long) {
            return genericDelete(new URL(this.getBaseEndpoint() + userId));
        } else {
            logger.error(ID_ERROR, userId);
            return false;
        }
    }

    @Override
    public <T> boolean put(T userId, PostData data) throws IOException {
        if (userId instanceof Long) {
            if (!(data instanceof UserSignupData)) {
                logger.error("Malformed user signup data format.");
                return false;
            }
            return genericPut(new URL(this.getBaseEndpoint() + userId), data.toMap());
        } else {
            logger.error(ID_ERROR, userId);
            return false;
        }
    }

}