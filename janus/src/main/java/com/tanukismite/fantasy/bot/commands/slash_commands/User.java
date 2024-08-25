package com.tanukismite.fantasy.bot.commands.slash_commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.communicators.MercuryCommunicator;
import com.tanukismite.fantasy.bot.handlers.Handler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.io.IOException;

public class User implements Command {

    private SlashCommandInteractionEvent event;

    public User(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute(Handler handler) {

        StringBuilder message;

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

                message = new StringBuilder();
                message.append("\nDiscord: " + user.get("discord_name").asText());

                event.getChannel().sendMessage(message).queue();

            }

        }

    }

}