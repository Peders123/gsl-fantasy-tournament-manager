package com.glaeriasmite.fantasy.bot.commands;

import java.awt.Color;

import com.glaeriasmite.fantasy.bot.handlers.Action;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.RestAction;

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
        embed.setDescription("GLAERIA SMITE LEAGUE FANTASY TOURNAMENT\n @peders");
        embed.setColor(new Color(28, 19, 31, 255));
        embed.setFooter("June 14th");

        RestAction<InteractionHook> action = Action.replyWithEmbeds(event, embed.build());

        this.queue(action);

    }

    
    public <R> void queue(RestAction<R> request) {

        request.queue();

    }

}