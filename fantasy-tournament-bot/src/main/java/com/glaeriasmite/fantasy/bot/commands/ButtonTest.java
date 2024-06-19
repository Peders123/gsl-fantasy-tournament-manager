package com.glaeriasmite.fantasy.bot.commands;

import com.glaeriasmite.fantasy.bot.handlers.Action;

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
    public void execute(Context context) {

        event.getMessageChannel();

        System.out.println("Hello World");

        FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithMessage(event, "Testing Buttons");

        action = Action.addActionRow((ReplyCallbackAction)action,
            Button.secondary(event.getUser().getId() + ":test", "TESTING"),
            Button.danger(event.getUser().getId() + ":bad", "BAD")
        );

        this.queue(action);

    }

    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {

        request.queue();
        
    }

}