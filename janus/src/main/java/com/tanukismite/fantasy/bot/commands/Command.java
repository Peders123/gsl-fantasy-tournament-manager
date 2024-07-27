package com.tanukismite.fantasy.bot.commands;

import com.tanukismite.fantasy.bot.handlers.Handler;

import net.dv8tion.jda.api.requests.FluentRestAction;

public interface Command {

    void execute(Handler handler);
    <R> void queue(FluentRestAction<R, ?> request);

}