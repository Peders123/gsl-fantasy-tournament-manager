package com.tanukismite.fantasy.bot.listeners;

import java.io.IOException;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tanukismite.fantasy.bot.commands.slashcommands.CreateSignups;
import com.tanukismite.fantasy.bot.communicators.CaptainCommunicator;
import com.tanukismite.fantasy.bot.communicators.PlayerCommunicator;
import com.tanukismite.fantasy.bot.handlers.Handler;
import com.tanukismite.fantasy.bot.signup.UserSignupData;


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

    private void signup(ButtonInteractionEvent event, boolean captain) {

        Long longId = Long.parseLong(event.getUser().getId());

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

        boolean signupExists = playerExists || captainExists;

        if (signupExists) {
            this.handler.getContext().getSignupRoot().alreadySignedUp(event);
        }

        if (!CreateSignups.checkUserExists(this.handler, longId)) {
            try {
                this.handler.getCommunicator("user").post(
                    new UserSignupData(event.getUser().getId(), event.getUser().getName())
                );
            } catch (IOException e) {
                logger.error("Error creating user with id: {}", event.getUser().getId());
                return;
            }
        }

        this.handler.getContext().getSignupRoot().createModal(handler, event, captain);

    }

    private void signout(ButtonInteractionEvent event) {

        Long longId = Long.parseLong(event.getUser().getId());

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

        boolean signupExists = playerExists || captainExists;

        if (!signupExists) {
            event.reply("You are not currently signed up.").setEphemeral(true).queue();
        } else {
            logger.info("Deleting user signup with id {}", event.getUser().getId());
            this.handler.getContext().getSignupRoot().signout(handler, event);
        }

    }

}