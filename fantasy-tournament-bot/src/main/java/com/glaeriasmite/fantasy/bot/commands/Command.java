package com.glaeriasmite.fantasy.bot.commands;

import net.dv8tion.jda.api.requests.FluentRestAction;

public interface Command {

    void execute(Context context);
    <R> void queue(FluentRestAction<R, ?> request);

}