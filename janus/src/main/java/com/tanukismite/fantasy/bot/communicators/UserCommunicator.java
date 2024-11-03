package com.tanukismite.fantasy.bot.communicators;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tanukismite.fantasy.bot.signup.PostData;
import com.tanukismite.fantasy.bot.signup.UserSignupData;


/**
 * Implementation of {@link MercuryCommunicator} for interactions related to "users".
 *
 * @see MercuryCommunicator
 * @author Rory Caston
 * @since 1.0
 */
public class UserCommunicator extends MercuryCommunicator {

    private static final Logger logger = LogManager.getLogger("ConsoleLogger");
    private static final String ID_ERROR = "Malformed userId in request: expected long, got {}";

    /**
     * Calls the initilisation function to obtain an API authentication token.
     */
    public UserCommunicator() {

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
     * @return The base API endpoint URL for users.
     */
    @Override
    protected String getBaseEndpoint() {
        return this.baseUrl + "api/users/";
    }

    /**
     * Retrieves all user data from the API.
     *
     * @return A {@link JsonNode} containing all user data.
     * @throws IOException if the GET request fails.
     */
    @Override
    public JsonNode get() throws IOException {
        return genericGet(new URL(this.getBaseEndpoint()));
    }

    /**
     * Writes a new user signup to the API. Validates the provided data is of the correct type.
     *
     * @param data The {@link PostData} containing user sign-up details.
     * @return {@code true} if the POST is successful, {@code false} if otherwise.
     * @throws IOException if the POST request fails.
     */
    @Override
    public boolean post(PostData data) throws IOException {
        if (!(data instanceof UserSignupData)) {
            logger.error("Malformed user signup data format.");
            return false;
        }
        return genericPost(new URL(this.getBaseEndpoint()), data.toMap());
    }

    /**
     * Retrieves data relating to a specific user from the API.
     *
     * @param <T>    The type of the userId, expected Long.
     * @param userId The user's ID.
     * @return A {@link JsonNode} containing the user data.
     * @throws IOException if the GET request fails.
     */
    @Override
    public <T> JsonNode getDetailed(T userId) throws IOException {
        if (userId instanceof Long) {
            return genericDetailedGet(new URL(this.getBaseEndpoint() + userId));
        } else {
            logger.error(ID_ERROR, userId);
            return null;
        }
    }

    /**
     * Deletes a specific user from the API.
     *
     * @param <T>    The type of the userId, expected Long.
     * @param userId The user's ID.
     * @return {@code true} if the DELETE is successful, {@code false} if otherwise.
     * @throws IOException is the DELETE request fails.
     */
    @Override
    public <T> boolean delete(T userId) throws IOException {
        if (userId instanceof Long) {
            return genericDelete(new URL(this.getBaseEndpoint() + userId));
        } else {
            logger.error(ID_ERROR, userId);
            return false;
        }
    }

    /**
     * Replaces a specific user from the API.
     *
     * @param <T>    The type of the userId, expected Long.
     * @param userId The user's ID.
     * @param data   The {@link PostData} containing user data.
     * @return {@code true} if the DELETE is successful, {@code false} if otherwise.
     * @throws IOException is the DELETE request fails.
     */
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