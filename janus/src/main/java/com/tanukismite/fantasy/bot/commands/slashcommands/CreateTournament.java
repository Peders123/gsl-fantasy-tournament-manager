package com.tanukismite.fantasy.bot.commands.slashcommands;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.handlers.Components;
import com.tanukismite.fantasy.bot.handlers.Handler;
import com.tanukismite.fantasy.bot.signup.TournamentData;


/**
 * Handles the creation of a new tournament within the Tanuki Smite League.
 * <p>
 * This class implements the {@link Command} interface to facilitate the creation of a new tournament.
 * The initial slash command triggers a modal interaction where the user provides details about the
 * tournament such as date, title, and description. The modal interaction is then processed to store
 * the tournament information.
 * </p>
 * 
 * <p>
 * The class utilizes JDA's {@link SlashCommandInteractionEvent} to initiate the creation process and
 * {@link ModalInteractionEvent} to handle the submission of the tournament details. The tournament
 * data is sent to a specified communicator for storage, and a confirmation message with the tournament
 * details is sent back to the user.
 * </p>
 *
 * @author Rory Caston
 * @since 1.0
 */
public class CreateTournament implements Command {

    private static final Logger logger = LogManager.getLogger("ConsoleLogger");
    
    private SlashCommandInteractionEvent event;

    /**
     * Constructs a {@code CreateTournament} command instance.
     * <p>
     * </p>
     *
     * @param event The {@link SlashCommandInteractionEvent} representing the user's interaction
     *              that triggered the creation of the tournament.
     */
    public CreateTournament(SlashCommandInteractionEvent event) {
        this.event = event;
    }

    /**
     * Executes the command by initiating the tournament creation process.
     * <p>
     * This method triggers the display of a modal to the user, prompting them to enter details about
     * the new tournament, including the date, title, and description. The modal fields are configured
     * and presented to the user to collect the necessary information.
     * </p>
     *
     * @param handler The {@link Handler} used to manage command execution. It is utilized to set
     *                the context for the tournament creation process.
     */
    @Override
    public void execute(Handler handler) {

        TextInput[] inputs = new TextInput[3];

        // Creating the modal inputs.
        inputs[0] = TextInput.create("datetime", "Date", TextInputStyle.SHORT)
            .setPlaceholder("%Y-%m-%dT%D:%M:%SZ")
            .setMinLength(10)
            .setMaxLength(30)
            .build();
        inputs[1] = TextInput.create("title", "Title", TextInputStyle.SHORT)
            .setPlaceholder("Tanuki Smite League - Fantasy Tournament")
            .setMinLength(5)
            .setMaxLength(100)
            .build();
        inputs[2] = TextInput.create("description", "Description", TextInputStyle.PARAGRAPH)
            .setPlaceholder("Brief summary of the tournament")
            .setMinLength(40)
            .setMaxLength(1000)
            .build();

        event.replyModal(Components.createModal(
            event.getUser().getId() + ":tournament-modal",
            "Create Tournament",
            inputs
        )).queue();

        handler.getContext().setTournamentRoot(this);

    }

    /**
     * Processes the submission of the tournament creation modal.
     * <p>
     * This method handles the data submitted through the modal, constructs a {@link TournamentData}
     * object with the provided details, and sends it to the database via the communicator. A response
     * embed with the tournament details is then sent back to the user.
     * </p>
     *
     * @param handler    The {@link Handler} used to manage command execution. It is utilized to get
     *                   the communicator for sending the tournament data.
     * @param modalEvent The {@link ModalInteractionEvent} representing the modal submission event
     *                   containing the tournament details.
     */
    public void submitModal(Handler handler, ModalInteractionEvent modalEvent) {

        TournamentData data = new TournamentData();

        data.setDateTime(modalEvent.getValue("datetime").getAsString());
        data.setTitle(modalEvent.getValue("title").getAsString());
        data.setDescription(modalEvent.getValue("description").getAsString());
        
        try {
            handler.getCommunicator("tournament").post(data);
        } catch (Exception error) {
            logger.error("Unable to write tournament to database", error);
        }

        modalEvent.replyEmbeds(data.toEmbed()).queue();

    }

}
