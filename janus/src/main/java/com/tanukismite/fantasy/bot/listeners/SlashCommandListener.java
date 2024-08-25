package com.tanukismite.fantasy.bot.listeners;

import com.tanukismite.fantasy.bot.commands.slash_commands.ButtonTest;
import com.tanukismite.fantasy.bot.commands.slash_commands.CreateSignups;
import com.tanukismite.fantasy.bot.commands.slash_commands.CreateTournament;
import com.tanukismite.fantasy.bot.commands.slash_commands.Delete;
import com.tanukismite.fantasy.bot.commands.slash_commands.Edit;
import com.tanukismite.fantasy.bot.commands.slash_commands.Embed;
import com.tanukismite.fantasy.bot.commands.slash_commands.Ping;
import com.tanukismite.fantasy.bot.commands.slash_commands.Test;
import com.tanukismite.fantasy.bot.commands.slash_commands.User;
import com.tanukismite.fantasy.bot.commands.slash_commands.Volumes;
import com.tanukismite.fantasy.bot.handlers.Handler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SlashCommandListener extends BaseListener {

    public SlashCommandListener(Handler handler) {
        super(handler);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        switch (event.getName()) {

            case "test":
                this.handler.execute(new Test(event));
                break;

            case "button":
                this.handler.execute(new ButtonTest(event));
                break;

            case "ping":
                this.handler.execute(new Ping(event));
                break;

            case "embed":
                this.handler.execute(new Embed(event));
                break;

            case "user":
                this.handler.execute(new User(event));
                break;

            case "edit":
                this.handler.execute(new Edit(event));
                break;

            case "create-signups":
                this.handler.execute(new CreateSignups(event));
                break;

            case "delete":
                this.handler.execute(new Delete(event));
                break;

            case "create-tournament":
                this.handler.execute(new CreateTournament(event));
                break;

            case "volumes":
                this.handler.execute(new Volumes(event));
                break;

            default:
                BaseListener.notImplemented(event.getChannel());

        }

    }

}
