package com.tanukismite.fantasy.bot.commands.slashcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.handlers.Handler;


/**
 * A simple ping command to test the connectivity of the bot.
 *
 * <p>This class implements the {@link Command} interface and provides a basic implementation that
 * replies to the user command with the message "Pong!".</p>
 *
 * @author Rory Caston
 * @since 1.0
 */
public class Ping implements Command {

    private SlashCommandInteractionEvent event;

    /**
     * Constructs a {@code Ping} command instace.
     *
     * @param event The {@link SlashCommandInteractionEvent} representing the user interaction that
     *              triggered this command. The event is used to reply to the user command.
     * @see com.tanukismite.fantasy.bot.listeners.SlashCommandListener
     */
    public Ping(SlashCommandInteractionEvent event) {
        this.event = event;
    }

    /**
     * Sends an async reply to the event to show the bot is working.
     *
     * @param handler The {@link Handler} used to manage command execution. It is unused in this
     *                method but is required by the {@link Command} interface.
     */
    @Override
    public void execute(Handler handler) {
        event.reply("Pong!").queue();
    }

}