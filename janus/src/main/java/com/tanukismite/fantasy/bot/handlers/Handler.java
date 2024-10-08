package com.tanukismite.fantasy.bot.handlers;

import java.util.HashMap;

import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.commands.Context;
import com.tanukismite.fantasy.bot.commands.ExtendedCommand;
import com.tanukismite.fantasy.bot.communicators.MercuryCommunicator;

public class Handler {

    private Context context;
    private HashMap<String, MercuryCommunicator> communicators;

    public Handler() {
        this.context = new Context();
        this.communicators = new HashMap<>();
    }

    public void execute(Command command) {
        command.execute(this);
    }

    public void executeMethod(ExtendedCommand command, String methodName, Object... params) throws Exception {
        command.executeMethod(methodName, this, params);
    }

    public Context getContext() {
        return this.context;
    }

    public void addCommunicator(String type, MercuryCommunicator communicator) {
        this.communicators.put(type, communicator);
    }

    public MercuryCommunicator getCommunicator(String type) {
        return this.communicators.get(type);
    }

}