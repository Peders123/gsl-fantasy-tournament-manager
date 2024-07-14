package com.tanukismite.fantasy.bot.commands.slashCommands;

import java.lang.reflect.Method;

import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.handlers.Action;
import com.tanukismite.fantasy.bot.handlers.Components;
import com.tanukismite.fantasy.bot.handlers.Handler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.FluentRestAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class ButtonTest implements Command {

    private SlashCommandInteractionEvent event;

    public ButtonTest(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute(Handler handler) {

        FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithMessage(event, "Testing Buttons");

        action = Components.addActionRowReply((ReplyCallbackAction)action,
            Button.secondary(event.getUser().getId() + ":test", "TESTING"),
            Button.danger(event.getUser().getId() + ":bad", "BAD")
        );

        this.queue(action);

    }

    @Override
    public void executeMethod(String methodName, Handler handler, Object... params) throws Exception {

        Method method = ButtonTest.class.getDeclaredMethod(methodName, Handler.class, Object[].class);
        method.invoke(this, handler, new Object[] {params});

    }

    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {

        request.queue();
        
    }

}