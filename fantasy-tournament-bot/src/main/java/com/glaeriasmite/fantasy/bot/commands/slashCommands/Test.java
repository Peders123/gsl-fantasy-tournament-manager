package com.glaeriasmite.fantasy.bot.commands.slashCommands;

import java.lang.reflect.Method;

import com.glaeriasmite.fantasy.bot.commands.Command;
import com.glaeriasmite.fantasy.bot.commands.Context;
import com.glaeriasmite.fantasy.bot.handlers.Action;

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
    public void execute(Context context) {

        String replyString = "";

        for (int i = 0; i < event.getOption("iterations").getAsInt(); i++) {
            replyString += event.getOption("content").getAsString() + "\n";
        }

        FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithMessage(event, replyString);

        this.queue(action);

    }

    @Override
    public void executeMethod(String methodName, Context context, Object... params) throws Exception {

        Method method = Test.class.getDeclaredMethod(methodName, Context.class, Object[].class);
        method.invoke(this, context, new Object[] {params});

    }

    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {
        
        request.queue();
        
    }

}