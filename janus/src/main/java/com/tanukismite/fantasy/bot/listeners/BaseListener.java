package com.tanukismite.fantasy.bot.listeners;

import com.tanukismite.fantasy.bot.commands.slash_commands.CreateSignups;
import com.tanukismite.fantasy.bot.handlers.Handler;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BaseListener extends ListenerAdapter {

    protected Handler handler;

    public BaseListener() {

        System.out.println("ERROR");

    }

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

        try {
            CreateSignups.sendTestMessage(channel);
        } catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
        }

    }

}
