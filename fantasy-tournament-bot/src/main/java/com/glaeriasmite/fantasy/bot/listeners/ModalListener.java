package com.glaeriasmite.fantasy.bot.listeners;

import com.glaeriasmite.fantasy.bot.handlers.Handler;
import com.glaeriasmite.fantasy.bot.signup.CaptainSignupData;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

public class ModalListener extends BaseListener {

    public ModalListener(Handler handler) {
        super(handler);
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {

        String[] id = event.getModalId().split(":");
        String userId = id[0];
        String type = id[1];

        switch (type) {

            case "signup-modal":

                if (id[2] == "captain-signup") {
                    try {
                        this.handler.executeMethod(
                            this.handler.getContext().getUserSignupData(userId, CaptainSignupData.class).getSignupRoot(),
                            "submitModal",
                            event
                        );
                    } catch (Exception e) {
                        System.out.println("ERROR");
                        System.out.println(e);
                    }
                } else {
                    try {
                        this.handler.executeMethod(
                            this.handler.getContext().getUserSignupData(userId, CaptainSignupData.class).getSignupRoot(),
                            "submitModal",
                            event
                        );
                    } catch (Exception e) {
                        System.out.println("ERROR");
                        System.out.println(e);
                    }
                }
                break;

        }

    }

}
