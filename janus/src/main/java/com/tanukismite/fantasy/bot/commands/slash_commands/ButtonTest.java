package com.tanukismite.fantasy.bot.commands.slash_commands;

import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.handlers.Handler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class ButtonTest implements Command {

    private SlashCommandInteractionEvent event;

    public ButtonTest(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute(Handler handler) {

        ReplyCallbackAction action = event.reply("Testing Buttons");

        action.addActionRow(
            Button.secondary(event.getUser().getId() + ":test", "TESTING"),
            Button.danger(event.getUser().getId() + ":bad", "BAD")
        );

        action.queue();

    }

}