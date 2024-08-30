package com.tanukismite.fantasy.bot.handlers;

import java.util.HashMap;

import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.commands.Context;
import com.tanukismite.fantasy.bot.communicators.MercuryCommunicator;

import net.dv8tion.jda.api.JDA;

public class Handler {

    private Context context;
    private HashMap<String, MercuryCommunicator> communicators;

    public Handler(JDA jda) {
        this.context = new Context(jda);
        this.communicators = new HashMap<>();
    }

    public void execute(Command command) {
        command.execute(this);
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