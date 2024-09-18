package com.tanukismite.fantasy.bot.signup;

import java.awt.Color;
import java.util.Map;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import com.tanukismite.fantasy.bot.Role;
import com.tanukismite.fantasy.bot.commands.slashcommands.CreateSignups;


/**
 * This class extends {@link SignupData} and provides specific fields 
 * for player signups, such as roles and SmiteGuru profile.
 * 
 * <p>It overrides the methods to convert the player sign-up data into a Discord embed and a map.</p>
 * 
 * @see SignupData
 * 
 * @author Rory Caston
 * @since 1.0
 */
public class PlayerSignupData extends SignupData {

    private String smiteGuru;
    protected Role role1;
    protected Role role2;

    /**
     * Constructs a new instance tied to the current signup session.
     * @param currentSession
     */
    public PlayerSignupData(CreateSignups currentSession) {
        super(currentSession);
    }

    /**
     * Getter for smiteGuru.
     * @return The current smiteGuru.
     */
    public String getSmiteGuru() {
        return this.smiteGuru;
    }

    /**
     * Setter for smiteGuru.
     * @param smiteGuru The new smiteGuru.
     */
    public void setSmiteGuru(String smiteGuru) {
        this.smiteGuru = smiteGuru;
    }

    /**
     * Getter for role1.
     * @return The current role1.
     */
    public Role getRole1() {
        return this.role1;
    }

    /**
     * Setter for role1.
     * @param role1 The new role1.
     */
    public void setRole1(Role role1) {
        this.role1 = role1;
    }

    /**
     * Getter for role2.
     * @return The current role2
     */
    public Role getRole2() {
        return this.role2;
    }

    /**
     * Setter for role2.
     * @param role2 The new role2.
     */
    public void setRole2(Role role2) {
        this.role2 = role2;
    }

    /**
     * Converts the player signup data into a Discord embed message.
     *
     * @return A {@link MessageEmbed} containing the player's signup information.
     */
    @Override
    public MessageEmbed toEmbed() {
        
        EmbedBuilder embed = new EmbedBuilder()
            .setAuthor(this.ign.toUpperCase())
            .setTitle("Signup Data")
            .addField("Primary Role", this.role1.getFormattedEmote() + " " + this.role1.name(), true)
            .addField("Secondary Role", this.role2.getFormattedEmote() + " " + this.role2.name(), true)
            .setColor(new Color(28, 19, 31, 255))
            .setFooter(this.smiteGuru);

        return embed.build();

    }

    /**
     * Converts the data into a map of key-value pairs, ready for processing.
     * 
     * @return A map representing the player sign-up data.
     */
    @Override
    public Map<String, Object> toMap() {

        Map<String, Object> map = super.toMap();
        map.put("smite_guru", this.smiteGuru);
        map.put("role_1", this.role1.toString());
        map.put("role_2", this.role2.toString());
        map.put("captain_id", null);

        return map;

    }

}
