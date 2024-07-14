package com.tanukismite.fantasy.bot.signup;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class UserSignupData implements PostData {
    
    protected String id;
    protected String discord;

    public UserSignupData() {}

    public UserSignupData(String id, String discord) {

        this.id = id;
        this.discord = discord;

    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDiscord() {
        return this.discord;
    }

    public void setDiscord(String discord) {
        this.discord = discord;
    }
    
    @Override
    public Map<String, Object> toMap() {

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", Long.parseLong(this.id));
        map.put("discord_name", this.discord);

        return map;

    }

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