package com.tanukismite.fantasy.bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * The {@code HttpHandler} class manages HTTP connections and handles requests/responses 
 * with an external server. It provides utility methods to send data, retrieve responses, 
 * and handle error responses.
 *
 * <p>This class can be used to perform HTTP requests (GET, POST) for interaction 
 * with web APIs, handling both success and error responses.</p>
 *
 * @see HttpURLConnection
 * @see ObjectMapper
 *
 * @author Rory Caston
 * @since 1.0
 */
public class HttpHandler {

    private static final Logger logger = LogManager.getLogger("ConsoleLogger");

    private URL url;
    private HttpURLConnection conn;

    /**
     * Constructs a new {@code HttpHandler} instance for managing HTTP connections.
     *
     * @param url The URL for the HTTP connection.
     * @param requestMethod The HTTP method to use (e.g., GET, POST).
     * @param headers A map containing HTTP headers.
     * @throws IOException if there is an issue setting up the connection.
     */
    public HttpHandler(URL url, String requestMethod, Map<String, String> headers) throws IOException {

        this.url = url;
        this.conn = (HttpURLConnection) this.url.openConnection();
        this.conn.setRequestMethod(requestMethod);

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            this.conn.setRequestProperty(entry.getKey(), entry.getValue());
        }

    }

    /**
     * Writes a map of key-value pairs to the HTTP connection output stream.
     *
     * @param inputMap A map containing data to be sent.
     * @throws IOException if there is an issue writing data to the connection.
     */
    public void writeFromMap(Map<String, Object> inputMap) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        String input = mapper.writeValueAsString(inputMap);

        this.conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(input.getBytes());
            os.flush();
        }
        
    }

    /**
     * Retrieves the response code from the HTTP connection.
     *
     * @return The HTTP response code.
     * @throws IOException if there is an issue retrieving the response code.
     */
    public int getResponseCode() throws IOException {
        return this.conn.getResponseCode();
    }

    /**
     * Reads the HTTP response and converts it to a {@link JsonNode}.
     *
     * @return The JSON response from the server.
     * @throws IOException if there is an issue reading the response.
     */
    public JsonNode readToJson() throws IOException {

        int responseCode = this.conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(this.conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree((response.toString()));

        }

        return null;

    }

    /**
     * Reads the error stream from the HTTP connection and logs the error.
     *
     * @throws IOException if there is an issue reading the error stream.
     */
    public void readError() throws IOException {

        int responseCode = this.conn.getResponseCode();

        BufferedReader errorInput = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        String inputLine;
        StringBuilder errorResponse = new StringBuilder();

        while ((inputLine = errorInput.readLine()) != null) {
            errorResponse.append(inputLine);
        }
        errorInput.close();

        logger.error("Failed post. Code: {}\n{}", responseCode, errorResponse);

    }

    /**
     * Getter for conn.
     *
     * @return The current conn.
     */
    public HttpURLConnection getConn() {
        return this.conn;
    }

}