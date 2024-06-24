package com.glaeriasmite.fantasy.bot.listeners;

import com.glaeriasmite.fantasy.bot.commands.slashCommands.ButtonTest;
import com.glaeriasmite.fantasy.bot.commands.slashCommands.Edit;
import com.glaeriasmite.fantasy.bot.commands.slashCommands.Embed;
import com.glaeriasmite.fantasy.bot.commands.slashCommands.Ping;
import com.glaeriasmite.fantasy.bot.commands.slashCommands.Signup;
import com.glaeriasmite.fantasy.bot.commands.slashCommands.Test;
import com.glaeriasmite.fantasy.bot.commands.slashCommands.User;
import com.glaeriasmite.fantasy.bot.handlers.Handler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SlashCommandListener extends BaseListener {

    public SlashCommandListener(Handler handler) {
        super(handler);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        switch (event.getName()) {

            case "test":
                handler.execute(new Test(event));
                break;

            case "button":
                handler.execute(new ButtonTest(event));
                break;

            case "ping":
                handler.execute(new Ping(event));
                break;

            case "embed":
                handler.execute(new Embed(event));
                break;

            case "user":
                handler.execute(new User(event));
                break;

            case "signup":
                handler.execute(new Signup(event));
                break;

            case "edit":
                handler.execute(new Edit(event));
                break;

        }

    }

}
