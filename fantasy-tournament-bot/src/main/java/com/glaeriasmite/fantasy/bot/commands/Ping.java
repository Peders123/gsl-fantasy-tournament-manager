package com.glaeriasmite.fantasy.bot.commands;

import com.glaeriasmite.fantasy.bot.handlers.Action;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.FluentRestAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class Ping implements Command {

    private SlashCommandInteractionEvent event;

    public Ping(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute() {

        // RestAction<Message> action = Action.sendMessage(event.getChannel(), "ping");

        // this.queue(action);

        String message = event.getUser().getId() + " " + event.getUser().getName();

        FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithMessage(event, message);

        action.queue();

    }

    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {

        request.queue();

    }

}