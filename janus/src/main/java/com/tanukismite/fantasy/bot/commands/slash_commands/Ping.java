package com.tanukismite.fantasy.bot.commands.slash_commands;

import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.handlers.Handler;

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
    public void execute(Handler handler) {

        event.reply("Pong!").queue();

    }

}