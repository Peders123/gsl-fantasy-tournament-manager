package com.tanukismite.fantasy.bot.signup;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tanukismite.fantasy.bot.commands.slashcommands.CreateSignups;


/**
 * The {@code SignupData} abstract class provides a base implementation of {@link PostData} 
 * for storing and processing user signup information. It includes common fields such as 
 * user ID, tournament ID, in-game name (IGN), and the root signup session.
 * 
 * <p>This class can be extended to create specific types of signup data like player signups.</p>
 * 
 * @see PostData
 *
 * @author Rory Caston
 * @since 1.0
 */
public abstract class SignupData implements PostData {

    private static final Logger logger = LogManager.getLogger("ConsoleLogger");

    protected String id;
    protected int tournamentId;
    protected String ign;
    protected CreateSignups signupRoot;
    protected GenericComponentInteractionCreateEvent signupSession;

    /**
     * Default constructor for {@code SignupData}.
     */
    protected SignupData() {}

    /**
     * Constructs a new {@code SignupData} instance tied to the current signup session.
     *
     * @param currentSession The current {@link CreateSignups} session.
     */
    protected SignupData(CreateSignups currentSession) {

        logger.info("Creating signup data.");

        this.signupRoot = currentSession;
        this.ign = null;

    }

    /**
     * Getter for sign-up id.
     *
     * @return The current sign-up id.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Setter for sign-up id.
     *
     * @param id The new sign-up id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for tournamentId.
     *
     * @return The current tournamentId.
     */
    public int getTournamentId() {
        return this.tournamentId;
    }

    /**
     * Setter for tournamentId.
     *
     * @param tournamentId The new tournamentId.
     */
    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

    /**
     * Getter for ign.
     *
     * @return The current ign.
     */
    public String getIgn() {
        return this.ign;
    }

    /**
     * Setter for ign.
     *
     * @param ign The new ign.
     */
    public void setIgn(String ign) {
        this.ign = ign;
    }

    /**
     * Getter for signupRoot.
     *
     * @return The current signupRoot.
     */
    public CreateSignups getSignupRoot() {
        return this.signupRoot;
    }

    /**
     * Getter for signupSession.
     *
     * @return The current signupSession.
     */
    public GenericComponentInteractionCreateEvent getSignupSession() {
        return this.signupSession;
    }

    /**
     * Setter for signupSession.
     *
     * @param signupSession The new signupSession.
     */
    public void setSignupSession(GenericComponentInteractionCreateEvent signupSession) {
        this.signupSession = signupSession;
    }

    /**
     * The string representation is just the signup Root.
     *
     * @return The string representation for sign-up data.
     */
    @Override
    public String toString() {
        return this.signupRoot.toString();
    }

    /**
     * Converts the data into a map of key-value pairs, ready for processing.
     *
     * @return A map representing the sign-up data.
     */
    @Override
    public Map<String, Object> toMap() {

        Map<String, Object> map = new HashMap<>();
        map.put("tournament_id", this.tournamentId);
        map.put("user_id", Long.valueOf(this.id));
        map.put("smite_name", this.ign);

        return map;

    }

}
