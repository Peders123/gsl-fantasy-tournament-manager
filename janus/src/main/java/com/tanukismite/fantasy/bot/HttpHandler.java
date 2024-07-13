package com.tanukismite.fantasy.bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONTokener;

public class HttpHandler {

    private URL url;
    private HttpURLConnection conn;

    public HttpHandler(String url) throws IOException {

        this.url = new URL(url);
        this.conn = (HttpURLConnection) this.url.openConnection();

    }

    public JSONArray sendGet() throws IOException {

        this.conn.setRequestMethod("GET");
        this.conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = this.conn.getResponseCode();

        if (responseCode == 200) {
            BufferedReader input = new BufferedReader(new InputStreamReader(this.conn.getInputStream()));
            JSONTokener tokener = new JSONTokener(input);
            JSONArray response = new JSONArray(tokener);
            return response;
        } else {
            return null;
        }

    }

    public int sendPost(JSONArray json) throws IOException {

        this.conn.setRequestMethod("POST");
        this.conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        this.conn.setRequestProperty("Content-Type", "application/json");

        this.conn.setDoOutput(true);
        OutputStream os = this.conn.getOutputStream();
        os.write(json.toString().getBytes());
        os.flush();
        os.close();

        int responseCode = this.conn.getResponseCode();

        return responseCode;

    }

}
