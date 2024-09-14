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


public class HttpHandler {

    private static final Logger logger = LogManager.getLogger("ConsoleLogger");

    private URL url;
    private HttpURLConnection conn;

    public HttpHandler(URL url, String requestMethod, Map<String, String> headers) throws IOException {

        this.url = url;
        this.conn = (HttpURLConnection) this.url.openConnection();
        this.conn.setRequestMethod(requestMethod);

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            this.conn.setRequestProperty(entry.getKey(), entry.getValue());
        }

    }

    public void writeFromMap(Map<String, Object> inputMap) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        String input = mapper.writeValueAsString(inputMap);

        this.conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(input.getBytes());
            os.flush();
        }
        
    }

    public int getResponseCode() throws IOException {

        return this.conn.getResponseCode();

    }

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

    public HttpURLConnection getConn() {
        return this.conn;
    }

}