package com.tanukismite.fantasy.bot.commands;

import java.util.concurrent.ConcurrentHashMap;

import com.tanukismite.fantasy.bot.commands.slash_commands.CreateSignups;
import com.tanukismite.fantasy.bot.commands.slash_commands.CreateTournament;
import com.tanukismite.fantasy.bot.signup.SignupData;

import net.dv8tion.jda.api.JDA;

public class Context {

    private ConcurrentHashMap<String, SignupData> userSignupSessions;
    private CreateSignups signupRoot;
    private CreateTournament tournamentRoot;

    public Context(JDA jda) {
        this.userSignupSessions = new ConcurrentHashMap<>();
        this.signupRoot = this.initialiseSignupRoot(jda);
    }

    private CreateSignups initialiseSignupRoot(JDA jda) {

        CreateSignups signups = CreateSignups.readObject();
        if (signups == null) {
            return null;
        }
        signups.initialiseNonSerializedFields(jda);

        return signups;

    }

    public <T extends SignupData> T getUserSignupData(String id, Class<T> type) {
        return type.cast(this.userSignupSessions.get(id));
    }

    public <T extends SignupData> void putUserSignupData(String id, SignupData data, Class<T> type) {
        this.userSignupSessions.put(id, type.cast(data));
    }

    public void removeUserSignupData(String id) {
        this.userSignupSessions.remove(id);
    }

    public boolean signupDataExists(String id) {
        return userSignupSessions.get(id) == null;
    }

    public CreateSignups getSignupRoot() {
        return this.signupRoot;
    }

    public void setSignupRoot(CreateSignups signupRoot) {
        this.signupRoot = signupRoot;
    }

    public CreateTournament getTournamentRoot() {
        return this.tournamentRoot;
    }

    public void setTournamentRoot(CreateTournament tournamentRoot) {
        this.tournamentRoot = tournamentRoot;
    }

}
