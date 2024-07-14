package com.tanukismite.fantasy.bot.commands.slashCommands;

import java.awt.Color;
import java.lang.reflect.Method;

import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.commands.Context;
import com.tanukismite.fantasy.bot.handlers.Action;
import com.tanukismite.fantasy.bot.handlers.Components;
import com.tanukismite.fantasy.bot.handlers.Handler;

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
    public void execute(Handler handler) {

        event.getMessageChannel();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Embed testing command");
        embed.setDescription("GLAERIA SMITE LEAGUE FANTASY TOURNAMENT\n @peders");
        embed.setColor(new Color(28, 19, 31, 255));
        embed.setFooter("June 14th");

        FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithEmbeds(event, embed.build());
        action = Components.addActionRowReply(action,  Button.secondary(event.getUser().getId() + ":test", "TESTING"));

        this.queue(action);

    }

    @Override
    public void executeMethod(String methodName, Handler handler, Object... params) throws Exception {

        Context context = handler.getContext();

        Method method = Embed.class.getDeclaredMethod(methodName, Handler.class, Object[].class);
        method.invoke(this, handler, new Object[] {params});

    }

    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {

        request.queue();

    }

}