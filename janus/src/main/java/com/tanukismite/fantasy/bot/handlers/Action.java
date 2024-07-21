package com.tanukismite.fantasy.bot.handlers;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.requests.FluentRestAction;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ModalCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class Action {

    public static FluentRestAction<Message, MessageCreateAction> sendMessage(MessageChannel channel, String message) {

        return channel.sendMessage(message);

    }

    public static FluentRestAction<Message, MessageCreateAction> sendMessageWithEmbed(MessageChannel channel, MessageEmbed embed) {

        return channel.sendMessageEmbeds(embed);

    }

    public static FluentRestAction<InteractionHook, ReplyCallbackAction> replyWithMessage(IReplyCallback event, String message) {

        return Action.replyWithMessage(event, message, false);

    }

    public static FluentRestAction<InteractionHook, ReplyCallbackAction> replyWithMessage(IReplyCallback event, String message, boolean ephemeral) {

        return event.reply(message).setEphemeral(ephemeral);

    }

    public static FluentRestAction<InteractionHook, ReplyCallbackAction> replyWithEmbeds(SlashCommandInteractionEvent event, MessageEmbed embed) {

        return event.replyEmbeds(embed);

    }

    public static FluentRestAction<InteractionHook, ReplyCallbackAction> replyWithEmbeds(IReplyCallback event, MessageEmbed embed) {

        return event.replyEmbeds(embed);

    }

    public static FluentRestAction<Void, ModalCallbackAction> replyWithModal(ButtonInteractionEvent event, Modal modal) {

        return event.replyModal(modal);

    }

    public static FluentRestAction<Void, ModalCallbackAction> replyWithModal(SlashCommandInteractionEvent event, Modal modal) {

        return event.replyModal(modal);

    }

    public static RestAction<Message> editMessage(MessageChannel channel, String messageId, String message) {

        return channel.editMessageById(messageId, message);

    }

    public static RestAction<Message> editEmbed(MessageChannel channel, String messageId, MessageEmbed embed) {

        return channel.editMessageEmbedsById(messageId, embed);

    }

    public static RestAction<Void> deleteMessage(MessageChannel channel, String messageId) {

        return channel.deleteMessageById(messageId);

    }

    public static Modal createModal(String id, String title, TextInput... components) {

        Modal.Builder modal = Modal.create(id, title);

        for (TextInput component : components) {
            modal.addActionRow(component);
        }

        return modal.build();

    }

}