package com.tanukismite.fantasy.bot.signup;

import java.awt.Color;

import com.tanukismite.fantasy.bot.commands.slashCommands.CreateSignups;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class CaptainSignupData extends SignupData {

    private String reason;

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

    @Override
    public MessageEmbed toEmbed() {
        
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Embed testing command");
        embed.setDescription("GLAERIA SMITE LEAGUE FANTASY TOURNAMENT\n @peders");
        embed.setColor(new Color(28, 19, 31, 255));
        embed.setFooter("June 14th");

        return embed.build();

    }

}
