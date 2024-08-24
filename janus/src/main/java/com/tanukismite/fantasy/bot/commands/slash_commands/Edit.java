package com.tanukismite.fantasy.bot.commands.slash_commands;

import com.tanukismite.fantasy.bot.commands.ExtendedCommand;
import com.tanukismite.fantasy.bot.handlers.Components;
import com.tanukismite.fantasy.bot.handlers.Handler;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.FluentRestAction;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.internal.entities.channel.concrete.TextChannelImpl;

public class Edit extends ExtendedCommand {

    private SlashCommandInteractionEvent event;

    public Edit(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute(Handler handler) {

        event.reply("Testing Buttons").addActionRow(
            Button.primary(event.getUser().getId() + ":edit", "EDIT"),
            Button.danger(event.getUser().getId() + ":delete", "DELETE")
        ).queue();
 
    }

    protected void editMessage(Handler handler, TextChannelImpl channel, String id) {

        try {
            channel.sendMessage("SUCCESS!").queue();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    protected void deleteMessage(Handler handler, TextChannelImpl channel, String id) {

        try {
            channel.deleteMessageById(id).queue();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
