package com.tanukismite.fantasy.bot.commands.slashCommands;

import java.io.IOException;

import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.commands.Context;
import com.tanukismite.fantasy.bot.handlers.Action;
import com.tanukismite.fantasy.bot.handlers.Handler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.FluentRestAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class Test implements Command {

    private SlashCommandInteractionEvent event;

    public Test(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute(Handler handler) {

        Context context = handler.getContext();
        String msg;

        try {
            context.write();
            msg = "Written";
        } catch (IOException e) {
            e.printStackTrace();
            msg = "Failed";
        }

        FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithMessage(event, msg);
        this.queue(action);

    }

    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {
        
        request.queue();
        
    }

}