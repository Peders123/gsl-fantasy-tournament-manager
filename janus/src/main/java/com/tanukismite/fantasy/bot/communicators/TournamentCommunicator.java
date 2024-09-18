package com.tanukismite.fantasy.bot.communicators;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tanukismite.fantasy.bot.signup.PostData;
import com.tanukismite.fantasy.bot.signup.TournamentData;


/**
 * Implementation of {@link MercuryCommunicator} for interactions related to "tournaments".
 * 
 * @see MercuryCommunicator
 * @author Rory Caston
 * @since 1.0
 */
public class TournamentCommunicator extends MercuryCommunicator {

    private static final Logger logger = LogManager.getLogger("ConsoleLogger");
    private static final String ID_ERROR = "Malformed tournamentId in request: expected integer, got {}";

    /**
     * Calls the initilisation function to obtain an API authentication token.
     */
    public TournamentCommunicator() {

        super();
        try {
            this.initialise();
        } catch (IOException e) {
            logger.error("Error on construction.");
        }

    }

    /**
     * @return The base API endpoint URL for tournaments.
     */
    @Override
    protected String getBaseEndpoint() {
        return this.baseUrl + "api/tournaments/";
    }

    /**
     * Retrieves all tournament data from the API.
     * 
     * @return A {@link JsonNode} containing all tournament data.
     * @throws IOException if the GET request fails.
     */
    @Override
    public JsonNode get() throws IOException {
        return genericGet(new URL(this.getBaseEndpoint()));
    }

    /**
     * Writes a new tournament signup to the API. Validates the provided data is of the correct type.
     * 
     * @param data The {@link PostData} containing tournament sign-up details.
     * @return {@code true} if the POST is successful, {@code false} if otherwise.
     * @throws IOException if the POST request fails.
     */
    @Override
    public boolean post(PostData data) throws IOException {
        if (!(data instanceof TournamentData)) {
            logger.error("Malformed tournament signup data format.");
            return false;
        }
        return genericPost(new URL(this.getBaseEndpoint()), data.toMap());
    }

    /**
     * Retrieves data relating to a specific tournament from the API.
     * 
     * @param <T>          The type of the tournamentId, expected Integer.
     * @param tournamentId The tournament's ID.
     * @return A {@link JsonNode} containing the tournament data.
     * @throws IOException if the GET request fails.
     */
    @Override
    public <T> JsonNode getDetailed(T tournamentId) throws IOException {
        if (tournamentId instanceof Integer) {
            return genericDetailedGet(new URL(this.getBaseEndpoint() + tournamentId));
        } else {
            logger.error(ID_ERROR, tournamentId);
            return null;
        }
    }

    /**
     * Deletes a specific tournament from the API.
     * 
     * @param <T>          The type of the tournamentId, expected Integer.
     * @param tournamentId The tournament's ID.
     * @return {@code true} if the DELETE is successful, {@code false} if otherwise.
     * @throws IOException is the DELETE request fails.
     */
    @Override
    public <T> boolean delete(T tournamentId) throws IOException {
        if (tournamentId instanceof Integer) {
            return genericDelete(new URL(this.getBaseEndpoint() + tournamentId));
        } else {
            logger.error(ID_ERROR, tournamentId);
            return false;
        }
    }

    /**
     * Replaces a specific tournament from the API.
     * 
     * @param <T>          The type of the tournamentId, expected Integer.
     * @param tournamentId The tournament's ID.
     * @param data         The {@link PostData} containing tournament data.
     * @return {@code true} if the DELETE is successful, {@code false} if otherwise.
     * @throws IOException is the DELETE request fails.
     */
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
