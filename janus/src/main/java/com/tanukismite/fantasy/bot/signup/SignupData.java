package com.tanukismite.fantasy.bot.signup;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tanukismite.fantasy.bot.commands.slashcommands.CreateSignups;

public abstract class SignupData implements PostData {

    private static final Logger logger = LogManager.getLogger("ConsoleLogger");

    protected String id;
    protected int tournamentId;
    protected String ign;
    protected CreateSignups signupRoot;
    protected GenericComponentInteractionCreateEvent signupSession;

    protected SignupData() {}

    protected SignupData(CreateSignups currentSession) {

        logger.info("Creating signup data.");

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
    
    @Override
    public String toString() {

        return this.signupRoot.toString();

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
