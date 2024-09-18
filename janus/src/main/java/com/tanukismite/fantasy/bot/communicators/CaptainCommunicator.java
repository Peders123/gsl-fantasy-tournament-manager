package com.tanukismite.fantasy.bot.communicators;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tanukismite.fantasy.bot.HttpHandler;
import com.tanukismite.fantasy.bot.signup.CaptainSignupData;
import com.tanukismite.fantasy.bot.signup.PostData;


/**
 * Implementation of {@link MercuryCommunicator} for interactions related to "captains".
 * 
 * @see MercuryCommunicator
 * @author Rory Caston
 * @since 1.0
 */
public class CaptainCommunicator extends MercuryCommunicator {

    private static final Logger logger = LogManager.getLogger("ConsoleLogger");
    private static final String ID_ERROR = "Malformed captainId in request: expected integer, got {}";

    /**
     * Calls the initilisation function to obtain an API authentication token.
     */
    public CaptainCommunicator() {

        super();
        try {
            this.initialise();
        } catch (IOException error) {
            logger.error("Error on construction.", error);
        }

    }

    /**
     * @return The base API endpoint URL for captains.
     */
    @Override
    protected String getBaseEndpoint() {
        return this.baseUrl + "api/captains/";
    }

    /**
     * Retrieves all captain data from the API.
     * 
     * @return A {@link JsonNode} containing all captain data.
     * @throws IOException if the GET request fails.
     */
    @Override
    public JsonNode get() throws IOException {
        return genericGet(new URL(this.getBaseEndpoint()));
    }

    /**
     * Writes a new captain signup to the API. Validates the provided data is of the correct type.
     * 
     * @param data The {@link PostData} containing captain sign-up details.
     * @return {@code true} if the POST is successful, {@code false} if otherwise.
     * @throws IOException if the POST request fails.
     */
    @Override
    public boolean post(PostData data) throws IOException {
        if (!(data instanceof CaptainSignupData)) {
            logger.error("Malformed captain signup data format.");
            return false;
        }
        return genericPost(new URL(this.getBaseEndpoint()), data.toMap());
    }

    /**
     * Retrieves data relating to a specific captain from the API.
     * 
     * @param <T>       The type of the captainId, expected Integer.
     * @param captainId The captain's ID.
     * @return A {@link JsonNode} containing the captain data.
     * @throws IOException if the GET request fails.
     */
    @Override
    public <T> JsonNode getDetailed(T captainId) throws IOException {
        if (captainId instanceof Integer) {
            return genericDetailedGet(new URL(this.getBaseEndpoint() + captainId));
        } else {
            logger.error(ID_ERROR, captainId.getClass());
            return null;
        }
    }

    /**
     * Deletes a specific captain from the API.
     * 
     * @param <T>       The type of the captainId, expected Integer.
     * @param captainId The captain's ID.
     * @return {@code true} if the DELETE is successful, {@code false} if otherwise.
     * @throws IOException is the DELETE request fails.
     */
    @Override
    public <T> boolean delete(T captainId) throws IOException {
        if (captainId instanceof Integer) {
            return genericDelete(new URL(this.getBaseEndpoint() + captainId));
        } else {
            logger.error(ID_ERROR, captainId.getClass());
            return false;
        }
    }

    /**
     * Replaces a specific captain from the API.
     * 
     * @param <T>       The type of the captainId, expected Integer.
     * @param captainId The captain's ID.
     * @param data      The {@link PostData} containing captain data.
     * @return {@code true} if the DELETE is successful, {@code false} if otherwise.
     * @throws IOException is the DELETE request fails.
     */
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

    /**
     * Gets a specific user's respective captain from the API.
     * 
     * @param userId The user id to get the captain for.
     * @return {@link JsonNode} containing the captain data.
     * @throws IOException if the GET request fails.
     */
    public JsonNode getCaptainUser(long userId) throws IOException {
        return genericDetailedGet(new URL(this.getBaseEndpoint() + "by-user/" + userId));
    }

    /**
     * Gets if a specific user has a captain in the API.
     * 
     * @param userId The user id to check the captain for.
     * @return {@code true} if a captain exists for the user, {@code false} if otherwise.
     * @throws IOException If the GET request fails.
     */
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
