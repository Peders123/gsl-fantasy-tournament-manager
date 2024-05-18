package com.glaeriasmite.fantasy.bot;

import com.glaeriasmite.fantasy.bot.commands.*;
import com.glaeriasmite.fantasy.bot.handlers.*;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    Handler handler = new Handler();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        handler.execute(new Ping(event));

    }

}