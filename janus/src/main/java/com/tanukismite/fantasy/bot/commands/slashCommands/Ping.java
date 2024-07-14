package com.tanukismite.fantasy.bot.commands.slashCommands;

import java.lang.reflect.Method;

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

        // RestAction<Message> action = Action.sendMessage(event.getChannel(), "ping");

        // this.queue(action);

        String message = event.getUser().getId() + " " + event.getUser().getName();

        FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithMessage(event, message);

        action.queue();

    }

    @Override
    public void executeMethod(String methodName, Handler handler, Object... params) throws Exception {

        Method method = Ping.class.getDeclaredMethod(methodName, Handler.class, Object[].class);
        method.invoke(this, handler, new Object[] {params});

    }

    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {

        request.queue();

    }

}