package com.glaeriasmite.fantasy.bot.commands;

import java.util.concurrent.ConcurrentHashMap;

import com.glaeriasmite.fantasy.bot.signup.SignupData;

public class Context {

    private ConcurrentHashMap<String, SignupData> userSignupSessions;

    public Context() {

        this.userSignupSessions = new ConcurrentHashMap<>();

    }

    public <T extends SignupData> T getUserSignupData(String id, Class<T> type) {

        return type.cast(this.userSignupSessions.get(id));

    }

    public <T extends SignupData> void putUserSignupData(String id, SignupData data, Class<T> type) {

        this.userSignupSessions.put(id, type.cast(data));

    }

}
