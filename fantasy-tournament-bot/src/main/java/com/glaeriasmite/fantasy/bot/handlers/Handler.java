package com.glaeriasmite.fantasy.bot.handlers;

import com.glaeriasmite.fantasy.bot.commands.Command;
import com.glaeriasmite.fantasy.bot.commands.Context;

public class Handler {

    private Context context;

    public Handler() {

        this.context = new Context();

    }

    public void execute(Command command) {

        command.execute(this.context);

    }

    public void executeMethod(Command command, String methodName, Object... params) throws Exception {

        command.executeMethod(methodName, this.context, params);

    }

    public Context getContext() {

        return this.context;

    }

}