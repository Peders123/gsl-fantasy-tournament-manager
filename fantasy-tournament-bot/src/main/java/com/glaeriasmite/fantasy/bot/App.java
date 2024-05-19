package com.glaeriasmite.fantasy.bot;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*; 

public class App {
    public static void main(String[] args) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(new File("../config.json"));
        String token = jsonNode.get("discord_token").asText();

        JDABuilder build = JDABuilder.createDefault(token);
        build.setActivity(Activity.watching("playing Donkey Kong Country"));
        build.setStatus(OnlineStatus.ONLINE);

        JDA jda = build.enableIntents(GatewayIntent.MESSAGE_CONTENT)
            .build();

        jda.addEventListener(new MessageListener()); 

        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(
            Commands.slash("test", "testing the bot")
                .addOption(STRING, "content", "TEST", true)
        );

        commands.queue();

        System.out.println("Commands created");

    }

}