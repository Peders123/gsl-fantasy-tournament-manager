package com.glaeriasmite.fantasy.bot;

import com.glaeriasmite.fantasy.bot.commands.*;
import com.glaeriasmite.fantasy.bot.handlers.*;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    Handler handler = new Handler();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getMessage().getContentRaw().equals("!ping")) {

            // handler.execute(new Ping(event));
        
        }

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

        }

    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        String[] id = event.getComponentId().split(":");
        String authorId = id[0];
        String type = id[1];

        event.deferEdit().queue();

        System.out.println(authorId);

        switch (type) {

            case "test":
                System.out.println("TEST");
                break;

            case "bad":
                System.out.println("BAD");
                break;

        }

    }

}