package com.tanukismite.fantasy.bot.signup;

import java.util.Map;

import net.dv8tion.jda.api.entities.MessageEmbed;

public interface PostData {

    Map<String, Object> toMap();
    MessageEmbed toEmbed();

}