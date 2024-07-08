package com.glaeriasmite.fantasy.bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class MercuryCommunicator {

    private String token;

    public MercuryCommunicator(String username, String password) throws IOException {

        URL url = new URL("http://127.0.0.1:8001/api-token-auth/");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setRequestProperty("Content-Type", "application/json");

        String jsonString = new JSONObject()
            .put("username", "Peders")
            .put("password", "Pa55we1rd")
            .toString();

        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(jsonString.getBytes());
        os.flush();
        os.close();

        BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        JSONTokener tokener = new JSONTokener(input);
        JSONArray response = new JSONArray(tokener);

        System.out.println(response);

    }

}
