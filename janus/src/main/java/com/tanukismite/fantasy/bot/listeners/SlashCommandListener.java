package com.tanukismite.fantasy.bot.listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import com.tanukismite.fantasy.bot.commands.slashcommands.CreateSignups;
import com.tanukismite.fantasy.bot.commands.slashcommands.CreateTournament;
import com.tanukismite.fantasy.bot.commands.slashcommands.Ping;
import com.tanukismite.fantasy.bot.handlers.Handler;


public class SlashCommandListener extends BaseListener {

    public SlashCommandListener(Handler handler) {
        super(handler);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        switch (event.getName()) {

            case "ping":
                this.handler.execute(new Ping(event));
                break;

            case "create-signups":
                this.handler.execute(new CreateSignups(event));
                break;

            case "create-tournament":
                this.handler.execute(new CreateTournament(event));
                break;

            default:
                BaseListener.notImplemented(event.getChannel());

        }

    }

}
