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


/**
 * Implementation of {@link MercuryCommunicator} for interactions related to "players".
 *
 * @see MercuryCommunicator
 * @author Rory Caston
 * @since 1.0
 */
public class PlayerCommunicator extends MercuryCommunicator {

    private static final Logger logger = LogManager.getLogger("ConsoleLogger");
    private static final String ID_ERROR = "Malformed playerId in request: expected integer, got {}";

    /**
     * Calls the initilisation function to obtain an API authentication token.
     */
    public PlayerCommunicator() {

        super();
        try {
            this.initialise();
        } catch (IOException e) {
            logger.error("Error on construction.");
        }

    }

    /**
     * Getter for baseUrl.
     * 
     * @return The base API endpoint URL for players.
     */
    @Override
    protected String getBaseEndpoint() {
        return this.baseUrl + "api/players/";
    }

    /**
     * Retrieves all player data from the API.
     *
     * @return A {@link JsonNode} containing all player data.
     * @throws IOException if the GET request fails.
     */
    @Override
    public JsonNode get() throws IOException {
        return genericGet(new URL(this.getBaseEndpoint()));
    }

    /**
     * Writes a new player signup to the API. Validates the provided data is of the correct type.
     *
     * @param data The {@link PostData} containing player sign-up details.
     * @return {@code true} if the POST is successful, {@code false} if otherwise.
     * @throws IOException if the POST request fails.
     */
    @Override
    public boolean post(PostData data) throws IOException {
        if (!(data instanceof PlayerSignupData)) {
            logger.error("Malformed player signup data format.");
            return false;
        }
        return genericPost(new URL(this.getBaseEndpoint()), data.toMap());
    }

    /**
     * Retrieves data relating to a specific player from the API.
     *
     * @param <T>      The type of the playerId, expected Integer.
     * @param playerId The player's ID.
     * @return A {@link JsonNode} containing the player data.
     * @throws IOException if the GET request fails.
     */
    @Override
    public <T> JsonNode getDetailed(T playerId) throws IOException {
        if (playerId instanceof Integer) {
            return genericDetailedGet(new URL(this.getBaseEndpoint() + playerId.toString()));
        } else {
            logger.error(ID_ERROR, playerId);
            return null;
        }
    }

    /**
     * Deletes a specific player from the API.
     *
     * @param <T>      The type of the playerId, expected Integer.
     * @param playerId The player's ID.
     * @return {@code true} if the DELETE is successful, {@code false} if otherwise.
     * @throws IOException is the DELETE request fails.
     */
    @Override
    public <T> boolean delete(T playerId) throws IOException {
        if (playerId instanceof Integer) {
            return genericDelete(new URL(this.getBaseEndpoint() + playerId.toString()));
        } else {
            logger.error(ID_ERROR, playerId);
            return false;
        }
    }

    /**
     * Replaces a specific player from the API.
     *
     * @param <T>      The type of the playerId, expected Integer.
     * @param playerId The player's ID.
     * @param data     The {@link PostData} containing player data.
     * @return {@code true} if the DELETE is successful, {@code false} if otherwise.
     * @throws IOException is the DELETE request fails.
     */
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

    /**
     * Gets a specific user's respective player from the API.
     *
     * @param userId The user id to get the player for.
     * @return {@link JsonNode} containing the player data.
     * @throws IOException if the GET request fails.
     */
    public JsonNode getPlayerUser(long userId) throws IOException {
        return genericDetailedGet(new URL(this.getBaseEndpoint() + "by-user/" + userId));
    }

    /**
     * Gets if a specific user has a player in the API.
     *
     * @param userId The user id to check the player for.
     * @return {@code true} if a player exists for the user, {@code false} if otherwise.
     * @throws IOException If the GET request fails.
     */
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
