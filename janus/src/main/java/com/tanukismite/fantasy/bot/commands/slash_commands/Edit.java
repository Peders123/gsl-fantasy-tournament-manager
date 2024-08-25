package com.tanukismite.fantasy.bot.commands.slash_commands;

import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.handlers.Handler;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class Edit implements Command {

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

    public static void editMessage(MessageChannel channel, String id) {

        try {
            channel.editMessageById(id, "Edited");
            channel.sendMessage("SUCCESS!").queue();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void deleteMessage(MessageChannel channel, String id) {

        try {
            channel.deleteMessageById(id).queue();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
