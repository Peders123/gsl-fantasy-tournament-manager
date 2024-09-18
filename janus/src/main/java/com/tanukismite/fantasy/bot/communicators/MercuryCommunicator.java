package com.tanukismite.fantasy.bot.communicators;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tanukismite.fantasy.bot.HttpHandler;
import com.tanukismite.fantasy.bot.signup.PostData;


/**
 * This class is an abstract base class for communicating with the Mercury REST APIs.
 * <p>
 * It provides common functionalities for HTTP GET, POST, PUT and DELETE requests using token-based
 * authentication. Subclasses of {@code MercuryCommunicator} should provide specific concrete
 * implementations for the required API interactions.
 * </p>
 * <p>
 * This class automatically initialises the API credentials and endpoint URLs by reading from
 * the configuration files: {@code config.json} and {@code secrets.json} at run time. It also
 * handles the process of obtaining an authentication token.
 * </p>
 *
 * @author Rory Caston
 * @since 1.0
 */
public abstract class MercuryCommunicator {

    private static final Logger logger = LogManager.getLogger("ConsoleLogger");

    private String username;
    private String password;
    protected String token;
    protected String baseUrl;

    /**
     * Constructors the communicator class by reading necessary data from config files.
     */
    protected MercuryCommunicator() {

        final String buildType = System.getenv("BUILD_TYPE");

        JsonNode configNode;
        JsonNode secretNode;
        ObjectMapper objectMapper = new ObjectMapper();

        // Reads the config files.
        try {
            configNode = objectMapper.readTree(new File("config.json"));
            secretNode = objectMapper.readTree(new File("secrets.json"));
        } catch (IOException error) {
            logger.error("Could not read config files.", error);
            return;
        }
        
        this.username = configNode.get("proc").get("username").get(buildType).asText();
        this.password = secretNode.get("passwords").get("proc").get(buildType).asText();
        this.baseUrl = configNode.get("endpoint").get(buildType).asText();

    }

    /**
     * Initialises the communicator by performing an API login to obtaina session token.
     *
     * @throws IOException If the authentication request fails.
     */
    public void initialise() throws IOException {

        // Sets headers
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0");
        headers.put("Content-Type", "application/json");

        // Reads the token
        HttpHandler tokenGrabber = new HttpHandler(new URL(this.baseUrl + "api-authentication/"), "POST", headers);
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("username", this.username);
        inputMap.put("password", this.password);
        tokenGrabber.writeFromMap(inputMap);

        this.token = tokenGrabber.readToJson().get("token").asText();

    }

    /**
     * Creates a new {@link HttpHandler} for making HTTP requests to the API.
     *
     * @param url      The target URL for the request.
     * @param method   The HTTP request method to use.
     * @param inputMap A map of data to include in the request body (optional).
     * @return A configured {@link HttpHandler} read to execute the request.
     * @throws IOException if an error occurs setting up the handler.
     */
    protected HttpHandler createHttpHandler(URL url, String method, Map<String, Object> inputMap) throws IOException {

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0");
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Token " + this.token);

        HttpHandler handler = new HttpHandler(url, method, headers);
        if (inputMap != null) {
            handler.writeFromMap(inputMap);
        }
        return handler;

    }

    /**
     * Sends a generic GET request to the specified URL.
     *
     * @param url The target URL for the GET request.
     * @return A {@link JsonNode} containing the response data.
     * @throws IOException if the GET request fails.
     */
    protected JsonNode genericGet(URL url) throws IOException {

        HttpHandler getter = createHttpHandler(url, "GET", null);
        int responseCode = getter.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_OK) {
            logger.error("Unsuccessful get: {}", responseCode);
            return getter.readToJson();
        }
        return getter.readToJson();

    }

    /**
     * Sends a generic POST request to the specified URL.
     *
     * @param url      The target URL for the request.
     * @param inputMap The POST data.
     * @return {@code true} if successful, {@code false} if otherwise.
     * @throws IOException if the POST request fails.
     */
    protected boolean genericPost(URL url, Map<String, Object> inputMap) throws IOException {

        HttpHandler poster = createHttpHandler(url, "POST", inputMap);
        int responseCode = poster.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_CREATED) {
            logger.error("Unsuccessful post: {}", responseCode);
            return false;
        }
        return true;
    }

    /**
     * Sends a generic GET request to the specified URL.
     *
     * @param url The target URL for the GET request.
     * @return A {@link JsonNode} containing the response data.
     * @throws IOException if the GET request fails.
     */
    protected JsonNode genericDetailedGet(URL url) throws IOException {

        HttpHandler getter = createHttpHandler(url, "GET", null);
        int responseCode = getter.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
            return getter.readToJson();
        } else {
            logger.error("Unsuccessful get: {}", responseCode);
            return getter.readToJson();
        }

    }

    /**
     * Sends a generic DELETE request to the specified URL.
     *
     * @param url The target URL for the DELETE request.
     * @return {@code true} if successful, {@code false} if otherwise.
     * @throws IOException if the DELETE request fails.
     */
    protected boolean genericDelete(URL url) throws IOException {
        HttpHandler deleter = createHttpHandler(url, "DELETE", null);
        return deleter.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT;
    }

    /**
     * Sends a generic PUT request to the specified URL.
     *
     * @param url      The target URL for the PUT request.
     * @param inputMap The PUT data.
     * @return {@code true} if successful, {@code false} if otherwise.
     * @throws IOException if the PUT request fails.
     */
    protected boolean genericPut(URL url, Map<String, Object> inputMap) throws IOException {
        HttpHandler putter = createHttpHandler(url, "PUT", inputMap);
        return putter.getResponseCode() == HttpURLConnection.HTTP_CREATED;
    }

    /**
     * Retrieves a list of resources from the API. Subclasses must implement.
     *
     * @return A {@link JsonNode} containing the retrieved data.
     * @throws IOException if the GET request fails.
     */
    public abstract JsonNode get() throws IOException;

    /**
     * Writes a resource to the API. Subclasses must implement.
     *
     * @param data The {@link PostData} to write.
     * @return A {@link JsonNode} containing the response data.
     * @throws IOException if the POST request fails.
     */
    public abstract boolean post(PostData data) throws IOException;

    /**
     * Gets a specific resource from the API. Subclasses must implement.
     *
     * @param <T> The type of the ID.
     * @param id  The ID of the resource to get.
     * @return A {@link JsonNode} containing the retrieved data.
     * @throws IOException if the GET request fails.
     */
    public abstract <T> JsonNode getDetailed(T id) throws IOException;

    /**
     * Deletes a specific resource from the API. Subclasses must implement.
     *
     * @param <T> The type of the ID.
     * @param id  The ID of the resource to delete.
     * @return A {@link JsonNode} containing the response data.
     * @throws IOException if the DELETE request fails.
     */
    public abstract <T> boolean delete(T id) throws IOException;

    /**
     * Replaces a specific resource from the API. Subclasses must implement.
     *
     * @param <T>  The type of the ID.
     * @param id   The ID of the resource to delete.
     * @param data The {@link PostData} to replace with.
     * @return A {@link JsonNode} containing the response data.
     * @throws IOException if the PUT request fails.
     */
    public abstract <T> boolean put(T id, PostData data) throws IOException;

    /**
     * Abstract getter for the specific API endpoint base.
     *
     * @return The base url.
     */
    protected abstract String getBaseEndpoint();

    /**
     * Getter for the authentication token.
     *
     * @return The authentication token.
     */
    public String getToken() {
        return this.token;
    }

}