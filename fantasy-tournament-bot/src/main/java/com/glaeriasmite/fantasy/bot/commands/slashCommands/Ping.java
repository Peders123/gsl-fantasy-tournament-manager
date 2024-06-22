package com.glaeriasmite.fantasy.bot.commands.slashCommands;

import java.lang.reflect.Method;

import com.glaeriasmite.fantasy.bot.commands.Command;
import com.glaeriasmite.fantasy.bot.commands.Context;
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
    public void execute(Context context) {

        // RestAction<Message> action = Action.sendMessage(event.getChannel(), "ping");

        // this.queue(action);

        String message = event.getUser().getId() + " " + event.getUser().getName();

        FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithMessage(event, message);

        action.queue();

    }

    @Override
    public void executeMethod(String methodName, Context context, Object... params) throws Exception {

        Method method = Ping.class.getDeclaredMethod(methodName, Context.class, Object[].class);
        method.invoke(this, context, new Object[] {params});

    }

    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {

        request.queue();

    }

}