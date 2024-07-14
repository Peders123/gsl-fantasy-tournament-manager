package com.tanukismite.fantasy.bot.listeners;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.tanukismite.fantasy.bot.commands.slashCommands.CreateSignups;
import com.tanukismite.fantasy.bot.commands.slashCommands.Edit;
import com.tanukismite.fantasy.bot.communicators.UserCommunicator;
import com.tanukismite.fantasy.bot.handlers.Handler;
import com.tanukismite.fantasy.bot.signup.UserSignupData;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class ButtonListener extends BaseListener {

    public ButtonListener(Handler handler) {
        super(handler);
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        String[] id = event.getComponentId().split(":");
        String type = id[1];

        switch (type) {

            case "test":
                System.out.println("TEST");
                break;

            case "bad":
                System.out.println("BAD");
                break;

            case "edit":
                this.edit(event);
                break;

            case "delete":
                this.delete(event);
                break;

            case "player-signup":
                this.signup(event, false);
                break;

            case "captain-signup":
                this.signup(event, true);
                break;

            case "players":
                this.notImplemented(event.getChannel());
                break;

        }

    }

    private void edit(ButtonInteractionEvent event) {

        try {
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

    }

    private void delete(ButtonInteractionEvent event) {

        try {
            this.handler.executeMethod(
                new Edit(null),
                "deleteMessage",
                event.getMessageChannel(),
                event.getMessageId()
            );
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e);
        }

    }

    private void signup(ButtonInteractionEvent event, boolean captain) {

        UserCommunicator userCommunicator = (UserCommunicator) this.handler.getCommunicator("user");
        boolean exists = CreateSignups.checkUserExists(this.handler, Long.parseLong(event.getUser().getId()));

        try {
            JsonNode response = this.handler.getCommunicator("user").get();
            System.out.println(response.toString());
        } catch (IOException e) {
            System.out.println("HANDLE ERROR GET");
            return;
        }

        if (exists == false) {
            try {
                UserSignupData data = new UserSignupData(event.getUser().getId(), event.getUser().getName());
                userCommunicator.post(data);
            } catch (IOException e) {
                System.out.println("HANDLE ERROR");
                return;
            }
        }

        try {
            JsonNode response = userCommunicator.get();
            System.out.println(response.toString());
        } catch (IOException e) {
            System.out.println("HANDLE ERROR GET");
            return;
        }

        try {
            this.handler.executeMethod(
                this.handler.getContext().getSignupRoot(),
                "createModal",
                event,
                captain
            );
        } catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
        }

    }

    private void notImplemented(MessageChannel channel) {

        try {
            this.handler.executeMethod(
                new CreateSignups(null),
                "sendTestMessage",
                channel
            );
        } catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
        }

    }

}