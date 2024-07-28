package com.tanukismite.fantasy.bot;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanukismite.fantasy.bot.communicators.CaptainCommunicator;
import com.tanukismite.fantasy.bot.communicators.PlayerCommunicator;
import com.tanukismite.fantasy.bot.communicators.TournamentCommunicator;
import com.tanukismite.fantasy.bot.communicators.UserCommunicator;
import com.tanukismite.fantasy.bot.handlers.Handler;
import com.tanukismite.fantasy.bot.listeners.*;
import com.tanukismite.fantasy.bot.signup.UserSignupData;

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
        JsonNode secretNode = objectMapper.readTree(new File("secrets.json"));
        
        String token = secretNode.get("tokens").get("discord").asText();

        JDABuilder build = JDABuilder.createDefault(token);
        build.setActivity(Activity.playing("Donkey Kong Country"));
        build.setStatus(OnlineStatus.ONLINE);

        JDA jda = build.enableIntents(GatewayIntent.MESSAGE_CONTENT)
            .build();

        Handler handler = new Handler();
        jda.addEventListener(new ButtonListener(handler));
        jda.addEventListener(new MessageListener(handler));
        jda.addEventListener(new ModalListener(handler));
        jda.addEventListener(new SlashCommandListener(handler));
        jda.addEventListener(new StringSelectListener(handler));

        handler.addCommunicator("user", new UserCommunicator());
        handler.addCommunicator("player", new PlayerCommunicator());
        handler.addCommunicator("tournament", new TournamentCommunicator());
        handler.addCommunicator("captain", new CaptainCommunicator());

        CommandListUpdateAction commands = jda.updateCommands();

        JsonNode arrayNode = objectMapper.readTree(new File(
            "src/main/java/com/tanukismite/fantasy/bot/config/slashCommands.json"
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

        UserSignupData data = new UserSignupData("123456", "peedly");

        JsonNode response = handler.getCommunicator("user").get();
        System.out.println(response.toString());

        handler.getCommunicator("user").post(data);

        response = handler.getCommunicator("user").get();
        System.out.println(response.toString());

        response = handler.getCommunicator("captain").get();
        System.out.println(response.toString());

    }

}