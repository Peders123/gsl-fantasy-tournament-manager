package com.tanukismite.fantasy.bot.listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import com.tanukismite.fantasy.bot.commands.slashcommands.ButtonTest;
import com.tanukismite.fantasy.bot.commands.slashcommands.CreateSignups;
import com.tanukismite.fantasy.bot.commands.slashcommands.CreateTournament;
import com.tanukismite.fantasy.bot.commands.slashcommands.Delete;
import com.tanukismite.fantasy.bot.commands.slashcommands.Edit;
import com.tanukismite.fantasy.bot.commands.slashcommands.Embed;
import com.tanukismite.fantasy.bot.commands.slashcommands.Ping;
import com.tanukismite.fantasy.bot.commands.slashcommands.Test;
import com.tanukismite.fantasy.bot.commands.slashcommands.User;
import com.tanukismite.fantasy.bot.commands.slashcommands.Volumes;
import com.tanukismite.fantasy.bot.handlers.Handler;


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
