package com.glaeriasmite.fantasy.bot.commands;

import net.dv8tion.jda.api.requests.FluentRestAction;

public interface Command {

    void execute();
    <R> void queue(FluentRestAction<R, ?> request);

}