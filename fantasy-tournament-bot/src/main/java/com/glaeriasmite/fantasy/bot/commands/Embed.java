package com.glaeriasmite.fantasy.bot.commands;

import java.awt.Color;

import com.glaeriasmite.fantasy.bot.handlers.Action;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Embed implements Command {
    
    private SlashCommandInteractionEvent event;

    public Embed(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute() {

        event.getMessageChannel();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Embed testing command");
        embed.setDescription("GLAERIA SMITE LEAGUE FANTASY TOURNAMENT");
        embed.setColor(new Color(28, 19, 31));
        embed.setFooter("June 14th");

        Action.replyWithEmbeds(event, embed.build());
    }

}