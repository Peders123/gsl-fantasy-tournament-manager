package com.tanukismite.fantasy.bot.listeners;

import com.tanukismite.fantasy.bot.handlers.Handler;

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

}
