package com.tanukismite.fantasy.bot.listeners;

import com.tanukismite.fantasy.bot.handlers.Handler;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

public class ModalListener extends BaseListener {

    public ModalListener(Handler handler) {
        super(handler);
    }

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
