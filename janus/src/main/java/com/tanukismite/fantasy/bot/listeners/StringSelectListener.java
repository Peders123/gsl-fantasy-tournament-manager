package com.tanukismite.fantasy.bot.listeners;

import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import com.tanukismite.fantasy.bot.commands.slashcommands.CreateSignups;
import com.tanukismite.fantasy.bot.handlers.Handler;


/**
 * The {@code StringSelectListener} class extends {@link BaseListener} and listens for string select 
 * menu interactions within Discord. It handles role selection by invoking appropriate methods in 
 * the {@link CreateSignups} class.
 * 
 * <p><b>Usage:</b> This class processes interactions from string select menus and manages user role 
 * selections during signup sessions.</p>
 * 
 * @see BaseListener
 * 
 * @author Rory Caston
 * @since 1.0
 */
public class StringSelectListener extends BaseListener {

    /**
     * Constructor with a {@link Handler} reference.
     * @param handler The {@link Handler} current app handler.
     */
    public StringSelectListener(Handler handler) {
        super(handler);
    }

    /**
     * Handles the {@link StringSelectInteractionEvent} when a string select menu interaction 
     * occurs in Discord. Depending on the menu's component ID, it calls the corresponding method 
     * in the {@link CreateSignups} class to handle role selection.
     * 
     * @param event The {@link StringSelectInteractionEvent} representing the string select interaction.
     */
    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {

        String id = event.getComponentId();
        CreateSignups signUpSession = this.handler.getContext().getSignupRoot();

        switch (id) {

            case "role1":
                signUpSession.submitFirstRole(this.handler.getContext(), event);
                break;

            case "role2":
                signUpSession.submitSecondRole(handler, event);
                break;

            default:
                BaseListener.notImplemented(event.getChannel());

        }

    }

}
