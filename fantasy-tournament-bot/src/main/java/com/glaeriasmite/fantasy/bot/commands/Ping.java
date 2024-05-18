package com.glaeriasmite.fantasy.bot.commands;

import com.glaeriasmite.fantasy.bot.handlers.Action;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Ping implements Command {

    private MessageReceivedEvent event;

    public Ping(MessageReceivedEvent event) {

        this.event = event;

    }

    @Override
    public void execute() {

        Action.sendMessage(event.getChannel(), "PING");

    }

}