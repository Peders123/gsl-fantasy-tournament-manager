package com.tanukismite.fantasy.bot.commands;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import com.tanukismite.fantasy.bot.commands.slashCommands.CreateSignups;
import com.tanukismite.fantasy.bot.commands.slashCommands.CreateTournament;
import com.tanukismite.fantasy.bot.signup.SignupData;

public class Context implements Serializable {

    private ConcurrentHashMap<String, SignupData> userSignupSessions;
    private CreateSignups signupRoot;
    private CreateTournament tournamentRoot;

    public Context() {
        this.userSignupSessions = new ConcurrentHashMap<>();
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
        if (userSignupSessions.get(id) == null) {
            return false;
        }
        return true;
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

    public void write() throws IOException {

        File file = new File("/data/janus/mount/janus/saves/data.tsl");

        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fileOut = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);

        out.writeObject(this);

        out.close();
        fileOut.close();
    }

}
