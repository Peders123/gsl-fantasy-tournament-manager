package com.glaeriasmite.fantasy.bot.commands;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.glaeriasmite.fantasy.bot.HttpHandler;
import com.glaeriasmite.fantasy.bot.handlers.Action;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.RestAction;

public class Signup implements Command {

    private SlashCommandInteractionEvent event;

    public Signup(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute() {

        String message;
        RestAction<InteractionHook> action;

        /* JSONObject user = new JSONObject();
        user.put("user_id", event.getUser().getId());
        user.put("discord_name", event.getUser().getName());
        user.put("smite_name", event.getOption("smite-name").getAsString());
        JSONArray json = new JSONArray();
        json.put(user);
        try {
            HttpHandler handler = new HttpHandler("http://127.0.0.1:8000/Users/");
            int responseCode = handler.sendPost(json);
            System.out.println(responseCode);
            if (responseCode == 201) {
                message = "Successfully created user";
                action = Action.replyWithMessage(event, message);
                this.queue(action);
            }
        } catch(IOException e) {
            message = "Error";
            action = Action.replyWithMessage(event, message);
            this.queue(action);
        } */

        JSONObject player = new JSONObject();
        player.put("tournament_id", 1);
        player.put("user_id", event.getUser().getId());
        player.put("role_1", "adc");
        player.put("role_2", "support");
        player.put("estimated_value", 10);
        JSONArray player_json = new JSONArray();
        player_json.put(player);
        try {
            HttpHandler handler = new HttpHandler("http://127.0.0.1:8000/Players/");
            int responseCode = handler.sendPost(player_json);
            System.out.println(responseCode);
            if (responseCode == 201) {
                message = "Successfully created player";
                action = Action.replyWithMessage(event, message);
                this.queue(action);
            }
        } catch(IOException e) {
            message = "Error";
            action = Action.replyWithMessage(event, message);
            this.queue(action);
        }

    }

    @Override
    public <R> void queue(RestAction<R> request) {

        request.queue();

    }

}
