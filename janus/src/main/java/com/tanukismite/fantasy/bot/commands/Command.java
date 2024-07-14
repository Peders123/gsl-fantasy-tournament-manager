package com.tanukismite.fantasy.bot.commands;

import com.tanukismite.fantasy.bot.handlers.Handler;

import net.dv8tion.jda.api.requests.FluentRestAction;

public interface Command {

    void execute(Handler handler);
    void executeMethod(String methodName, Handler handler, Object... params) throws Exception;
    <R> void queue(FluentRestAction<R, ?> request);

}