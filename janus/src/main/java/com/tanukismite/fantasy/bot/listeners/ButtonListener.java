package com.tanukismite.fantasy.bot.listeners;

import java.io.IOException;

import com.tanukismite.fantasy.bot.commands.slash_commands.CreateSignups;
import com.tanukismite.fantasy.bot.commands.slash_commands.Edit;
import com.tanukismite.fantasy.bot.communicators.CaptainCommunicator;
import com.tanukismite.fantasy.bot.communicators.PlayerCommunicator;
import com.tanukismite.fantasy.bot.communicators.UserCommunicator;
import com.tanukismite.fantasy.bot.handlers.Handler;
import com.tanukismite.fantasy.bot.signup.UserSignupData;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ButtonListener extends BaseListener {

    private static final Logger logger = LogManager.getLogger("ConsoleLogger");

    public ButtonListener(Handler handler) {
        super(handler);
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        String[] id = event.getComponentId().split(":");
        String type = id[1];

        switch (type) {

            case "test":
                logger.info("Testing button.");
                break;

            case "bad":
                logger.info("Bad button.");
                break;

            case "edit":
                this.edit(event);
                break;

            case "delete":
                this.delete(event);
                break;

            case "player-signup":
                this.signup(event, false);
                break;

            case "captain-signup":
                this.signup(event, true);
                break;

            case "signout":
                this.signout(event);
                break;

            default:
                BaseListener.notImplemented(event.getChannel());

        }

    }

    private void edit(ButtonInteractionEvent event) {

        try {
            Edit.editMessage(event.getMessageChannel(), event.getMessageId());
        } catch (Exception error) {
            logger.error("Error editing message with id {}", event.getMessageId(), error);
        }

    }

    private void delete(ButtonInteractionEvent event) {

        try {
            Edit.deleteMessage(event.getMessageChannel(), event.getMessageId());
        } catch (Exception error) {
            logger.error("Error deleting message with id {}", event.getMessageId(), error);
        }

    }

    private void signup(ButtonInteractionEvent event, boolean captain) {

        Long longId = Long.parseLong(event.getUser().getId());

        UserCommunicator userCommunicator = (UserCommunicator) this.handler.getCommunicator("user");
        boolean exists = CreateSignups.checkUserExists(this.handler, longId);
        boolean signupExists = false;

        PlayerCommunicator playerCommunicator = (PlayerCommunicator) this.handler.getCommunicator("player");
        CaptainCommunicator captainCommunicator = (CaptainCommunicator) this.handler.getCommunicator("captain");

        boolean playerExists;
        boolean captainExists;

        try {
            playerExists = playerCommunicator.getPlayerUserExists(longId);
            captainExists = captainCommunicator.getCaptainUserExists(longId);
        } catch (IOException error) {
            logger.error("Error determining whether or not user is signed up.", error);
            return;
        }

        logger.debug("Player exists: {}", playerExists);
        logger.debug("Captain exists: {}", captainExists);

        signupExists = playerExists || captainExists;

        if (signupExists) {
            this.handler.getContext().getSignupRoot().alreadySignedUp(event);
        }

        if (!exists) {
            try {
                UserSignupData data = new UserSignupData(event.getUser().getId(), event.getUser().getName());
                userCommunicator.post(data);
            } catch (IOException e) {
                logger.error("Error creating user with id: {}", event.getUser().getId());
                return;
            }
        }

        this.handler.getContext().getSignupRoot().createModal(handler, event, captain);

    }

    private void signout(ButtonInteractionEvent event) {

        Long longId = Long.parseLong(event.getUser().getId());

        boolean signupExists = false;

        PlayerCommunicator playerCommunicator = (PlayerCommunicator) this.handler.getCommunicator("player");
        CaptainCommunicator captainCommunicator = (CaptainCommunicator) this.handler.getCommunicator("captain");

        boolean playerExists;
        boolean captainExists;

        try {
            playerExists = playerCommunicator.getPlayerUserExists(longId);
            captainExists = captainCommunicator.getCaptainUserExists(longId);
        } catch (IOException error) {
            logger.error("Error determining whether or not user is signed up.", error);
            return;
        }

        logger.debug("Player exists: {}", playerExists);
        logger.debug("Captain exists: {}", captainExists);

        signupExists = playerExists || captainExists;

        if (!signupExists) {
            event.reply("You are not currently signed up.").setEphemeral(true).queue();
        } else {
            logger.info("Deleting user signup with id {}", event.getUser().getId());
            this.handler.getContext().getSignupRoot().signout(handler, event);
        }

    }

}