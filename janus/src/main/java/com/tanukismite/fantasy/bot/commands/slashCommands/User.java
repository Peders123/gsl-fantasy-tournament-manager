package com.tanukismite.fantasy.bot.commands.slashCommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.communicators.MercuryCommunicator;
import com.tanukismite.fantasy.bot.handlers.Action;
import com.tanukismite.fantasy.bot.handlers.Handler;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.FluentRestAction;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;

import java.io.IOException;

public class User implements Command {

    private SlashCommandInteractionEvent event;

    public User(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute(Handler handler) {

        String message;
        FluentRestAction<Message, MessageCreateAction> action;

        if (event.getOption("method").getAsString().equals("get")) {

            JsonNode response = null;

            MercuryCommunicator communicator = handler.getCommunicator("user");
            try {
                response = communicator.get();
            } catch (IOException e) {
                System.out.println("ERROR");
                e.printStackTrace();
                return;
            }

            for (JsonNode user : response) {

                message = "ID: " + user.get("user_id").asText();
                message += "\nDiscord: " + user.get("discord_name").asText();
                action = Action.sendMessage(event.getChannel(), message);
                this.queue(action);

            }

        }

    }

    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {

        request.queue();

    }

}