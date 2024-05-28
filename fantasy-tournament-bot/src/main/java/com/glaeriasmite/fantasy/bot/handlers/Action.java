package com.glaeriasmite.fantasy.bot.handlers;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.requests.FluentRestAction;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class Action {

    public static FluentRestAction<Message, MessageCreateAction> sendMessage(MessageChannel channel, String message) {

        return channel.sendMessage(message);

    }

    public static FluentRestAction<InteractionHook, ReplyCallbackAction> replyWithMessage(SlashCommandInteractionEvent event, String message) {

        return event.reply(message);

    }

    public static FluentRestAction<InteractionHook, ReplyCallbackAction> replyWithEmbeds(SlashCommandInteractionEvent event, MessageEmbed embed) {

        return event.replyEmbeds(embed);

    }

    public static FluentRestAction<InteractionHook, ReplyCallbackAction> addActionRow(ReplyCallbackAction action, ItemComponent... components) {

        return action.addActionRow(components);

    }

}