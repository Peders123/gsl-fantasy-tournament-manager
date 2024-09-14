package com.tanukismite.fantasy.bot.commands.slashcommands;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.handlers.Handler;


public class Embed implements Command {
    
    private SlashCommandInteractionEvent event;

    public Embed(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute(Handler handler) {

        event.getMessageChannel();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Embed testing command");
        embed.setDescription("GLAERIA SMITE LEAGUE FANTASY TOURNAMENT\n @peders");
        embed.setColor(new Color(28, 19, 31, 255));
        embed.setFooter("June 14th");

        event.replyEmbeds(embed.build()).addActionRow(
            Button.secondary(event.getUser().getId() + ":test", "TESTING")
        ).queue();

    }

}