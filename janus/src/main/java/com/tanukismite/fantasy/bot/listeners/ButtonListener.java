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


/**
 * The {@code ButtonListener} class extends {@link BaseListener} and handles interactions 
 * triggered by button clicks within Discord. It manages signups and signouts based on 
 * user button interactions.
 * 
 * <p><b>Usage:</b> This class is automatically triggered when a button is clicked in Discord, and
 * it processes the event based on the type of button clicked (e.g., player signup, captain
 * signup).</p>
 * 
 * @see BaseListener
 * 
 * @author Rory Caston
 * @since 1.0
 */
public class ButtonListener extends BaseListener {

    private static final Logger logger = LogManager.getLogger("ConsoleLogger");

    /**
     * Constructor with a {@link Handler} reference.
     * @param handler The {@link Handler} current app handler.
     */
    public ButtonListener(Handler handler) {
        super(handler);
    }

    /**
     * Handles the {@link ButtonInteractionEvent} when a button is clicked in Discord. Depending on
     * the button's id, different actions are taken.
     * 
     * @param event The {@link ButtonInteractionEvent} representing the button blick.
     */
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

    /**
     * Processes user signup when a button interaction event is triggered for signup actions.
     * The method differentiates between player and captain signups and communicates with
     * appropriate communicators to verify or create user signups.
     * 
     * @param event   The {@link ButtonInteractionEvent} triggered by a user click.
     * @param captain Indicates whether the signup is for a captain.
     */
    private void signup(ButtonInteractionEvent event, boolean captain) {

        Long longId = Long.parseLong(event.getUser().getId());

        PlayerCommunicator playerCommunicator = (PlayerCommunicator) this.handler.getCommunicator("player");
        CaptainCommunicator captainCommunicator = (CaptainCommunicator) this.handler.getCommunicator("captain");

        boolean playerExists;
        boolean captainExists;

        // Determine if the user is signed up.
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

        // Tell the user if they are already signed up.
        if (signupExists) {
            this.handler.getContext().getSignupRoot().alreadySignedUp(event);
            return;
        }

        // Create a new user in the database.
        if (!CreateSignups.checkUserExists(this.handler.getCommunicator("user"), longId)) {
            try {
                this.handler.getCommunicator("user").post(
                    new UserSignupData(event.getUser().getId(), event.getUser().getName())
                );
            } catch (IOException e) {
                logger.error("Error creating user with id: {}", event.getUser().getId());
                return;
            }
        }

        // Continue sign-up execution.
        this.handler.getContext().getSignupRoot().createModal(this.handler.getContext(), event, captain);

    }

    /**
     * Processes user signout when a button interaction event is triggered for signout actions.
     * The method verifies whether the user is already signed up and removes them if necessary.
     * 
     * @param event The {@link ButtonInteractionEvent} triggered by a user click.
     */
    private void signout(ButtonInteractionEvent event) {

        Long longId = Long.parseLong(event.getUser().getId());

        PlayerCommunicator playerCommunicator = (PlayerCommunicator) this.handler.getCommunicator("player");
        CaptainCommunicator captainCommunicator = (CaptainCommunicator) this.handler.getCommunicator("captain");

        boolean playerExists;
        boolean captainExists;

        // Determine if player is signed up.
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

        // Take actions.
        if (!signupExists) {
            event.reply("You are not currently signed up.").setEphemeral(true).queue();
        } else {
            logger.info("Deleting user signup with id {}", event.getUser().getId());
            this.handler.getContext().getSignupRoot().signout(handler, event);
        }

    }

}