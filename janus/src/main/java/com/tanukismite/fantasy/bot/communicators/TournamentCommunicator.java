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
        return this.baseUrl + "api/tournaments/";
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
        if (tournamentId instanceof Integer) {
            return genericDelete(new URL(this.getBaseEndpoint() + tournamentId));
        } else {
            System.out.println("ERROR: Malformed tournamentId in request, expected integer.");
            return false;
        }
    }

    @Override
    public <T> boolean put(T tournamentId, PostData data) throws IOException {
        if (tournamentId instanceof Integer) {
            if (!(data instanceof TournamentData)) {
                System.out.println("MALFORMED DATA SIGNUP FORMAT");
                return false;
            }
            return genericPut(new URL(this.getBaseEndpoint() + tournamentId), data.toMap());
        } else {
            System.out.println("ERROR: Malformed tournamentId in request, expected integer.");
            return false;
        }
    }

}
