package com.tanukismite.fantasy.bot.listeners;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import com.tanukismite.fantasy.bot.handlers.Handler;


/**
 * The {@code ModalListener} class extends {@link BaseListener} and handles interactions 
 * triggered by modal submissions within Discord. It processes various modal types such as 
 * user signups or tournament data submission.
 * 
 * <p><b>Usage:</b> This class is automatically triggered when a modal is submitted in Discord,
 * and it processes the event based on the modal type (e.g., signup modal, tournament modal).</p>
 * 
 * @see BaseListener
 * 
 * @author Rory Caston
 * @since 1.0
 */
public class ModalListener extends BaseListener {

    /**
     * Constructor with a {@link Handler} reference.
     * @param handler The {@link Handler} current app handler.
     */
    public ModalListener(Handler handler) {
        super(handler);
    }

    /**
     * Handles the {@link ModalInteractionEvent} when a modal is submitted in Discord. Depending on 
     * the modal's ID, different actions are taken.
     * 
     * @param event The {@link ModalInteractionEvent} representing the modal submission.
     */
    @Override
    public void onModalInteraction(ModalInteractionEvent event) {

        String[] id = event.getModalId().split(":");
        String type = id[1];

        switch (type) {

            case "signup-modal":
                this.handler.getContext().getSignupRoot().submitModal(handler, event);
                break;

            case "tournament-modal":
                this.handler.getContext().getTournamentRoot().submitModal(handler, event);
                break;

            default:
                BaseListener.notImplemented(event.getChannel());

        }

    }

}
