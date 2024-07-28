package com.tanukismite.fantasy.bot.commands.slashCommands;

import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.handlers.Action;
import com.tanukismite.fantasy.bot.handlers.Handler;

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
    public void execute(Handler handler) {

        FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithMessage(event, "Pong!");

        action.queue();

    }

    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {

        request.queue();

    }

}