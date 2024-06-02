package com.glaeriasmite.fantasy.bot.commands;

import com.glaeriasmite.fantasy.bot.HttpHandler;
import com.glaeriasmite.fantasy.bot.handlers.Action;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
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

        String message;
        RestAction<Message> action;
        RestAction<InteractionHook> hookAction;

        System.out.println(event.getOption("method").getAsString());

        if (event.getOption("method").getAsString().equals("get")) {
            try {
                HttpHandler handler = new HttpHandler("http://127.0.0.1:8000/Users/");
                JSONArray response = handler.sendGet();
                JSONObject user;
    
                System.out.println(response);

                for (int i = 0; i < response.length(); i++) {
                    user = response.getJSONObject(i);
    
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
                }
    
            } catch(IOException e) {
                message = "Error";
                action = Action.sendMessage(event.getChannel(), message);
                this.queue(action);
            }
        } else if (event.getOption("method").getAsString().equals("post")) {
            JSONObject user = new JSONObject();
            user.put("user_id", 11);
            user.put("discord_name", "re1nn");
            user.put("smite_name", "re1nn");
            JSONArray json = new JSONArray();
            json.put(user);
            try {
                HttpHandler handler = new HttpHandler("http://127.0.0.1:8000/Users/");
                int responseCode = handler.sendPost(json);
                System.out.println(responseCode);
                if (responseCode == 201) {
                    message = "Successfully created user";
                    hookAction = Action.replyWithMessage(event, message);
                    this.queue(hookAction);
                }
            } catch(IOException e) {
                message = "Error";
                action = Action.sendMessage(event.getChannel(), message);
                this.queue(action);
            }
        }

    }

    @Override
    public <R> void queue(RestAction<R> request) {

        request.queue();

    }

}