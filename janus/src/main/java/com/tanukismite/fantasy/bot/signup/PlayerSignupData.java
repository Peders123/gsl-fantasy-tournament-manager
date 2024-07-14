package com.tanukismite.fantasy.bot.signup;

import java.awt.Color;
import java.util.Map;

import com.tanukismite.fantasy.bot.Role;
import com.tanukismite.fantasy.bot.commands.slashCommands.CreateSignups;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class PlayerSignupData extends SignupData {

    private String smiteGuru;
    protected Role role1;
    protected Role role2;

    public PlayerSignupData(CreateSignups currentSession) {
        super(currentSession);
    }


    public String getSmiteGuru() {
        return this.smiteGuru;
    }

    public void setSmiteGuru(String smiteGuru) {
        this.smiteGuru = smiteGuru;
    }

    public Role getRole1() {
        return this.role1;
    }

    public void setRole1(Role role1) {
        this.role1 = role1;
    }

    public Role getRole2() {
        return this.role2;
    }

    public void setRole2(Role role2) {
        this.role2 = role2;
    }

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
