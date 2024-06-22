package com.glaeriasmite.fantasy.bot.handlers;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.requests.FluentRestAction;
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

    public static FluentRestAction<InteractionHook, ReplyCallbackAction> replyWithMessage(SlashCommandInteractionEvent event, String message) {

        return event.reply(message);

    }

    public static FluentRestAction<InteractionHook, ReplyCallbackAction> replyWithEmbeds(SlashCommandInteractionEvent event, MessageEmbed embed) {

        return event.replyEmbeds(embed);

    }

    public static FluentRestAction<Void, ModalCallbackAction> replyWithModal(SlashCommandInteractionEvent event, Modal modal) {

        return event.replyModal(modal);

    }

    public static FluentRestAction<InteractionHook, ReplyCallbackAction> addActionRow(FluentRestAction<InteractionHook, ReplyCallbackAction> action, ItemComponent... components) {

        if (action instanceof ReplyCallbackAction) {
            return ((ReplyCallbackAction) action).addActionRow(components);
        }
        
        throw new IllegalArgumentException("The action must be a ReplyCallbackAction");

    }

    public static Modal createModal(String id, String title, TextInput... components) {

        Modal.Builder modal = Modal.create(id, title);

        for (TextInput component : components) {
            modal.addActionRow(component);
        }

        return modal.build();

    }

}