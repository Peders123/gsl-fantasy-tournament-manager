package com.glaeriasmite.fantasy.bot.handlers;

import java.io.IOException;

import com.glaeriasmite.fantasy.bot.MercuryCommunicator;
import com.glaeriasmite.fantasy.bot.commands.Command;
import com.glaeriasmite.fantasy.bot.commands.Context;

public class Handler {

    private Context context;
    private MercuryCommunicator communicator;

    public Handler() {

        this.context = new Context();
        try {
            this.communicator = new MercuryCommunicator("Peders", "Pa55we1rd");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

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

    public MercuryCommunicator getCommunicator() {
        return this.communicator;
    }

}