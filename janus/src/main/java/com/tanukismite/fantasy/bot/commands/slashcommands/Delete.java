package com.tanukismite.fantasy.bot.commands.slashcommands;

import java.io.IOException;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.communicators.MercuryCommunicator;
import com.tanukismite.fantasy.bot.handlers.Handler;
import com.tanukismite.fantasy.bot.signup.UserSignupData;


public class Delete implements Command {
    
    private static final Logger logger = LogManager.getLogger("ConsoleLogger");
    
    private SlashCommandInteractionEvent event;

    public Delete(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute(Handler handler) {

        UserSignupData data = new UserSignupData(this.event.getUser().getId(), this.event.getUser().getName());

        MercuryCommunicator communicator = handler.getCommunicator("user");

        try {
            boolean success = communicator.post(data);
            logger.debug("User created: {}", success);
            success = communicator.delete(Long.parseLong(data.getId()));
            logger.debug("User deleted: {}", success);
        } catch (IOException error) {
            logger.error("Issue when creating and deleting user.", error);
        }

    }

}
