package com.tanukismite.fantasy.bot.commands;

import net.dv8tion.jda.api.requests.FluentRestAction;

public interface Command {

    void execute(Context context);
    void executeMethod(String methodName, Context context, Object... params) throws Exception;
    <R> void queue(FluentRestAction<R, ?> request);

}