package com.glaeriasmite.fantasy.bot.commands.slashCommands;

import com.glaeriasmite.fantasy.bot.commands.ExtendedCommand;
import com.glaeriasmite.fantasy.bot.commands.Context;
import com.glaeriasmite.fantasy.bot.handlers.Action;
import com.glaeriasmite.fantasy.bot.handlers.Components;

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
    public void execute(Context context) {

        FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithMessage(event, "Testing Buttons");

        action = Components.addActionRowReply(action,
            Button.primary(event.getUser().getId() + ":edit", "EDIT")
        );

        this.queue(action);
 
    }


    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {

        request.queue();

    }

    public void editMessage(Context context, TextChannelImpl channel, String id) {

        System.out.println("IN");

        try {

            String message = "SUCCESS!";

            RestAction<Message> action = Action.editMessage(channel, id, message);

            action.queue();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
