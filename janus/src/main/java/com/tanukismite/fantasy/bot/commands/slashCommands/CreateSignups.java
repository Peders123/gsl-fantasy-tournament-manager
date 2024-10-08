package com.tanukismite.fantasy.bot.commands.slashCommands;

import java.awt.Color;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.JsonNode;
import com.tanukismite.fantasy.bot.Role;
import com.tanukismite.fantasy.bot.commands.Context;
import com.tanukismite.fantasy.bot.commands.ExtendedCommand;
import com.tanukismite.fantasy.bot.communicators.CaptainCommunicator;
import com.tanukismite.fantasy.bot.communicators.MercuryCommunicator;
import com.tanukismite.fantasy.bot.communicators.PlayerCommunicator;
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
    private int tournamentId;
    private String signupRootId;
    private String recentMessageId;

    public CreateSignups(SlashCommandInteractionEvent event) {

        this.event = event;
        this.signupRootId = "TESTING ROOT ID";
        this.recentMessageId = null;
        this.tournamentId = event.getOption("tournamentid").getAsInt();

    }

    @Override
    public void execute(Handler handler) {

        JsonNode node = null;

        try{
            node = handler.getCommunicator("tournament").getDetailed(this.tournamentId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Context context = handler.getContext();

        MessageChannel channel = event.getMessageChannel();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(node.get("title").asText());
        embed.setDescription(node.get("description").asText());
        embed.setColor(new Color(28, 19, 31, 255));
        embed.setFooter(CreateSignups.convertDate(node.get("datetime").asText()));

        FluentRestAction<InteractionHook, ReplyCallbackAction> response = Action.replyWithMessage(event, "Created!", true);
        FluentRestAction<Message, MessageCreateAction> action = Action.sendMessageWithEmbed(channel, embed.build());

        action = Components.addActionRowMessage(action,
            Button.primary(event.getUser().getId() + ":player-signup", "Player Signup"),
            Button.success(event.getUser().getId() + ":captain-signup", "Captain Signup"),
            Button.danger(event.getUser().getId() + ":signout", "Signout")
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
            if (context.signupDataExists(buttonEvent.getUser().getId()) == true) {
                context.removeUserSignupData(buttonEvent.getUser().getId());
            }
            context.putUserSignupData(buttonEvent.getUser().getId(), new CaptainSignupData(this), CaptainSignupData.class);
        } else {
            if (context.signupDataExists(buttonEvent.getUser().getId()) == true) {
                context.removeUserSignupData(buttonEvent.getUser().getId());
            }
            context.putUserSignupData(buttonEvent.getUser().getId(), new PlayerSignupData(this), PlayerSignupData.class);
        }

        String modalId = buttonEvent.getUser().getId() + ":signup-modal";

        modalId += captain ? ":captain-signup" : ":player-signup";
        String title = captain ? "Captain Signup" : "Player signup";
        TextInput[] inputs;

        
        if (captain) {

            inputs = new TextInput[3];

            inputs[1] = TextInput.create("reason", "Reasoning", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Why would you like to be a captain?")
                .setMinLength(20)
                .setMaxLength(1000)
                .build();

            inputs[2] = TextInput.create("team-name", "Team Name", TextInputStyle.SHORT)
                .setPlaceholder("Please enter your proposed team name.")
                .setMinLength(1)
                .setMaxLength(30)
                .build();

        } else {

            inputs = new TextInput[2];

            inputs[1] = TextInput.create("guru", "Smite-guru", TextInputStyle.SHORT)
                .setPlaceholder("Link to your Smite-guru profile.")
                .setMinLength(15)
                .build();

        }

        inputs[0] = TextInput.create("ign", "Smite IGN", TextInputStyle.SHORT)
            .setPlaceholder("Peders")
            .setMinLength(4)
            .setMaxLength(20)
            .build();


        Modal modal = Action.createModal(modalId, title, inputs);
        FluentRestAction<Void, ModalCallbackAction> action = Action.replyWithModal(buttonEvent, modal);
        this.queue(action);

    }

    protected void alreadySignedUp(Handler handler, ButtonInteractionEvent buttonEvent) {

        String message = "You are already signed up with this discord account. If you want to re-do your signup, please first use the sign-out button!";

        FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithMessage(buttonEvent, message, true);
        this.queue(action);

    }

    protected void signout(Handler handler, ButtonInteractionEvent buttonEvent) {

        Long longId = Long.parseLong(buttonEvent.getUser().getId());

        PlayerCommunicator playerCommunicator = (PlayerCommunicator) handler.getCommunicator("player");
        CaptainCommunicator captainCommunicator = (CaptainCommunicator) handler.getCommunicator("captain");

        try {
            if (playerCommunicator.getPlayerUserExists(longId)) {
                System.out.println("DELETING PLAYER");
                playerCommunicator.delete(playerCommunicator.getPlayerUser(longId).get("player_id").asInt());
            } else if (captainCommunicator.getCaptainUserExists(longId)) {
                System.out.println(tournamentId);
                captainCommunicator.delete(captainCommunicator.getCaptainUser(longId).get("captain_id").asInt());
            }
        } catch (IOException e) {
            System.out.println("ERROR");
            e.printStackTrace();
        }
        
        boolean signupExists = false;

        try {
            System.out.println("Player exists:" + Boolean.toString(playerCommunicator.getPlayerUserExists(longId)));
            System.out.println("Captain exists:" + Boolean.toString(captainCommunicator.getCaptainUserExists(longId)));
            signupExists = playerCommunicator.getPlayerUserExists(longId) || captainCommunicator.getCaptainUserExists(longId);
            System.out.println(Boolean.toString(signupExists));
        } catch (IOException e) {
            System.out.println("HANDLE ERROR");
            return;
        }

        if (signupExists == true) {
            Action.replyWithMessage(buttonEvent, "Error when deleting user, please contact an admin.", true).queue();
        } else {
            Action.replyWithMessage(buttonEvent, "Signup deleted successfully.", true).queue();
        }

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
            CaptainSignupData.class.cast(data).setTeamName(modalEvent.getValue("team-name").getAsString());

            data.setId(modalEvent.getUser().getId());
            data.setTournamentId(this.tournamentId);
            data.setIGN(modalEvent.getValue("ign").getAsString());

            try {
                handler.getCommunicator("captain").post(CaptainSignupData.class.cast(data));
            } catch (IOException e) {
                System.out.println("ERROR");
                e.printStackTrace();
            }

            FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithMessage(modalEvent, "SUBMITTED", true);
            this.queue(action);

        } else if (id[2].equals("player-signup")) {

            data = context.getUserSignupData(modalEvent.getUser().getId(), PlayerSignupData.class);
            PlayerSignupData.class.cast(data).setSmiteGuru(modalEvent.getValue("guru").getAsString());

            data.setId(modalEvent.getUser().getId());
            data.setTournamentId(this.tournamentId);
            data.setIGN(modalEvent.getValue("ign").getAsString());

            SelectOption[] options = Components.createSelectOptions("role1");
            StringSelectMenu selection = Components.createSelectMenu("role1", options);
            FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithMessage(modalEvent, "Choose your primary role:", true);
            Components.addActionRowReply(action, selection);
    
            this.queue(action);

        }

    }

    protected void submitFirstRole(Handler handler, StringSelectInteractionEvent selectEvent) {

        Context context = handler.getContext();

        PlayerSignupData data = context.getUserSignupData(selectEvent.getUser().getId(), PlayerSignupData.class);
        data.setRole1(Role.valueOf(selectEvent.getValues().get(0)));

        FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithMessage(selectEvent, "Choose your secondary role:", true);
        StringSelectMenu selection = Components.createSelectMenu("role2", Components.createSelectOptions("role2"));
        Components.addActionRowReply(action, selection);

        action.queue(
            hook -> {
                selectEvent.getMessage().delete().queue();
            },
            error -> System.out.println("OUT ERROR HANDLING")
        );

    }

    protected void submitSecondRole(Handler handler, StringSelectInteractionEvent selectEvent) {

        Context context = handler.getContext();

        PlayerSignupData data = context.getUserSignupData(selectEvent.getUser().getId(), PlayerSignupData.class);
        data.setRole2(Role.valueOf(selectEvent.getValues().get(0)));

        this.submitData(handler, selectEvent.getUser().getId(), selectEvent);

    }

    private void submitData(Handler handler, String userId, StringSelectInteractionEvent selectEvent) {

        Context context = handler.getContext();
        MercuryCommunicator communicator = handler.getCommunicator("player");

        PlayerSignupData data = context.getUserSignupData(userId, PlayerSignupData.class);
        MessageEmbed embed = data.toEmbed();
        FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithEmbeds(selectEvent, embed, true);

        System.out.println(data.toMap().toString());

        try {
            communicator.post(data);
        } catch (IOException e) {
            System.out.println("FAILED TO WRITE PLAYER");
        }

        action.queue(
            hook -> {
                selectEvent.getMessage().delete().queue();
            },
            error -> System.out.println("OUT ERROR HANDLING")
        );

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

    public static String convertDate(String dateString) {

        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("hh:mm a dd/MM/yyyy");
        return zonedDateTime.format(outputFormatter);

    }

    public String toString() {

        return this.signupRootId;

    }

}