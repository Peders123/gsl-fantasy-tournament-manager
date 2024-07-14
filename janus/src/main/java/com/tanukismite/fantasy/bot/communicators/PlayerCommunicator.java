package com.tanukismite.fantasy.bot.communicators;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
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
        return "http://192.168.64.1:8001/api/players/";
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
            return genericDetailedGet(new URL(this.getBaseEndpoint() + playerId));
        } else {
            System.out.println("ERROR: Malformed playerId in request, expected integer.");
            return null;
        }
    }

    @Override
    public <T> boolean delete(T playerId) throws IOException {
        if (playerId instanceof Long) {
            return genericDelete(new URL(this.getBaseEndpoint() + playerId));
        } else {
            System.out.println("ERROR: Malformed playerId in request, expected long.");
            return false;
        }
    }

    @Override
    public <T> boolean put(T playerId, PostData data) throws IOException {
        if (playerId instanceof Long) {
            if (!(data instanceof PlayerSignupData)) {
                System.out.println("MALFORMED DATA SIGNUP FORMAT");
                return false;
            }
            return genericPut(new URL(this.getBaseEndpoint() + playerId), data.toMap());
        } else {
            System.out.println("ERROR: Malformed playerId in request, expected long.");
            return false;
        }
    }

}
