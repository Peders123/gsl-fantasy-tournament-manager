package com.tanukismite.fantasy.bot.commands.slashcommands;

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
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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


/**
 * Handles the creation and management of user sign ups to a tournament.
 * <p>
 * This class implements the {@link Command} interface to facilitate the creation of sign ups to a
 * Tanuki Smite League tournament. The initial slash command takes in a single parameter,
 * "tournamentId", which determines which tournament in the database players are signing up to. The
 * slash command is only usable by admins. An embed will then be created with buttons attached
 * allowing regular users to interact sign in or out of the tournament.
 * </p>
 * <p>
 * The sign ups are created by making an HTTP call to an API which interacts with the project
 * database. Methods are also provided for saving and loading the state of the sign ups from a file
 * as the bot may restart for updates during the sign up period, but we still want the original
 * message embed usable without the need to recreate it. This functionality is currently only
 * supported by one instance at a time, not a huge issue as the project is still small scale, but
 * will be increated in the future.
 * </p>
 * 
 * <p>
 * @author Rory Caston
 * @since 1.0
 * </p>
 */
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

    /**
     * Constructs a {@code CreateSignups} instance from saved data.
     * <p>
     * This constructor is used for deserialisation. The variables channelId, userId and
     * tournamentId are the only ones loaded as they are the only ones serialisable. The other
     * attributes are derived and assigned elsewhere.
     * </p>
     * 
     * @param channelId    The channel where sign ups are managed.
     * @param userId       The id of the user who initiated the sign up process.
     * @param tournamentId The tournament for which users are signing up.
     */
    @JsonCreator
    public CreateSignups(
        @JsonProperty("channelId") String channelId,
        @JsonProperty("userId") String userId,
        @JsonProperty("tournamentId") int tournamentId
    ) {
        this.channelId = channelId;
        this.userId = userId;
        this.tournamentId = tournamentId;
    }

    /**
     * Constructs a {@code CreateSignups} command instace.
     * <p>
     * This constructor initialises the object based on the provided slash command interaction. The
     * necessary information, such as the tournamentId or the message channel, is extracted from
     * this event.
     * </p>
     * 
     * @param event The {@link SlashCommandInteractionEvent} representing the user interaction that
     *              triggered the creation of a sign up event.
     * @see com.tanukismite.fantasy.bot.listeners.SlashCommandListener
     */
    public CreateSignups(SlashCommandInteractionEvent event) {

        this.event = event;
        this.tournamentId = event.getOption("tournamentid").getAsInt();

        this.messageChannel = event.getMessageChannel();
        this.channelId = messageChannel.getId();
        this.user = event.getUser();
        this.userId = user.getId();

    }

    /**
     * Executes the command by creating the base sign up embed.
     * <p>
     * The method aims to initialise the user sign ups by creating the base embed all users
     * interact with to create their sign ups. First, a communicator grabs details about the
     * specific tournament. Using this data, the embed is initialised with relevant information.
     * The buttons are attached and the request is queued. The sign up root is assigned to this
     * class as this will be used in other parts of the program to ensure the methods are being
     * executed on this specific sign up instance. After creation, the details are serialised.
     * </p>
     * 
     * @param handler The {@link Handler} used to manage command execution. Here it is used to
     *                gather tounament information, as well as track the state of the signup.
     */
    @Override
    public void execute(Handler handler) {

        logger.info("Running command: CreateSignups");

        // Get tournament details.
        JsonNode node = null;
        try {
            node = handler.getCommunicator("tournament").getDetailed(this.tournamentId);
        } catch (IOException error) {
            logger.error("Error fetching tournament details for ID: {}", this.tournamentId, error);
        }

        // Create the embed.
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(node.get("title").asText());
        embed.setDescription(node.get("description").asText());
        embed.setColor(new Color(28, 19, 31, 255));
        embed.setFooter(CreateSignups.convertDate(node.get("datetime").asText()));

        // Acknowledge the initial command.
        this.event.reply("Created!").setEphemeral(true).queue();

        // Send the embed to channel.
        this.messageChannel.sendMessageEmbeds(
            embed.build()
        ).addActionRow(
            Button.primary(this.user.getId() + ":player-signup", "Player Signup"),
            Button.success(this.user.getId() + ":captain-signup", "Captain Signup"),
            Button.danger(this.user.getId() + ":signout", "Signout")
        ).queue();

        // Register instance in the relevant places.
        handler.getContext().setSignupRoot(this);
        this.writeObject();

    }

    /**
     * Creates and sends a modal to get user sign up information.
     * <p>
     * This method creates a modal to gather the necessary sign up information from the user. The
     * fields present in this modal will change depending on if the user is signing up as a captain
     * or a standard player.
     * </p>
     * <p>
     * This method is triggered when the user interacts with one of the sign
     * up buttons on the previously mentioned embed. The method will initialise a saved sign up
     * state for the user in the current {@link Context}, allowing the data to be sent between
     * different methods.
     * </p>
     * 
     * @param context     The {@link Context} used to retain data for the sign up process.
     * @param buttonEvent The {@link ButtonInteractionEvent} representing the button press that
     *                    triggered a sign up request.
     * @param captain     {@code true} if the sign up is for a captain; {@code false} if otherwise.
     * @see com.tanukismite.fantasy.bot.listeners.ButtonListener
     */
    public void createModal(Context context, ButtonInteractionEvent buttonEvent, boolean captain) {

        logger.debug("Button ID: {}", buttonEvent.getUser().getId());

        // Initialises the user sign up data in the Context.
        if (captain) {
            if (context.signupDataExists(buttonEvent.getUser().getId())) {
                context.removeUserSignupData(buttonEvent.getUser().getId());
            }
            context.putUserSignupData(
                buttonEvent.getUser().getId(),
                new CaptainSignupData(this),
                CaptainSignupData.class
            );
        } else {
            if (context.signupDataExists(buttonEvent.getUser().getId())) {
                context.removeUserSignupData(buttonEvent.getUser().getId());
            }
            context.putUserSignupData(
                buttonEvent.getUser().getId(),
                new PlayerSignupData(this),
                PlayerSignupData.class
            );
        }

        // Defining modal data
        String modalId = buttonEvent.getUser().getId() + ":signup-modal";
        modalId += captain ? ":captain-signup" : ":player-signup";
        String title = captain ? "Captain Signup" : "Player signup";
        TextInput[] inputs;

        if (captain) {

            // Modal fields for a captain sign up.
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

            // Modal fields for a player sign up.
            inputs = new TextInput[2];
            inputs[1] = TextInput.create("guru", "Smite-guru", TextInputStyle.SHORT)
                .setPlaceholder("Link to your Smite-guru profile.")
                .setMinLength(15)
                .build();

        }

        // Shared modal field for either sign up type.
        inputs[0] = TextInput.create("ign", "Smite IGN", TextInputStyle.SHORT)
            .setPlaceholder("Peders")
            .setMinLength(4)
            .setMaxLength(20)
            .build();

        buttonEvent.replyModal(Components.createModal(modalId, title, inputs)).queue();

    }

    /**
     * Sends an error for if the user tries to sign up multiple times.
     * 
     * @param buttonEvent The {@link ButtonInteractionEvent} that caused this error.
     */
    public void alreadySignedUp(ButtonInteractionEvent buttonEvent) {

        String message = "You are already signed up with this discord account."
                         + "If you want to re-do your signup, please first use the sign-out button!";
        buttonEvent.reply(message).setEphemeral(true).queue();

    }

    /**
     * Signs out a user from the tournament.
     * <p>
     * This method is triggered if a user clicks the 'Signout' button on the base embed.This
     * method checks whether or not a user is signed up either as a player or a captain. If so
     * their record is deleted from the database. This is all achieved using the Communicators to
     * make API calls. A response is made to the user to let them know if the deletion was
     * successful.
     * </p>
     * 
     * @param handler     The current {@link Handler} which provides the communicators to make
     *                    calls to the API.
     * @param buttonEvent The {@link ButtonInteractionEvent} representing the button press that
     *                    triggered the signout request.
     * @see com.tanukismite.fantasy.bot.listeners.ButtonListener
     */
    public void signout(Handler handler, ButtonInteractionEvent buttonEvent) {

        Long longId = Long.parseLong(buttonEvent.getUser().getId());

        // Determine if the user is sign up as either a player or a captain.
        PlayerCommunicator playerCommunicator = (PlayerCommunicator) handler.getCommunicator("player");
        CaptainCommunicator captainCommunicator = (CaptainCommunicator) handler.getCommunicator("captain");
        boolean playerExists;
        boolean captainExists;
        try {
            playerExists = playerCommunicator.getPlayerUserExists(longId);
            captainExists = captainCommunicator.getCaptainUserExists(longId);
        } catch (IOException error) {
            logger.error("Error determining whether account exists.", error);
            return;
        }

        // If the sign up exists, delete it from the database.
        try {
            if (playerExists) {
                logger.info("Deleting player from user: {}", longId);
                playerCommunicator.delete(playerCommunicator.getPlayerUser(longId).get("player_id").asInt());
            } else if (captainExists) {
                logger.info("Deleting captain from user: {}", longId);
                captainCommunicator.delete(captainCommunicator.getCaptainUser(longId).get("captain_id").asInt());
            }
        } catch (IOException error) {
            logger.error("Error when deleting player/captain: {}", longId, error);
        }

        // Get the user sign up status once again.
        try {
            playerExists = playerCommunicator.getPlayerUserExists(longId);
            captainExists = captainCommunicator.getCaptainUserExists(longId);
        } catch (IOException error) {
            logger.error("Error determining whether account exists.", error);
            return;
        }

        boolean signupExists = playerExists || captainExists;

        // Verify if sign up was deleted.
        if (signupExists) {
            logger.warn("User was not deleted when they should have been for user: {}.", longId);
            buttonEvent.reply("Error when deleting user, please contact an admin.").setEphemeral(true).queue();
        } else {
            logger.info("Deleted user signup successfully.");
            buttonEvent.reply("Signup deleted successfully.").setEphemeral(true).queue();
        }

    }

    /**
     * Gathers and handles the data the user submitted in the sign up modal.
     * <p>
     * The data is processed differently depending on if the user is signing up as a captain or as
     * a player. If the user is a captain, the steps are finished and the data is send to the API
     * via a POST and written to the database. If the user is signing up as a player, the modal
     * data is saved and the method builds a select menu which is used for the next step.
     * 
     * @param handler    The {@link Handler} used to managed the state of the Commands. Here it is
     *                   used to retrieve the Context for the sign up data, as well as communciate
     *                   with the API.
     * @param modalEvent The {@link ModalInteractionEvent} caused by the sign up modal submission.
     * @see com.tanukismite.fantasy.bot.listeners.ModalListener
     */
    public void submitModal(Handler handler, ModalInteractionEvent modalEvent) {

        Context context = handler.getContext();
        String[] id = modalEvent.getModalId().split(":");

        // If the user is signing up as a captain.
        if (id[2].equals("captain-signup")) {

            CaptainSignupData data = context.getUserSignupData(modalEvent.getUser().getId(), CaptainSignupData.class);

            // Sets the sign up data.
            data.setReason(modalEvent.getValue("reason").getAsString());
            data.setTeamName(modalEvent.getValue("team-name").getAsString());
            data.setId(modalEvent.getUser().getId());
            data.setTournamentId(this.tournamentId);
            data.setIgn(modalEvent.getValue("ign").getAsString());

            // Submits the captain sign up.
            try {
                handler.getCommunicator("captain").post(data);
            } catch (IOException error) {
                logger.error(
                    "Unable to write Captain to the database for user: {}",
                    modalEvent.getUser().getId(),
                    error
                );
            }

            // Acknowledge the modal event.
            modalEvent.reply("SUBMITTED").setEphemeral(true).queue();

        // If the user is signing up as a player.
        } else if (id[2].equals("player-signup")) {

            PlayerSignupData data = context.getUserSignupData(modalEvent.getUser().getId(), PlayerSignupData.class);

            // Sets the sign up data.
            data.setSmiteGuru(modalEvent.getValue("guru").getAsString());
            data.setId(modalEvent.getUser().getId());
            data.setTournamentId(this.tournamentId);
            data.setIgn(modalEvent.getValue("ign").getAsString());

            // Builds and sends the select menu.
            modalEvent.reply("Choose your primary role:").setEphemeral(true).addActionRow(
                Components.createSelectMenu("role1", Components.createSelectOptions("role1"))
            ).queue();

        }

    }

    /**
     * Processes the player's selection of their primary role.
     * <p>
     * Once the user has selected their primary role from the selection menu, this method stores
     * that information in the context and sends another selection menu to select the player's
     * secondary role. When the message is sent, the original selection message is deleted.
     * </p>
     * 
     * @param context     The {@link Context} which stores the user's current sign up data.
     * @param selectEvent The {@link StringSelectInteractionEvent} triggered by the user selecting
     *                    their primary role.
     * @see com.tanukismite.fantasy.bot.listeners.StringSelectListener
     */
    public void submitFirstRole(Context context, StringSelectInteractionEvent selectEvent) {

        // Saves the data.
        context.getUserSignupData(
            selectEvent.getUser().getId(),
            PlayerSignupData.class
        ).setRole1(
            Role.valueOf(selectEvent.getValues().get(0))
        );

        // Builds and sends the selection menu. When queued it deletes the previous selection menu.
        selectEvent.reply("Choose your secondary role:").setEphemeral(true).addActionRow(
            Components.createSelectMenu("role2", Components.createSelectOptions("role2"))
        ).queue(
            hook -> selectEvent.getMessage().delete().queue()
        );

    }

    /**
     * Processes the player's selection of their secondary role.
     * <p>
     * After the user selects their secondary role, this method stores that information in the
     * context and finalises the sign up process. It sends the data to the method to finish the
     * data submission.
     * </p>
     * 
     * @param handler     The {@link Handler} which contains the context. Forwarded on to the final
     *                    submission method.
     * @param selectEvent The {@link StringSelectInteractionEvent} triggered by the user selecting
     *                    their secondary role.
     * @see com.tanukismite.fantasy.bot.listeners.StringSelectListener
     */
    public void submitSecondRole(Handler handler, StringSelectInteractionEvent selectEvent) {

        // Saves the data.
        handler.getContext().getUserSignupData(
            selectEvent.getUser().getId(),
            PlayerSignupData.class
        ).setRole2(
            Role.valueOf(selectEvent.getValues().get(0))
        );

        this.submitData(handler, selectEvent.getUser().getId(), selectEvent);

    }

    /**
     * Submits the player's sign-up data to the API.
     * <p>
     * This method handles the final submission of the player sign-up data after both the primary
     * and secondary roles have been selected. After submission, and embed containing the data is
     * also sent to the user and the previous selection menu is deleted.
     * </p>
     * 
     * @param handler
     * @param userId
     * @param selectEvent
     */
    private void submitData(Handler handler, String userId, StringSelectInteractionEvent selectEvent) {

        MercuryCommunicator communicator = handler.getCommunicator("player");

        PlayerSignupData data = handler.getContext().getUserSignupData(userId, PlayerSignupData.class);
        MessageEmbed embed = data.toEmbed();

        // Submit data to API.
        try {
            communicator.post(data);
        } catch (IOException error) {
            logger.error("Failed to write Player to the database for user: {}", userId, error);
        }

        // Send embed and delete previous selection menu.
        selectEvent.replyEmbeds(embed).setEphemeral(true).queue(
            hook -> selectEvent.getMessage().delete().queue()
        );

    }

    /**
     * Checks if the user exists in the database.
     * <p>
     * This method is not intended to be used internally when written. However, it is used in an
     * external class in reference to the user sign-up process so its scope falls within this
     * class. The method sends a query to the API with the user ID to see if the record exists.
     * </p>
     * 
     * @param communicator The {@link MercuryCommunicator} for a user to communicate with the API.
     * @param userId       The ID of the user to check in the database.
     * @return {@code true} if the user exists, otherwise {@code false}.
     */
    public static boolean checkUserExists(MercuryCommunicator communicator, long userId) {

        JsonNode response;

        // Get the user data.
        try {
            response = communicator.getDetailed(userId);
        } catch (IOException e) {
            logger.error("Could not communicate, please try again later.");
            return false;
        }

        if (response == null || response.findValue("userId") == null) {
            return false;
        }

        logger.info("Found user: {}", response);

        return response.get("userId").asInt() == userId;

    }

    /**
     * Converts an ISO 8601 date string to a more readable format.
     * <p>
     * This method takes in a date string formatted as ISO 8601 and converts it to the format
     * {@code hh:mm a dd/MM/yyyy}, making it easier to read for users. This is also the form
     * accepted by the API.
     * </p>
     * 
     * @param dateString The ISO 8601 formatted date string.
     * @return A formatted string representing the date in a readable format.
     */
    public static String convertDate(String dateString) {

        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("hh:mm a dd/MM/yyyy");
        return zonedDateTime.format(outputFormatter);

    }

    /**
     * Serializes the current instance of the sign-up data to a file.
     * <p>
     * This method serialises the state of the current sign-up process to a file. It is used to
     * persist the sign-up state between restarts of the bot, ensuring that the sign-up information
     * is not lost.
     * </p>
     */
    private void writeObject() {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(CreateSignups.SIGNUP_STATE_FILE), this);
            logger.info("Saved current signups to a file successfully.");
        } catch (IOException error) {
            logger.error("Error saving current signups to a file.", error);
        }

    }

    /**
     * Reads and deserialises the saved sign-up state from a file.
     * <p>
     * This method loads a previously saved sign-up instance from a file. It is used to restore the
     * sign-up state after the bot has restarted. If no saved state is found, the method returns
     * {@code null}.
     * </p>
     * 
     * @return The deserialized {@code CreateSignups} instance, or {@code null} if no saved state
     *         exists.
     */
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

    /**
     * Initializes non-serialisable fields.
     * <p>
     * This method initializes fields that could not be serialised, such as {@link MessageChannel}
     * and {@link User}, which require access to the JDA object. It retrieves these fields using the
     * saved channel ID and user ID.
     * </p>
     * 
     * @param jda The {@link JDA} instance used to re-initialize non-serializable fields.
     */
    public void initialiseNonSerializedFields(JDA jda) {

        this.messageChannel = jda.getTextChannelById(this.channelId);
        this.user = jda.getUserById(this.userId);

    }

    /**
     * Getter for tournamentId.
     * @return tournamentId.
     */
    public int getTournamentId() {
        return this.tournamentId;
    }

    /**
     * Getter for channelId.
     * @return channelId.
     */
    public String getChannelId() {
        return this.channelId;
    }

    /**
     * Getter for userId.
     * @return userId.
     */
    public String getUserId() {
        return this.userId;
    }

}