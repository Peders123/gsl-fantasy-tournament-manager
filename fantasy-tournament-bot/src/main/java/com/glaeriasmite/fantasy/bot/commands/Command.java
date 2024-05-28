package com.glaeriasmite.fantasy.bot.commands;

import net.dv8tion.jda.api.requests.RestAction;

public interface Command {

    void execute();
    <R> void queue(RestAction<R> request);

}