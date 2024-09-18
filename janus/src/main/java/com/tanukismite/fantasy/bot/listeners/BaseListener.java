package com.tanukismite.fantasy.bot.listeners;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import com.tanukismite.fantasy.bot.handlers.Handler;


/**
 * This class extends {@link ListenerAdapted} and serves as a base class for the other event
 * listeners in the bot. It amangers a reference to the {@link Handler} instance and provides
 * utility methods to simplify event handling.
 * <p>
 * The class contains core functionalities that can be shared across multiple listeners, including
 * the handler references and a utility method to indicate unimplemented features.
 * </p>
 * 
 * <p><b>Usage:</b> This class is intended to be extended by other listener classes that need to
 * access the {@link Handler} and event handling methods.</p>
 *
 * @author Rory Caston
 * @since 1.0
 */
public class BaseListener extends ListenerAdapter {

    protected Handler handler;

    /**
     * Default constructor. Used when a handler is not required at the time of construction.
     */
    public BaseListener() {}

    /**
     * Constructor with a {@link Handler} reference.
     *
     * @param handler The {@link Handler} current app handler.
     */
    public BaseListener(Handler handler) {
        this.handler = handler;
    }

    /**
     * Getter for the handler.
     *
     * @return The current app {@link Handler} instance.
     */
    public Handler getHandler() {
        return this.handler;
    }

    /**
     * Setter for the handler.
     *
     * @param handler The new {@link Handler} instance.
     */
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    /**
     * Sends a message if a user attempts to use a feature that is currently not implemented.
     *
     * @param channel The {@link MessageChannel} where the message will be sent.
     */
    protected static void notImplemented(MessageChannel channel) {
        channel.sendMessage("Not Yet implemented").queue();
    }

}
