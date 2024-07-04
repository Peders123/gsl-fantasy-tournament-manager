package com.glaeriasmite.fantasy.bot.commands;

import java.util.concurrent.ConcurrentHashMap;

import com.glaeriasmite.fantasy.bot.commands.slashCommands.CreateSignups;
import com.glaeriasmite.fantasy.bot.signup.SignupData;

public class Context {

    private ConcurrentHashMap<String, SignupData> userSignupSessions;
    private CreateSignups signupRoot;

    public Context() {

        this.userSignupSessions = new ConcurrentHashMap<>();

    }

    public <T extends SignupData> T getUserSignupData(String id, Class<T> type) {

        return type.cast(this.userSignupSessions.get(id));

    }

    public <T extends SignupData> void putUserSignupData(String id, SignupData data, Class<T> type) {

        T new_data = type.cast(data);

        System.out.println("CAST_DATA" + new_data);

        this.userSignupSessions.put(id, type.cast(data));

    }

    public CreateSignups getSignupRoot() {
        return this.signupRoot;
    }

    public void setSignupRoot(CreateSignups signupRoot) {
        this.signupRoot = signupRoot;
    }

}
