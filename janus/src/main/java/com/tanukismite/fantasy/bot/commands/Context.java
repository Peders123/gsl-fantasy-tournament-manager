package com.tanukismite.fantasy.bot.commands;

import java.util.concurrent.ConcurrentHashMap;

import net.dv8tion.jda.api.JDA;

import com.tanukismite.fantasy.bot.commands.slashcommands.CreateSignups;
import com.tanukismite.fantasy.bot.commands.slashcommands.CreateTournament;
import com.tanukismite.fantasy.bot.signup.SignupData;


/**
 * This class manages session data for the bot commands.
 * <p>
 * It acts as a central data store for managing user sign-up sessions and provides methods for
 * retrieving, adding, and removing {@link SignupData}. Additionally, it holds the command roots
 * for the sign-up and tournament creation processes, facilitating the execution of these commands.
 * </p>
 * 
 * @author Rory Caston
 * @since 1.0
 */
public class Context {

    private ConcurrentHashMap<String, SignupData> userSignupSessions;
    private CreateSignups signupRoot;
    private CreateTournament tournamentRoot;

    /**
     * Constructor for Context; initliases the de-serialisation of the sign-up root.
     * 
     * @param jda The current {@link JDA} instance the bot is running on.
     */
    public Context(JDA jda) {
        this.userSignupSessions = new ConcurrentHashMap<>();
        this.signupRoot = this.initialiseSignupRoot(jda);
    }

    /**
     * Initialises the {@link CreateSignups} root message by reading from the serialised file.
     * 
     * @param jda The current {@link JDA} instance the bot is running on.
     * @return The initialised {@link CreateSignups} object. Returns {@code null} if no data file
     *         is found.
     */
    private CreateSignups initialiseSignupRoot(JDA jda) {

        CreateSignups signups = CreateSignups.readObject();
        if (signups == null) {
            return null;
        }
        signups.initialiseNonSerializedFields(jda);

        return signups;

    }

    /**
     * Retrieves the user's sign-up data for a given session, casting to either player or captain.
     * 
     * @param <T>  The type of {@link SignupData} expected.
     * @param id   The discord user id to get data for.
     * @param type The type of {@link SignupData}. Either CaptainSignupData or PlayerSignupData.
     * @return The user's {@link SignupData} if found.
     */
    public <T extends SignupData> T getUserSignupData(String id, Class<T> type) {
        return type.cast(this.userSignupSessions.get(id));
    }

    /**
     * Stores a user's sign-up data for the current session. Casts to either player or captain.
     * 
     * @param <T>  The type of {@link SignupData} being stored.
     * @param id   The discord user id to store data for.
     * @param data The {@link SignupData} object to store.
     * @param type The type of {@link SignupData}. Either CaptainSignupData or PlayerSignupData.
     */
    public <T extends SignupData> void putUserSignupData(String id, SignupData data, Class<T> type) {
        this.userSignupSessions.put(id, type.cast(data));
    }

    /**
     * Deletes a user's sign-up data for the current session.
     * 
     * @param id The discord user id to delete data for.
     */
    public void removeUserSignupData(String id) {
        this.userSignupSessions.remove(id);
    }

    /**
     * Checks whether sign-up data exists for a given user.
     * 
     * @param id The discord user id to check data exists for.
     * @return {@code true} if sign-up data exists for the user, {@code false} if otherwise.
     */
    public boolean signupDataExists(String id) {
        return userSignupSessions.get(id) == null;
    }

    /**
     * Getter for sign-up command root.
     * @return The {@link CreateSignups} root for sign-up creation.
     */
    public CreateSignups getSignupRoot() {
        return this.signupRoot;
    }

    /**
     * Setter for sign-up command root.
     * @param signupRoot The {@link CreateSignups} root for sign-up creation.
     */
    public void setSignupRoot(CreateSignups signupRoot) {
        this.signupRoot = signupRoot;
    }

    /**
     * Getter for tournament command root.
     * @return The {@link CreateTournament} root for tournament creation.
     */
    public CreateTournament getTournamentRoot() {
        return this.tournamentRoot;
    }

    /**
     * Setter for tournament command root.
     * @param tournamentRoot The {@link CreateTournament} root for tournament creation.
     */
    public void setTournamentRoot(CreateTournament tournamentRoot) {
        this.tournamentRoot = tournamentRoot;
    }

}
