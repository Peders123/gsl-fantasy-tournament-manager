package com.glaeriasmite.fantasy.bot.handlers;

import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Action {

    public static void sendMessage(MessageChannelUnion channel, String message) {

        channel.sendMessage(message).queue();

    }

    public static void replyToMessage(SlashCommandInteractionEvent event, String message) {

        event.reply(message).queue();

    }

}