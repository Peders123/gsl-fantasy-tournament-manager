package com.tanukismite.fantasy.bot.signup;

import java.awt.Color;
import java.util.Map;

import com.tanukismite.fantasy.bot.commands.slash_commands.CreateSignups;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class CaptainSignupData extends SignupData {

    private String reason;
    private String teamName;

    public CaptainSignupData() {
        System.out.println("ERROR");
    }

    public CaptainSignupData(CreateSignups currentSession) {
        super(currentSession);
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public MessageEmbed toEmbed() {
        
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Embed testing command");
        embed.setDescription("GLAERIA SMITE LEAGUE FANTASY TOURNAMENT\n @peders");
        embed.setColor(new Color(28, 19, 31, 255));
        embed.setFooter("June 14th");

        return embed.build();

    }

    @Override
    public Map<String, Object> toMap() {

        Map<String, Object> map = super.toMap();
        map.put("reason", this.reason);
        map.put("team_name", this.teamName);

        return map;

    }

}
