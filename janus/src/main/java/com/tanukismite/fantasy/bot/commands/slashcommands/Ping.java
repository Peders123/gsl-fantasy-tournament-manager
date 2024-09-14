package com.tanukismite.fantasy.bot.commands.slashcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.handlers.Handler;


public class Ping implements Command {

    private SlashCommandInteractionEvent event;

    public Ping(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute(Handler handler) {

        event.reply("Pong!").queue();

    }

}