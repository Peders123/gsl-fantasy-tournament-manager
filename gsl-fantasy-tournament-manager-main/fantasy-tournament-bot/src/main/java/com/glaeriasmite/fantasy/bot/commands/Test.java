package com.glaeriasmite.fantasy.bot.commands;

import com.glaeriasmite.fantasy.bot.handlers.Action;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Test implements Command {

    private SlashCommandInteractionEvent event;

    public Test(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute() {

        Action.replyToMessage(event, event.getOption("content").getAsString());

    }

}