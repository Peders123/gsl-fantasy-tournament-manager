package com.tanukismite.fantasy.bot.communicators;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.tanukismite.fantasy.bot.signup.PostData;
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
    protected String getBaseEndpoint() {
        return this.baseUrl + "api/users/";
    }

    @Override
    public JsonNode get() throws IOException {
        return genericGet(new URL(this.getBaseEndpoint()));
    }

    @Override
    public boolean post(PostData data) throws IOException {
        System.out.println(data.getClass());
        if (!(data instanceof UserSignupData)) {
            System.out.println("MALFORMED DATA SIGNUP FORMAT");
            return false;
        }
        return genericPost(new URL(this.getBaseEndpoint()), data.toMap());
    }

    public <T> JsonNode getDetailed(T userId) throws IOException {
        if (userId instanceof Long) {
            return genericDetailedGet(new URL(this.getBaseEndpoint() + userId));
        } else {
            System.out.println("ERROR: Malformed userId in request, expected long.");
            return null;
        }
    }

    @Override
    public <T> boolean delete(T userId) throws IOException {
        if (userId instanceof Long) {
            return genericDelete(new URL(this.getBaseEndpoint() + userId));
        } else {
            System.out.println("ERROR: Malformed userId in request, expected long.");
            return false;
        }
    }

    @Override
    public <T> boolean put(T userId, PostData data) throws IOException {
        if (userId instanceof Long) {
            if (!(data instanceof UserSignupData)) {
                System.out.println("MALFORMED DATA SIGNUP FORMAT");
                return false;
            }
            return genericPut(new URL(this.getBaseEndpoint() + userId), data.toMap());
        } else {
            System.out.println("ERROR: Malformed userId in request, expected long.");
            return false;
        }
    }

}