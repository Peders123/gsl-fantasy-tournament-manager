package com.tanukismite.fantasy.bot.signup;

import java.util.HashMap;
import java.util.Map;

import com.tanukismite.fantasy.bot.commands.slashCommands.CreateSignups;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;

public abstract class SignupData implements PostData {

    protected String id;
    protected int tournamentId;
    protected String ign;
    protected CreateSignups signupRoot;
    protected GenericComponentInteractionCreateEvent signupSession;

    protected SignupData() {
        System.out.println("ERROR");
    }

    protected SignupData(CreateSignups currentSession) {

        System.out.println("CREATING");
        System.out.println(currentSession);

        this.signupRoot = currentSession;

        this.ign = null;

    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTournamentId() {
        return this.tournamentId;
    }

    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getIGN() {
        return this.ign;
    }

    public void setIGN(String ign) {
        this.ign = ign;
    }

    public CreateSignups getSignupRoot() {
        return this.signupRoot;
    }

    public GenericComponentInteractionCreateEvent getSignupSession() {
        return this.signupSession;
    }

    public void setSignupSession(GenericComponentInteractionCreateEvent signupSession) {
        this.signupSession = signupSession;
    }

    public abstract MessageEmbed toEmbed();
    
    @Override
    public String toString() {

        String str = this.signupRoot.toString();

        return str;

    }

    @Override
    public Map<String, Object> toMap() {

        Map<String, Object> map = new HashMap<>();
        map.put("tournament_id", this.tournamentId);
        map.put("user_id", Long.valueOf(this.id));
        map.put("smite_name", this.ign);

        return map;

    }

}
