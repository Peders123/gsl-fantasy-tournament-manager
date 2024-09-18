package com.tanukismite.fantasy.bot.listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import com.tanukismite.fantasy.bot.commands.slashcommands.CreateSignups;
import com.tanukismite.fantasy.bot.commands.slashcommands.CreateTournament;
import com.tanukismite.fantasy.bot.commands.slashcommands.Ping;
import com.tanukismite.fantasy.bot.handlers.Handler;


/**
 * The {@code SlashCommandListener} class extends {@link BaseListener} and listens for slash commands 
 * within Discord. It handles various commands like {@code /ping}, {@code /create-signups}, and 
 * {@code /create-tournament} by executing the appropriate command handler.
 * 
 * <p><b>Usage:</b> This class processes Discord slash commands and invokes the respective command 
 * classes to handle the bot's functionality for each command.</p>
 * 
 * @see BaseListener
 *
 * @author Rory Caston
 * @since 1.0
 */
public class SlashCommandListener extends BaseListener {

    /**
     * Constructor with a {@link Handler} reference.
     *
     * @param handler The {@link Handler} current app handler.
     */
    public SlashCommandListener(Handler handler) {
        super(handler);
    }

    /**
     * Handles the {@link SlashCommandInteractionEvent} when a slash command is used in Discord.
     * Based on the command name, it executes the corresponding command class.
     *
     * @param event The {@link SlashCommandInteractionEvent} representing the slash command interaction.
     */
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        switch (event.getName()) {

            case "ping":
                this.handler.execute(new Ping(event));
                break;

            case "create-signups":
                this.handler.execute(new CreateSignups(event));
                break;

            case "create-tournament":
                this.handler.execute(new CreateTournament(event));
                break;

            default:
                BaseListener.notImplemented(event.getChannel());

        }

    }

}
