package com.glaeriasmite.fantasy.bot.handlers;

import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Action {

    public static void sendMessage(MessageChannelUnion channel, String message) {

        channel.sendMessage(message).queue();

    }

}