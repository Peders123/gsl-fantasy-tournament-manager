package com.tanukismite.fantasy.bot.commands.slashcommands;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.handlers.Handler;


public class Edit implements Command {

    private static final Logger logger = LogManager.getLogger("ConsoleLogger");

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
        } catch (Exception error) {
            logger.error("Error when editing message. Message id: {}", id, error);
        }

    }

    public static void deleteMessage(MessageChannel channel, String id) {

        try {
            channel.deleteMessageById(id).queue();
        } catch (Exception error) {
            logger.error("Error when deleting message. Message id: {}", id, error);
        }

    }

}
