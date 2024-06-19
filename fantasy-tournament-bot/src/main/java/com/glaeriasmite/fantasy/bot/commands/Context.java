package com.glaeriasmite.fantasy.bot.commands;

import com.glaeriasmite.fantasy.bot.SignupData;

import java.util.concurrent.ConcurrentHashMap;

public class Context {

    private ConcurrentHashMap<String, SignupData> userSignupSessions;

    public Context() {

        this.userSignupSessions = new ConcurrentHashMap<>();

    }

    public SignupData getUserSignupData(String id) {

        return this.userSignupSessions.get(id);

    }

    public void putUserSignupData(String id, SignupData data) {

        this.userSignupSessions.put(id, data);

    }

}
