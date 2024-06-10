package com.glaeriasmite.fantasy.bot.commands;

import java.awt.Color;

import com.glaeriasmite.fantasy.bot.handlers.Action;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.FluentRestAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

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

        FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithEmbeds(event, embed.build());
        action = Action.addActionRow(action,  Button.secondary(event.getUser().getId() + ":test", "TESTING"));

        this.queue(action);

    }

    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {

        request.queue();

    }

}