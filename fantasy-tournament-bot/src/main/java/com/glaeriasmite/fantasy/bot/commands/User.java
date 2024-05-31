package com.glaeriasmite.fantasy.bot.commands;

import com.glaeriasmite.fantasy.bot.HttpHandler;
import com.glaeriasmite.fantasy.bot.handlers.Action;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.RestAction;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

public class User implements Command {

    private SlashCommandInteractionEvent event;

    public User(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute() {

        // System.out.println("GET Response Code :: " + responseCode);

        String message;
        RestAction<Message> action;

        try {
            HttpHandler handler = new HttpHandler("http://127.0.0.1:8000/Users/");
            JSONArray response = handler.sendGet();

            System.out.println(response);

            JSONObject user = response.getJSONObject(0);

            System.out.println(user);

            message = "ID: " + String.valueOf(user.getInt("user_id"));
            action = Action.sendMessage(event.getChannel(), message);
            this.queue(action);

            message = "Discord: " + user.getString("discord_name");
            action = Action.sendMessage(event.getChannel(), message);
            this.queue(action);

            message = "Smite: " + user.getString("smite_name");
            action = Action.sendMessage(event.getChannel(), message);
            this.queue(action);

        } catch(IOException e) {
            message = "Error";
            action = Action.sendMessage(event.getChannel(), message);
            this.queue(action);
        }

    }

    @Override
    public <R> void queue(RestAction<R> request) {

        request.queue();

    }

}