package com.glaeriasmite.fantasy.bot.commands;

import com.glaeriasmite.fantasy.bot.handlers.Action;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.RestAction;

public class ButtonTest implements Command {

    private SlashCommandInteractionEvent event;

    public ButtonTest(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute() {

        event.getMessageChannel();

        System.out.println("Hello World");

        // Action.replyWithButton(event, "Testing Buttons");

    }

    @Override
    public <R> void queue(RestAction<R> request) {
    
        request.queue();
        
    }

}