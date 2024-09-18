package com.tanukismite.fantasy.bot.commands;

import com.tanukismite.fantasy.bot.handlers.Handler;


/**
 * An interface for a command within the TSL discord bot.
 * <p>
 * Implementing classes define specific actions or behaviours that shoudl be triggered when the
 * command is invoked. Whenever a command in instantiated, the {@link #execute(Handler)} method
 * should be called.
 * </p>
 * <p>
 * Commands are dealt with by the {@link Handler} which provides the necessary context and
 * resources for executing the command.
 * </p>
 *
 * @author Rory Caston
 * @since 1.0
 */
public interface Command {
    /**
     * Executes the main logic of the command.
     *
     * @param handler The {@link Handler} used to manage the execution context.
     * @see com.tanukismite.fantasy.bot.listeners.SlashCommandListener
     */
    void execute(Handler handler);
}