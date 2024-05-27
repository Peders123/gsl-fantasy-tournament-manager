package com.glaeriasmite.fantasy.bot;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*; 

public class App {

    public static OptionType getOptionType(int option) {

        switch (option) {
            case 3:
                return STRING;
            case 4:
                return INTEGER;
            default:
                return UNKNOWN;
        }

    }

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

        /* commands.addCommands(
            Commands.slash("test", "testing the bot")
                .addOption(STRING, "content", "TEST", true)
                .addOption(STRING, "test", "test", false)
        );

        commands.addCommands(
            Commands.slash("button", "testing buttons")
        ); */

        JsonNode arrayNode = objectMapper.readTree(new File(
            "src/main/java/com/glaeriasmite/fantasy/bot/commands/slashCommands.json"
        ));

        SlashCommandData slash_command;
        
        for (JsonNode commandNode : arrayNode) {

            slash_command = Commands.slash(commandNode.get("name").asText(), commandNode.get("description").asText());

            for (JsonNode optionNode : commandNode.get("options")) {
                slash_command.addOption(
                    App.getOptionType(optionNode.get("type").asInt()),
                    optionNode.get("name").asText(),
                    optionNode.get("description").asText(),
                    optionNode.get("required").asBoolean()
                );
            }

            slash_command.setGuildOnly(commandNode.get("guildOnly").asBoolean());
            slash_command.setDefaultPermissions(commandNode.get("adminOnly").asBoolean() ?
                DefaultMemberPermissions.DISABLED : DefaultMemberPermissions.ENABLED
            );

            commands.addCommands(
                slash_command
            );
        }

        commands.queue();

        System.out.println("Commands created");

    }

}