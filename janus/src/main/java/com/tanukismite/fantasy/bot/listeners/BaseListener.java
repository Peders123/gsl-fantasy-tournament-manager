package com.tanukismite.fantasy.bot.listeners;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import com.tanukismite.fantasy.bot.handlers.Handler;


public class BaseListener extends ListenerAdapter {

    protected Handler handler;

    public BaseListener() {}

    public BaseListener(Handler handler) {

        this.handler = handler;

    }

    public Handler getHandler() {
        return this.handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    protected static void notImplemented(MessageChannel channel) {

        channel.sendMessage("Not Yet implemented").queue();
  
    }

}
