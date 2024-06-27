package com.glaeriasmite.fantasy.bot.listeners;

import com.glaeriasmite.fantasy.bot.Role;
import com.glaeriasmite.fantasy.bot.commands.slashCommands.CreateSignups;
import com.glaeriasmite.fantasy.bot.handlers.Handler;
import com.glaeriasmite.fantasy.bot.signup.PlayerSignupData;
import com.glaeriasmite.fantasy.bot.signup.SignupData;

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
        CreateSignups signUpSession = this.handler.getContext().getUserSignupData(userId, PlayerSignupData.class).getSignupRoot();
        SignupData data = this.handler.getContext().getUserSignupData(userId, PlayerSignupData.class);

        switch (id) {

            case "role1":
                data.setRole1(Role.valueOf(result));
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
