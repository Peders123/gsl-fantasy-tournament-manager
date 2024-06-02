package com.glaeriasmite.fantasy.bot.commands;

import com.glaeriasmite.fantasy.bot.handlers.Action;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.RestAction;

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

        RestAction<InteractionHook> action = Action.replyWithMessage(event, message);

        action.queue();

    }

    @Override
    public <R> void queue(RestAction<R> request) {

        request.queue();

    }

}