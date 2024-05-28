package com.glaeriasmite.fantasy.bot.commands;

import com.glaeriasmite.fantasy.bot.handlers.Action;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class ButtonTest implements Command {

    private SlashCommandInteractionEvent event;

    public ButtonTest(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute() {

        event.getMessageChannel();

        System.out.println("Hello World");

        RestAction<InteractionHook> action = Action.replyWithMessage(event, "Testing Buttons");

        action = Action.addActionRow((ReplyCallbackAction)action,
            Button.secondary(event.getUser().getId() + ":test", "TESTING"),
            Button.danger(event.getUser().getId() + ":bad", "BAD")
        );

        this.queue(action);

    }

    @Override
    public <R> void queue(RestAction<R> request) {

        request.queue();
        
    }

}