package com.tanukismite.fantasy.bot.commands.slashCommands;

import java.awt.Color;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.tanukismite.fantasy.bot.Role;
import com.tanukismite.fantasy.bot.commands.Context;
import com.tanukismite.fantasy.bot.commands.ExtendedCommand;
import com.tanukismite.fantasy.bot.communicators.MercuryCommunicator;
import com.tanukismite.fantasy.bot.handlers.Action;
import com.tanukismite.fantasy.bot.handlers.Components;
import com.tanukismite.fantasy.bot.handlers.Handler;
import com.tanukismite.fantasy.bot.signup.CaptainSignupData;
import com.tanukismite.fantasy.bot.signup.PlayerSignupData;
import com.tanukismite.fantasy.bot.signup.SignupData;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.requests.FluentRestAction;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ModalCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.internal.entities.channel.concrete.TextChannelImpl;
import net.dv8tion.jda.internal.requests.restaction.MessageCreateActionImpl;

public class CreateSignups extends ExtendedCommand {

    private SlashCommandInteractionEvent event;
    private String signupRootId;
    private String recentMessageId;

    public CreateSignups(SlashCommandInteractionEvent event) {

        this.event = event;
        this.signupRootId = "TESTING ROOT ID";
        this.recentMessageId = null;

    }

    @Override
    public void execute(Handler handler) {

        Context context = handler.getContext();

        MessageChannel channel = event.getMessageChannel();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Embed testing command");
        embed.setDescription("TANUKI SMITE LEAGUE FANTASY TOURNAMENT\n <@365588450881306630>");
        embed.setColor(new Color(28, 19, 31, 255));
        embed.setFooter("August 10th");

        FluentRestAction<InteractionHook, ReplyCallbackAction> response = Action.replyWithMessage(event, "Created!");
        FluentRestAction<Message, MessageCreateAction> action = Action.sendMessageWithEmbed(channel, embed.build());

        action = Components.addActionRowMessage(action,
            Button.primary(event.getUser().getId() + ":player-signup", "Signup"),
            Button.primary(event.getUser().getId() + ":captain-signup", "Signup (Captain)"),
            Button.secondary(event.getUser().getId() + ":players", "Players")
        );

        this.queue(response);
        this.queue(action);

        context.setSignupRoot(this);
 
    }

    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {

        if (request instanceof MessageCreateActionImpl) {
            request.queue((R message) -> {
                if (message instanceof Message) {
                    String msgId = ((Message) message).getId();
                    if (this.recentMessageId == null) {
                        this.signupRootId = msgId;
                    }
                    this.recentMessageId = msgId;
                }
            });
        } else {
            request.queue();
        }

    }

