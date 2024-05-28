package com.glaeriasmite.fantasy.bot.handlers;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class Action {

    public static void sendMessage(MessageChannel channel, String message) {

        channel.sendMessage(message).queue();

    }

    public static void replyToMessage(SlashCommandInteractionEvent event, String message) {

        event.reply(message).queue();

    }

    public static void replyWithButton(SlashCommandInteractionEvent event, String message) {

        event.reply(message)
            .addActionRow(
                Button.secondary(event.getUser().getId() + ":test", "TESTING"),
                Button.danger(event.getUser().getId() + ":bad", "BAD"))
            .queue();

    }

}