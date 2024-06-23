package com.glaeriasmite.fantasy.bot;

import com.glaeriasmite.fantasy.bot.commands.slashCommands.*;
import com.glaeriasmite.fantasy.bot.handlers.*;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    Handler handler = new Handler();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getMessage().getContentRaw().equals("!ping")) {

            // handler.execute(new Ping(event));
        
        }

    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        switch (event.getName()) {

            case "test":
                handler.execute(new Test(event));
                break;

            case "button":
                handler.execute(new ButtonTest(event));
                break;

            case "ping":
                handler.execute(new Ping(event));
                break;

            case "embed":
                handler.execute(new Embed(event));
                break;

            case "user":
                handler.execute(new User(event));
                break;

            case "signup":
                handler.execute(new Signup(event));
                break;

            case "edit":
                handler.execute(new Edit(event));
                break;

        }

    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        System.out.println(event.getMessageChannel());

        String[] id = event.getComponentId().split(":");
        String authorId = id[0];
        String type = id[1];

        event.deferEdit().queue();

        System.out.println(authorId);

        switch (type) {

            case "test":
                System.out.println("TEST");
                break;

            case "bad":
                System.out.println("BAD");
                break;

            case "edit":
                try{
                    this.handler.executeMethod(
                        new Edit(null),
                        "editMessage",
                        event.getMessageChannel(),
                        event.getMessageId()
                    );
                } catch (Exception e) {
                    System.out.println("ERROR");
                    System.out.println(e);
                }
                break;

        }

    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {

        String id = event.getComponentId();
        String userId = event.getUser().getId();
        String result = event.getValues().get(0);
        Signup signUpSession = this.handler.getContext().getUserSignupData(userId).getSignUpSession();
        SignupData data = this.handler.getContext().getUserSignupData(userId);

        // signUpSession.testMethod(data);

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