package com.glaeriasmite.fantasy.bot.listeners;

import com.glaeriasmite.fantasy.bot.commands.slashCommands.CreateSignups;
import com.glaeriasmite.fantasy.bot.handlers.Handler;
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
                try {
                    this.handler.executeMethod(
                        signUpSession,
                        "submitFirstRole",
                        event
                    );
                } catch (Exception e) {
                    System.out.println("ERROR");
                    e.printStackTrace();
                }
                break;

            case "role2":
                try {
                    this.handler.executeMethod(
                        signUpSession,
                        "submitSecondRole",
                        event
                    );
                } catch (Exception e) {
                    System.out.println("ERROR");
                    e.printStackTrace();
                }
                break;

        }

    }

}
