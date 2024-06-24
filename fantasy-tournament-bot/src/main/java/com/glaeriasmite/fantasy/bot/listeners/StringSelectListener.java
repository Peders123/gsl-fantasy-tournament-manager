package com.glaeriasmite.fantasy.bot.listeners;

import com.glaeriasmite.fantasy.bot.Role;
import com.glaeriasmite.fantasy.bot.SignupData;
import com.glaeriasmite.fantasy.bot.commands.slashCommands.Signup;
import com.glaeriasmite.fantasy.bot.handlers.Handler;

import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

public class StringSelectListener extends BaseListener {

    public StringSelectListener(Handler handler) {
        super(handler);
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {

        String id = event.getComponentId();
        String userId = event.getUser().getId();
        String result = event.getValues().get(0);
        Signup signUpSession = this.handler.getContext().getUserSignupData(userId).getSignUpSession();
        SignupData data = this.handler.getContext().getUserSignupData(userId);

        switch (id) {

            case "role1":
                this.handler.getContext().getUserSignupData(userId).setRole1(Role.valueOf(result));
                try {
                    this.handler.executeMethod(
                        signUpSession,
                        "testMethod",
                        data
                    );
                } catch (Exception e) {
                    System.out.println("ERROR");
                    System.out.println(e);
                }
                break;

            case "role2":
                break;

        }

    }

}
