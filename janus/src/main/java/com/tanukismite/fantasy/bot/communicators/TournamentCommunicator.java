package com.tanukismite.fantasy.bot.communicators;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.tanukismite.fantasy.bot.signup.PostData;
import com.tanukismite.fantasy.bot.signup.TournamentData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TournamentCommunicator extends MercuryCommunicator {

    private static final Logger logger = LogManager.getLogger("ConsoleLogger");
    private static final String ID_ERROR = "Malformed tournamentId in request: expected integer, got {}";

    public TournamentCommunicator() {

        super();

        try {
            this.initialise();
        } catch (IOException e) {
            logger.error("Error on construction.");
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
        if (!(data instanceof TournamentData)) {
            logger.error("Malformed tournament signup data format.");
            return false;
        }
        return genericPost(new URL(this.getBaseEndpoint()), data.toMap());
    }

    @Override
    public <T> JsonNode getDetailed(T tournamentId) throws IOException {
        if (tournamentId instanceof Integer) {
            return genericDetailedGet(new URL(this.getBaseEndpoint() + tournamentId));
        } else {
            logger.error(ID_ERROR, tournamentId);
            return null;
        }
    }

    @Override
    public <T> boolean delete(T tournamentId) throws IOException {
        if (tournamentId instanceof Integer) {
            return genericDelete(new URL(this.getBaseEndpoint() + tournamentId));
        } else {
            logger.error(ID_ERROR, tournamentId);
            return false;
        }
    }

    @Override
    public <T> boolean put(T tournamentId, PostData data) throws IOException {
        if (tournamentId instanceof Integer) {
            if (!(data instanceof TournamentData)) {
                logger.error("Malformed tournament signup data format.");
                return false;
            }
            return genericPut(new URL(this.getBaseEndpoint() + tournamentId), data.toMap());
        } else {
            logger.error(ID_ERROR, tournamentId);
            return false;
        }
    }

}
