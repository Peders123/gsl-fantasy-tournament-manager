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

                if (id[2] == "captain-signup") {
                    try {
                        this.handler.executeMethod(
                            this.handler.getContext().getSignupRoot(),
                            "submitModal",
                            event
                        );
                    } catch (Exception e) {
                        System.out.println("ERROR");
                        e.printStackTrace();
                    }
                } else {
                    try {
                        this.handler.executeMethod(
                            this.handler.getContext().getSignupRoot(),
                            "submitModal",
                            event
                        );
                    } catch (Exception e) {
                        System.out.println("ERROR");
                        e.printStackTrace();
                    }
                }
                break;

            case "tournament-modal":

                try {
                    this.handler.executeMethod(
                        this.handler.getContext().getTournamentRoot(),
                        "submitModal",
                        event
                    );
                } catch (Exception e) {
                    System.out.println("ERROR");
                    e.printStackTrace();
                }

        }

    }

}
