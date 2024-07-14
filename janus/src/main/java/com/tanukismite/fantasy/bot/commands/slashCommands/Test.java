package com.tanukismite.fantasy.bot.commands.slashCommands;

import java.lang.reflect.Method;

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

        String replyString = "";

        for (int i = 0; i < event.getOption("iterations").getAsInt(); i++) {
            replyString += event.getOption("content").getAsString() + "\n";
        }

        FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithMessage(event, replyString);

        this.queue(action);

    }

    @Override
    public void executeMethod(String methodName, Handler handler, Object... params) throws Exception {

        Context context = handler.getContext();

        Method method = Test.class.getDeclaredMethod(methodName, Handler.class, Object[].class);
        method.invoke(this, handler, new Object[] {params});

    }

    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {
        
        request.queue();
        
    }

}