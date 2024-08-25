package com.tanukismite.fantasy.bot.listeners;

import java.io.IOException;

import com.tanukismite.fantasy.bot.commands.slash_commands.CreateSignups;
import com.tanukismite.fantasy.bot.commands.slash_commands.Edit;
import com.tanukismite.fantasy.bot.communicators.CaptainCommunicator;
import com.tanukismite.fantasy.bot.communicators.PlayerCommunicator;
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

            case "signout":
                this.signout(event);
                break;

            default:
                BaseListener.notImplemented(event.getChannel());

        }

    }

    private void edit(ButtonInteractionEvent event) {

        try {
            Edit.editMessage(event.getMessageChannel(), event.getMessageId());
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e);
        }

    }

    private void delete(ButtonInteractionEvent event) {

        try {
            Edit.deleteMessage(event.getMessageChannel(), event.getMessageId());
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e);
        }

    }

    private void signup(ButtonInteractionEvent event, boolean captain) {

        Long longId = Long.parseLong(event.getUser().getId());

        UserCommunicator userCommunicator = (UserCommunicator) this.handler.getCommunicator("user");
        boolean exists = CreateSignups.checkUserExists(this.handler, longId);
        boolean signupExists = false;

        PlayerCommunicator playerCommunicator = (PlayerCommunicator) this.handler.getCommunicator("player");
        CaptainCommunicator captainCommunicator = (CaptainCommunicator) this.handler.getCommunicator("captain");
        try {
            System.out.println("Player exists:" + Boolean.toString(playerCommunicator.getPlayerUserExists(longId)));
            System.out.println("Captain exists:" + Boolean.toString(captainCommunicator.getCaptainUserExists(longId)));
            signupExists = playerCommunicator.getPlayerUserExists(longId) || captainCommunicator.getCaptainUserExists(longId);
            System.out.println(Boolean.toString(signupExists));
        } catch (IOException e) {
            System.out.println("HANDLE ERROR");
            return;
        }

        if (signupExists) {
            try {
                System.out.println("REFLECTION TESTING");
                this.handler.getContext().getSignupRoot().alreadySignedUp(event);
            } catch (Exception e) {
                System.out.println("ERROR");
                e.printStackTrace();
            }
        }

        if (!exists) {
            try {
                UserSignupData data = new UserSignupData(event.getUser().getId(), event.getUser().getName());
                userCommunicator.post(data);
            } catch (IOException e) {
                System.out.println("HANDLE ERROR");
                return;
            }
        }

        try {
            this.handler.getContext().getSignupRoot().createModal(handler, event, captain);
        } catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
        }

    }

    private void signout(ButtonInteractionEvent event) {

        Long longId = Long.parseLong(event.getUser().getId());

        boolean signupExists = false;

        PlayerCommunicator playerCommunicator = (PlayerCommunicator) this.handler.getCommunicator("player");
        CaptainCommunicator captainCommunicator = (CaptainCommunicator) this.handler.getCommunicator("captain");
        try {
            System.out.println("Player exists:" + Boolean.toString(playerCommunicator.getPlayerUserExists(longId)));
            System.out.println("Captain exists:" + Boolean.toString(captainCommunicator.getCaptainUserExists(longId)));
            signupExists = playerCommunicator.getPlayerUserExists(longId) || captainCommunicator.getCaptainUserExists(longId);
            System.out.println(Boolean.toString(signupExists));
        } catch (IOException e) {
            System.out.println("HANDLE ERROR");
            return;
        }

        if (!signupExists) {
            event.reply("You are not currently signed up.").setEphemeral(true).queue();
        } else {
            try {
                this.handler.getContext().getSignupRoot().signout(handler, event);
            } catch (Exception e) {
                System.out.println("ERROR");
                e.printStackTrace();
            }
        }

    }

}