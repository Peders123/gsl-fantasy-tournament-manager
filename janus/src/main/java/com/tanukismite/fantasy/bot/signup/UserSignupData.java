package com.tanukismite.fantasy.bot.signup;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;


/**
 * This class represents user-specific signup information, including 
 * the user's ID and their Discord username. It implements the {@link PostData} interface to 
 * convert this data into a map and a {@link MessageEmbed} for rich representation in Discord.
 * 
 * <p>This class can be used to store and display user signup information in a structured format, 
 * either for database storage or presentation in Discord messages.</p>
 * 
 * @see PostData
 * @see MessageEmbed
 * @see EmbedBuilder
 * 
 * @author Rory Caston
 * @since 1.0
 */
public class UserSignupData implements PostData {
    
    protected String id;
    protected String discord;

    /**
     * Default constructor for {@code UserSignupData}.
     */
    public UserSignupData() {}

    /**
     * Constructs a new {@code UserSignupData} instance with the specified user ID and Discord username.
     * 
     * @param id      The user's ID.
     * @param discord The user's Discord username.
     */
    public UserSignupData(String id, String discord) {
        this.id = id;
        this.discord = discord;
    }

    /**
     * Getter for id.
     * @return The current id.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Setter for id.
     * @param id The new id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for discord.
     * @return The current discord.
     */
    public String getDiscord() {
        return this.discord;
    }

    /**
     * Setter for discord.
     * @param discord The new discord.
     */
    public void setDiscord(String discord) {
        this.discord = discord;
    }

    /**
     * Converts the user signup data into a map of key-value pairs for processing.
     * 
     * @return A map representing the user signup data.
     */
    @Override
    public Map<String, Object> toMap() {

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", Long.parseLong(this.id));
        map.put("discord_name", this.discord);

        return map;

    }

    /**
     * Converts the user signup data into a rich {@link MessageEmbed} for display in Discord.
     * 
     * @return A {@link MessageEmbed} containing the user's signup details.
     */
    @Override
    public MessageEmbed toEmbed() {
        
        EmbedBuilder embed = new EmbedBuilder()
            .setAuthor(this.discord.toUpperCase())
            .setTitle("User Data")
            .addField("Id", this.id, false)
            .addField("Username", this.discord, false)
            .setColor(new Color(28, 19, 31, 255));

        return embed.build();

    }

}