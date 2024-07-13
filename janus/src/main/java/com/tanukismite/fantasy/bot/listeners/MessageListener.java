package com.tanukismite.fantasy.bot.listeners;

import com.tanukismite.fantasy.bot.handlers.Handler;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MessageListener extends BaseListener {

    public MessageListener(Handler handler) {
        super(handler);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        return;

    }

}
