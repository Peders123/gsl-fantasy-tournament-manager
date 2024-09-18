package com.tanukismite.fantasy.bot.signup;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;


/**
 * The this class represents data associated with a tournament, including 
 * the title, description, and scheduled date and time. It implements the {@link PostData} 
 * interface to convert this data into a map and a {@link MessageEmbed} for rich representation 
 * in Discord.
 * 
 * <p>This class can be used to encapsulate tournament details for displaying or posting 
 * in Discord or other data processing workflows.</p>
 * 
 * @see PostData
 * 
 * @author Rory Caston
 * @since 1.0
 */
public class TournamentData implements PostData {

    private String dateTime;
    private String title;
    private String description;

    /**
     * Default constructor for {@code TournamentData}.
     */
    public TournamentData() {}

    /**
     * Constructs a new {@code TournamentData} instance with the specified date, title, and description.
     * 
     * @param dateTime    The date and time of the tournament.
     * @param title       The title of the tournament.
     * @param description A brief description of the tournament.
     */
    public TournamentData(String dateTime, String title, String description) {
        this.dateTime = dateTime;
        this.title = title;
        this.description = description;
    }

    /**
     * Converts the tournament data into a map of key-value pairs for processing.
     * 
     * @return A map representing the tournament data.
     */
    @Override
    public Map<String, Object> toMap() {

        Map<String, Object> map = new HashMap<>();
        map.put("datetime", this.dateTime);
        map.put("title", this.title);
        map.put("description", this.description);

        return map;

    }

    /**
     * Converts the tournament data into a rich {@link MessageEmbed} for display in Discord.
     * 
     * @return A {@link MessageEmbed} containing the tournament details.
     */
    @Override
    public MessageEmbed toEmbed() {

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(this.title);
        embed.setDescription(this.description);
        embed.setColor(new Color(28, 19, 31, 255));
        embed.setFooter(this.dateTime);

        return embed.build();

    }

    /**
     * Getter for dateTime.
     * @return The current dateTime.
     */
    public String getDateTime() {
        return this.dateTime;
    }

    /**
     * Setter for dateTime.
     * @param dateTime The new dateTime.
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Getter for title.
     * @return The current title.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Setter for title.
     * @param title The new title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for description.
     * @return The current description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Setter for description.
     * @param description The new description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
