package com.tanukismite.fantasy.bot.commands.slashcommands;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.communicators.MercuryCommunicator;
import com.tanukismite.fantasy.bot.handlers.Handler;


public class User implements Command {

    private static final Logger logger = LogManager.getLogger("ConsoleLogger");

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
            } catch (IOException error) {
                logger.error("Error getting users.", error);
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