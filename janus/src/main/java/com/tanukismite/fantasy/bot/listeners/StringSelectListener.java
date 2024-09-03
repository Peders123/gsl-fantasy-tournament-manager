package com.tanukismite.fantasy.bot.listeners;

import com.tanukismite.fantasy.bot.commands.slash_commands.CreateSignups;
import com.tanukismite.fantasy.bot.handlers.Handler;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

public class StringSelectListener extends BaseListener {

    public StringSelectListener(Handler handler) {
        super(handler);
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {

        String id = event.getComponentId();
        CreateSignups signUpSession = this.handler.getContext().getSignupRoot();

        switch (id) {

            case "role1":

                signUpSession.submitFirstRole(handler, event);
                break;

            case "role2":

                signUpSession.submitSecondRole(handler, event);
                break;

            default:
                BaseListener.notImplemented(event.getChannel());

        }

    }

}
