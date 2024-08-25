package com.tanukismite.fantasy.bot.commands.slash_commands;

import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.handlers.Handler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Test implements Command {

    private SlashCommandInteractionEvent event;

    public Test(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute(Handler handler) {

        StringBuilder replyString = new StringBuilder("");

        for (int i = 0; i < event.getOption("iterations").getAsInt(); i++) {
            replyString.append(event.getOption("content").getAsString() + "\n");
        }

        event.reply(replyString.toString()).queue();

    }

}