    // RENAME TO BE MORE DESCRIPTIVE OF THE FULL FUNCTION
    protected void createModal(Handler handler, ButtonInteractionEvent buttonEvent, Boolean captain) {

        Context context = handler.getContext();

        System.out.println("BUTTON ID: " + buttonEvent.getUser().getId());
        System.out.println(this.signupRootId);

        if (captain) {
            context.putUserSignupData(buttonEvent.getUser().getId(), new CaptainSignupData(this), CaptainSignupData.class);
        } else {
            context.putUserSignupData(buttonEvent.getUser().getId(), new PlayerSignupData(this), PlayerSignupData.class);
        }

        String modalId = buttonEvent.getUser().getId() + ":signup-modal";

        modalId += captain ? ":captain-signup" : ":player-signup";
        String title = captain ? "Captain Signup" : "Player signup";
        TextInput[] inputs = new TextInput[2];

        inputs[0] = TextInput.create("ign", "Smite IGN", TextInputStyle.SHORT)
                .setPlaceholder("Peders")
                .setMinLength(4)
                .setMaxLength(15)
                .build();

        if (captain) {

            inputs[1] = TextInput.create("reason", "Reasoning", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Why would you like to be a captain?")
                    .setMinLength(20)
                    .setMaxLength(1000)
                    .build();

        } else {

            inputs[1] = TextInput.create("guru", "Smite-guru", TextInputStyle.SHORT)
                    .setPlaceholder("Link to your Smite-guru profile.")
                    .setMinLength(15)
                    .build();

        }

        Modal modal = Action.createModal(modalId, title, inputs);
        FluentRestAction<Void, ModalCallbackAction> action = Action.replyWithModal(buttonEvent, modal);
        this.queue(action);

    }

    protected void submitModal(Handler handler, ModalInteractionEvent modalEvent) {

        Context context = handler.getContext();

        String[] id = modalEvent.getModalId().split(":");

        for (String i : id) {
            System.out.println(i);
        }

        SignupData data = null;

        if (id[2].equals("captain-signup")) {

            data = context.getUserSignupData(modalEvent.getUser().getId(), CaptainSignupData.class);
            CaptainSignupData.class.cast(data).setReason(modalEvent.getValue("reason").getAsString());

            FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithMessage(modalEvent, "SUBMITTED", true);
            this.queue(action);

        } else if (id[2].equals("player-signup")) {

            data = context.getUserSignupData(modalEvent.getUser().getId(), PlayerSignupData.class);
            PlayerSignupData.class.cast(data).setSmiteGuru(modalEvent.getValue("guru").getAsString());

            SelectOption[] options = Components.createSelectOptions("role1");
            StringSelectMenu selection = Components.createSelectMenu("role1", options);
            FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithMessage(modalEvent, "TESTING", true);
            Components.addActionRowReply(action, selection);
    
            this.queue(action);

        }

        data.setId(modalEvent.getUser().getId());
        data.setIGN(modalEvent.getValue("ign").getAsString());


    }

    protected void submitFirstRole(Handler handler, StringSelectInteractionEvent selectEvent) {

        Context context = handler.getContext();

        PlayerSignupData data = context.getUserSignupData(selectEvent.getUser().getId(), PlayerSignupData.class);
        data.setRole1(Role.valueOf(selectEvent.getValues().get(0)));

        FluentRestAction<Message, MessageCreateAction> action = MessageCreateAction.class.cast(Action.sendMessage(selectEvent.getMessageChannel(), "Hello World!")).setMessageReference(this.signupRootId);
        StringSelectMenu selection = Components.createSelectMenu("role2", Components.createSelectOptions("role2"));
        Components.addActionRowMessage(action, selection);

        selectEvent.deferReply(true).queue(
            hook -> {
                hook.deleteOriginal().queue(
                    success -> {
                        selectEvent.getMessage().delete().queue();
                    },
                    error -> System.out.println("ERROR HANDLING")
                );
            },
            error -> System.out.println("OUT ERROR HANDLING")
        );

        this.queue(action);

    }

    protected void submitSecondRole(Handler handler, StringSelectInteractionEvent selectEvent) {

        Context context = handler.getContext();

        PlayerSignupData data = context.getUserSignupData(selectEvent.getUser().getId(), PlayerSignupData.class);
        data.setRole2(Role.valueOf(selectEvent.getValues().get(0)));

        this.submitData(handler, selectEvent.getUser().getId());

        selectEvent.deferReply(true).queue(
            hook -> {
                hook.deleteOriginal().queue(
                    success -> {
                        selectEvent.getMessage().delete().queue();
                    },
                    error -> System.out.println("ERROR HANDLING")
                );
            },
            error -> System.out.println("OUT ERROR HANDLING")
        );

    }

    private void submitData(Handler handler, String userId) {

        Context context = handler.getContext();
        MercuryCommunicator communicator = handler.getCommunicator("player");

        PlayerSignupData data = context.getUserSignupData(userId, PlayerSignupData.class);
        MessageEmbed embed = data.toEmbed();
        FluentRestAction<Message, MessageCreateAction> action = Action.sendMessageWithEmbed(this.event.getMessageChannel(), embed);

        System.out.println(data.toMap().toString());

        try {
            communicator.post(data);
        } catch (IOException e) {
            System.out.println("FAILED TO WRITE PLAYER");
        }

        this.queue(action);

    }

    protected void sendTestMessage(Handler handler, TextChannelImpl channel) {

        try {

            FluentRestAction<Message, MessageCreateAction> action = Action.sendMessage(channel, "Not Yet Implemented");
            this.queue(action);

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static boolean checkUserExists(Handler handler, long user_id) {

        MercuryCommunicator communicator = handler.getCommunicator("user");

        JsonNode response;

        try {
            response = communicator.getDetailed(user_id);
        } catch (IOException e) {
            System.out.println("Could not communicate, please try later.");
            return false;
        }

        if (response == null) {
            return false;
        }

        if (response.findValue("user_id") == null) {
            return false;
        }

        System.out.println(response.toString());

        int received = response.get("user_id").asInt();

        if (received == user_id) {
            return true;
        }

        return false;

    }

    public String toString() {

        return this.signupRootId;

    }

}