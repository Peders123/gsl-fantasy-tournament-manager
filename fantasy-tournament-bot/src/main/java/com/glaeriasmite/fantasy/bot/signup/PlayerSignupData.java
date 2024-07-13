package com.glaeriasmite.fantasy.bot.signup;

import java.awt.Color;

import com.glaeriasmite.fantasy.bot.commands.slashCommands.CreateSignups;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class PlayerSignupData extends SignupData {

    private String smiteGuru;

    public PlayerSignupData(CreateSignups currentSession) {
        super(currentSession);
    }


    public String getSmiteGuru() {
        return this.smiteGuru;
    }

    public void setSmiteGuru(String smiteGuru) {
        this.smiteGuru = smiteGuru;
    }

    @Override
    public MessageEmbed toEmbed() {
        
        EmbedBuilder embed = new EmbedBuilder()
            .setAuthor(this.discord.toUpperCase())
            .setTitle("Signup Data")
            .addField("Username", this.ign, false)
            .addField("Primary Role", this.role1.getFormattedEmote() + " " + this.role1.name(), true)
            .addField("Secondary Role", this.role2.getFormattedEmote() + " " + this.role2.name(), true)
            .setColor(new Color(28, 19, 31, 255))
            .setFooter(this.smiteGuru);

        return embed.build();

    }

}
