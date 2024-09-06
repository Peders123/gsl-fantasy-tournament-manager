package com.tanukismite.fantasy.bot.commands.slash_commands;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.tanukismite.fantasy.bot.Role;
import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.commands.Context;
import com.tanukismite.fantasy.bot.communicators.CaptainCommunicator;
import com.tanukismite.fantasy.bot.communicators.MercuryCommunicator;
import com.tanukismite.fantasy.bot.communicators.PlayerCommunicator;
import com.tanukismite.fantasy.bot.handlers.Components;
import com.tanukismite.fantasy.bot.handlers.Handler;
import com.tanukismite.fantasy.bot.signup.CaptainSignupData;
import com.tanukismite.fantasy.bot.signup.PlayerSignupData;
import com.tanukismite.fantasy.bot.signup.SignupData;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateSignups implements Command {

    private static final Logger logger = LogManager.getLogger("ConsoleLogger");
    public static final String SIGNUP_STATE_FILE = "/data/janus/mount/janus/saves/signupRoot.ser";

    @JsonIgnore
    private SlashCommandInteractionEvent event;
    @JsonIgnore
    private MessageChannel messageChannel;
    @JsonIgnore
    private User user;

    private int tournamentId;
    private String channelId;
    private String userId;

    @JsonCreator
    public CreateSignups(
        @JsonProperty("channelId") String channelId,
        @JsonProperty("userId") String userId,
        @JsonProperty("tournamentId") int tournamentId) {
        this.channelId = channelId;
        this.userId = userId;
        this.tournamentId = tournamentId;
    }

    public CreateSignups(SlashCommandInteractionEvent event) {

        this.event = event;
        this.tournamentId = event.getOption("tournamentid").getAsInt();

        this.messageChannel = event.getMessageChannel();
        this.channelId = messageChannel.getId();
        this.user = event.getUser();
        this.userId = user.getId();

    }

    @Override
    public void execute(Handler handler) {

        logger.info("Running command: CreateSignups");

        JsonNode node = null;

        try{
            node = handler.getCommunicator("tournament").getDetailed(this.tournamentId);
        } catch (IOException error) {
            logger.error("Error fetching tournament details for ID: {}", this.tournamentId, error);
        }

        Context context = handler.getContext();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(node.get("title").asText());
        embed.setDescription(node.get("description").asText());
        embed.setColor(new Color(28, 19, 31, 255));
        embed.setFooter(CreateSignups.convertDate(node.get("datetime").asText()));

        this.event.reply("Created!").setEphemeral(true).queue();
        MessageCreateAction action = this.messageChannel.sendMessageEmbeds(embed.build());

        action.addActionRow(
            Button.primary(this.user.getId() + ":player-signup", "Player Signup"),
            Button.success(this.user.getId() + ":captain-signup", "Captain Signup"),
            Button.danger(this.user.getId() + ":signout", "Signout")
        );

        action.queue();

        context.setSignupRoot(this);

        this.writeObject();

    }

    public void createModal(Handler handler, ButtonInteractionEvent buttonEvent, boolean captain) {

        Context context = handler.getContext();

        logger.debug("Button ID: {}", buttonEvent.getUser().getId());

        if (captain) {
            if (context.signupDataExists(buttonEvent.getUser().getId())) {
                context.removeUserSignupData(buttonEvent.getUser().getId());
            }
            context.putUserSignupData(buttonEvent.getUser().getId(), new CaptainSignupData(this), CaptainSignupData.class);
        } else {
            if (context.signupDataExists(buttonEvent.getUser().getId())) {
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

        buttonEvent.replyModal(Components.createModal(modalId, title, inputs)).queue();

    }

    public void alreadySignedUp(ButtonInteractionEvent buttonEvent) {

        String message = "You are already signed up with this discord account. If you want to re-do your signup, please first use the sign-out button!";

        buttonEvent.reply(message).setEphemeral(true).queue();

    }

    public void signout(Handler handler, ButtonInteractionEvent buttonEvent) {

        Long longId = Long.parseLong(buttonEvent.getUser().getId());

        PlayerCommunicator playerCommunicator = (PlayerCommunicator) handler.getCommunicator("player");
        CaptainCommunicator captainCommunicator = (CaptainCommunicator) handler.getCommunicator("captain");

        try {
            if (playerCommunicator.getPlayerUserExists(longId)) {
                logger.info("Deleting player from user: {}", longId);
                playerCommunicator.delete(playerCommunicator.getPlayerUser(longId).get("player_id").asInt());
            } else if (captainCommunicator.getCaptainUserExists(longId)) {
                logger.info("Deleting captain from user: {}", longId);
                captainCommunicator.delete(captainCommunicator.getCaptainUser(longId).get("captain_id").asInt());
            }
        } catch (IOException error) {
            logger.error("Error when deleting player/captain: {}", longId, error);
        }

        boolean signupExists = false;

        try {
            signupExists = playerCommunicator.getPlayerUserExists(longId) || captainCommunicator.getCaptainUserExists(longId);
        } catch (IOException error) {
            logger.error("Error determining whether account exists.", error);
            return;
        }

        if (signupExists) {
            logger.warn("User was not deleted when they should have been for user: {}.", longId);
            buttonEvent.reply("Error when deleting user, please contact an admin.").setEphemeral(true).queue();
        } else {
            logger.info("Deleted user signup successfully.");
            buttonEvent.reply("Signup deleted successfully.").setEphemeral(true).queue();
        }

    }

    public void submitModal(Handler handler, ModalInteractionEvent modalEvent) {

        Context context = handler.getContext();

        String[] id = modalEvent.getModalId().split(":");

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
            } catch (IOException error) {
                logger.error("Unable to write Captain to the database for user: {}", modalEvent.getUser().getId(), error);
            }

            modalEvent.reply("SUBMITTED").setEphemeral(true).queue();

        } else if (id[2].equals("player-signup")) {

            data = context.getUserSignupData(modalEvent.getUser().getId(), PlayerSignupData.class);
            PlayerSignupData.class.cast(data).setSmiteGuru(modalEvent.getValue("guru").getAsString());

            data.setId(modalEvent.getUser().getId());
            data.setTournamentId(this.tournamentId);
            data.setIGN(modalEvent.getValue("ign").getAsString());

            SelectOption[] options = Components.createSelectOptions("role1");
            StringSelectMenu selection = Components.createSelectMenu("role1", options);

            modalEvent.reply("Choose your primary role:").setEphemeral(true).addActionRow(selection).queue();

        }

    }

    public void submitFirstRole(Handler handler, StringSelectInteractionEvent selectEvent) {

        Context context = handler.getContext();

        PlayerSignupData data = context.getUserSignupData(selectEvent.getUser().getId(), PlayerSignupData.class);
        data.setRole1(Role.valueOf(selectEvent.getValues().get(0)));

        StringSelectMenu selection = Components.createSelectMenu("role2", Components.createSelectOptions("role2"));

        selectEvent.reply("Choose your secondary role:").setEphemeral(true).addActionRow(selection).queue(
            hook -> selectEvent.getMessage().delete().queue()
        );

    }

    public void submitSecondRole(Handler handler, StringSelectInteractionEvent selectEvent) {

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

        try {
            communicator.post(data);
        } catch (IOException error) {
            logger.error("Failed to write Player to the database for user: {}", userId, error);
        }

        selectEvent.replyEmbeds(embed).setEphemeral(true).queue(
            hook -> selectEvent.getMessage().delete().queue()
        );

    }

    public static boolean checkUserExists(Handler handler, long userId) {

        MercuryCommunicator communicator = handler.getCommunicator("user");

        JsonNode response;

        try {
            response = communicator.getDetailed(userId);
        } catch (IOException e) {
            logger.error("Could not communicate, please try again later.");
            return false;
        }

        if (response == null) {
            return false;
        }

        if (response.findValue("userId") == null) {
            return false;
        }

        logger.info("Found user: {}", response);

        int received = response.get("userId").asInt();

        return received == userId;

    }

    public static String convertDate(String dateString) {

        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("hh:mm a dd/MM/yyyy");
        return zonedDateTime.format(outputFormatter);

    }

    private void writeObject() {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(CreateSignups.SIGNUP_STATE_FILE), this);
            logger.info("Saved current signups to a file successfully.");
        } catch (IOException error) {
            logger.error("Error saving current signups to a file.", error);
        }

    }

    public static CreateSignups readObject() {

        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(CreateSignups.SIGNUP_STATE_FILE);
        if (!file.exists()) {
            return null;
        }

        try {
            return objectMapper.readValue(file, CreateSignups.class);
        } catch (IOException error) {
            logger.error("Error loading saved signups.", error);
            return null;
        }

    }

    public void initialiseNonSerializedFields(JDA jda) {

        this.messageChannel = jda.getTextChannelById(this.channelId);
        this.user = jda.getUserById(this.userId);

    }

    public int getTournamentId() {
        return this.tournamentId;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public String getUserId() {
        return this.userId;
    }

}