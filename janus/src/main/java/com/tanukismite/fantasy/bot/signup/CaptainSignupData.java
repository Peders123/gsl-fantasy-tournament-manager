package com.tanukismite.fantasy.bot.signup;

import java.awt.Color;
import java.util.Map;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import com.tanukismite.fantasy.bot.commands.slashcommands.CreateSignups;


/**
 * This class extends {@link SignupData} and provides specific fields 
 * for captain signups, such as roles and SmiteGuru profile.
 *
 * <p>It overrides the methods to convert the captain sign-up data into a Discord embed and a map.</p>
 *
 * @see SignupData
 *
 * @author Rory Caston
 * @since 1.0
 */
public class CaptainSignupData extends SignupData {

    private String reason;
    private String teamName;

    /**
     * Constructs a new instance tied to the current signup session.
     *
     * @param currentSession
     */
    public CaptainSignupData(CreateSignups currentSession) {
        super(currentSession);
    }

    /**
     * Getter for reason.
     *
     * @return The current reason.
     */
    public String getReason() {
        return this.reason;
    }

    /**
     * Setter for reason.
     *
     * @param reason The new reason.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Getter for teamName.
     *
     * @return The current teamName.
     */
    public String getTeamName() {
        return this.teamName;
    }

    /**
     * Setter for teamName.
     *
     * @param teamName The new teamName.
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /**
     * Converts the captain signup data into a Discord embed message.
     *
     * @return A {@link MessageEmbed} containing the captain's signup information.
     */
    @Override
    public MessageEmbed toEmbed() {
        
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Embed testing command");
        embed.setDescription("GLAERIA SMITE LEAGUE FANTASY TOURNAMENT\n @peders");
        embed.setColor(new Color(28, 19, 31, 255));
        embed.setFooter("June 14th");

        return embed.build();

    }

    /**
     * Converts the data into a map of key-value pairs, ready for processing.
     *
     * @return A map representing the captain sign-up data.
     */
    @Override
    public Map<String, Object> toMap() {

        Map<String, Object> map = super.toMap();
        map.put("reason", this.reason);
        map.put("team_name", this.teamName);

        return map;

    }

}
