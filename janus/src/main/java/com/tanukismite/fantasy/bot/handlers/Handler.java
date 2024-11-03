package com.tanukismite.fantasy.bot.handlers;

import java.util.HashMap;

import net.dv8tion.jda.api.JDA;

import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.commands.Context;
import com.tanukismite.fantasy.bot.communicators.MercuryCommunicator;


/**
 * The {@code Handler} class is responsible for managing the execution of {@link Command} objects
 * and coordinating interactions with various {@link MercuryCommunicator} instances. It holds the
 * application context and a registry of communicators that facilitate API interactions with
 * external services. It acts as a central manager for all command execution.
 *
 * @author Rory Caston
 * @since 1.0
 */
public class Handler {

    private Context context;
    private HashMap<String, MercuryCommunicator> communicators;

    /**
     * Constructs a {@code Handler} and intialises the application {@link Context} along with an
     * empty set of {@link MercuryCommunicator} instances.
     *
     * @param jda The current {@link JDA} instance the bot is running on.
     */
    public Handler(JDA jda) {
        this.context = new Context(jda);
        this.communicators = new HashMap<>();
    }

    /**
     * Executes a {@link Command} and passes this handler instance to it, enabling the command to
     * interact with the application's state.
     *
     * @param command The {@link Command} object to be executed.
     */
    public void execute(Command command) {
        command.execute(this);
    }

    /**
     * Getter for the app context.
     *
     * @return The current app {@link Context}.
     */
    public Context getContext() {
        return this.context;
    }

    /**
     * Adds a new communicator type to the handler.
     *
     * @param type         The type of communicator. Eg. "captain", "player", etc.
     * @param communicator The {@link MercuryCommunicator} instance.
     */
    public void addCommunicator(String type, MercuryCommunicator communicator) {
        this.communicators.put(type, communicator);
    }

    /**
     * Gets a specific communicator.
     *
     * @param type The type of communicator to get.
     * @return The {@link MercuryCommunicator} instance.
     */
    public MercuryCommunicator getCommunicator(String type) {
        return this.communicators.get(type);
    }

}