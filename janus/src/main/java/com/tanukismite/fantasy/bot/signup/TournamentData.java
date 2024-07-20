package com.tanukismite.fantasy.bot.signup;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class TournamentData implements PostData {

    private String dateTime;
    private String title;
    private String description;

    public TournamentData() {}

    public TournamentData(String dateTime, String title, String description) {

        this.dateTime = dateTime;
        this.title = title;
        this.description = description;

    }

    @Override
    public Map<String, Object> toMap() {

        Map<String, Object> map = new HashMap<>();
        map.put("datetime", this.dateTime);
        map.put("title", this.title);
        map.put("description", this.description);

        return map;

    }

    @Override
    public MessageEmbed toEmbed() {

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(this.title);
        embed.setDescription(this.description);
        embed.setColor(new Color(28, 19, 31, 255));
        embed.setFooter(this.dateTime);

        return embed.build();

    }

    public String getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
