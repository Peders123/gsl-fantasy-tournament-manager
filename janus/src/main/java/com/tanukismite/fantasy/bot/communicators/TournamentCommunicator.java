package com.tanukismite.fantasy.bot.communicators;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.tanukismite.fantasy.bot.signup.PostData;
import com.tanukismite.fantasy.bot.signup.TournamentData;

public class TournamentCommunicator extends MercuryCommunicator {

    public TournamentCommunicator() {

        super();

        try {
            this.initialise();
        } catch (IOException e) {
            System.out.println("ERROR ON CONSTRUCTION");
        }

    }

    @Override
    protected String getBaseEndpoint() {
        return "http://192.168.64.1:8001/api/tournaments/";
    }

    @Override
    public JsonNode get() throws IOException {
        return genericGet(new URL(this.getBaseEndpoint()));
    }

    @Override
    public boolean post(PostData data) throws IOException {
        System.out.println(data.toMap());
        if (!(data instanceof TournamentData)) {
            System.out.println("MALFORMED DATA SIGNUP FORMAT");
            return false;
        }
        return genericPost(new URL(this.getBaseEndpoint()), data.toMap());
    }

    @Override
    public <T> JsonNode getDetailed(T tournamentId) throws IOException {
        if (tournamentId instanceof Integer) {
            return genericDetailedGet(new URL(this.getBaseEndpoint() + tournamentId));
        } else {
            System.out.println("ERROR: Malformed tournamentId in request, expected integer.");
            return null;
        }
    }

    @Override
    public <T> boolean delete(T tournamentId) throws IOException {
        if (tournamentId instanceof Long) {
            return genericDelete(new URL(this.getBaseEndpoint() + tournamentId));
        } else {
            System.out.println("ERROR: Malformed tournamentId in request, expected long.");
            return false;
        }
    }

    @Override
    public <T> boolean put(T tournamentId, PostData data) throws IOException {
        if (tournamentId instanceof Long) {
            if (!(data instanceof TournamentData)) {
                System.out.println("MALFORMED DATA SIGNUP FORMAT");
                return false;
            }
            return genericPut(new URL(this.getBaseEndpoint() + tournamentId), data.toMap());
        } else {
            System.out.println("ERROR: Malformed tournamentId in request, expected long.");
            return false;
        }
    }

}
