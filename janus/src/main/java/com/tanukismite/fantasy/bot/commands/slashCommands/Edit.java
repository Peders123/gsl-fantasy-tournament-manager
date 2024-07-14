package com.tanukismite.fantasy.bot.commands.slashCommands;

import com.tanukismite.fantasy.bot.commands.ExtendedCommand;
import com.tanukismite.fantasy.bot.commands.Context;
import com.tanukismite.fantasy.bot.handlers.Action;
import com.tanukismite.fantasy.bot.handlers.Components;
import com.tanukismite.fantasy.bot.handlers.Handler;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.FluentRestAction;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.internal.entities.channel.concrete.TextChannelImpl;

public class Edit extends ExtendedCommand {

    private SlashCommandInteractionEvent event;

    public Edit(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute(Handler handler) {

        FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithMessage(event, "Testing Buttons");

        action = Components.addActionRowReply(action,
            Button.primary(event.getUser().getId() + ":edit", "EDIT"),
            Button.danger(event.getUser().getId() + ":delete", "DELETE")
        );

        this.queue(action);
 
    }


    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {

        request.queue();

    }

    protected void editMessage(Handler handler, TextChannelImpl channel, String id) {

        try {

            String message = "SUCCESS!";

            RestAction<Message> action = Action.editMessage(channel, id, message);

            action.queue();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    protected void deleteMessage(Handler handler, TextChannelImpl channel, String id) {

        try {

            RestAction<Void> action = Action.deleteMessage(channel, id);

            action.queue();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
