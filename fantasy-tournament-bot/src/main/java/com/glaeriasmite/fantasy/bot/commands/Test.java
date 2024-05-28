package com.glaeriasmite.fantasy.bot.commands;

import com.glaeriasmite.fantasy.bot.handlers.Action;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.RestAction;

public class Test implements Command {

    private SlashCommandInteractionEvent event;

    public Test(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute() {

        String replyString = "";

        for (int i = 0; i < event.getOption("iterations").getAsInt(); i++) {
            replyString += event.getOption("content").getAsString() + "\n";
        }

        RestAction<InteractionHook> action = Action.replyWithMessage(event, replyString);

        this.queue(action);

    }

    @Override
    public <R> void queue(RestAction<R> request) {
        
        request.queue();
        
    }

}