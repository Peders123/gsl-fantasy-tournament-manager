package com.tanukismite.fantasy.bot;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tanukismite.fantasy.bot.communicators.CaptainCommunicator;
import com.tanukismite.fantasy.bot.communicators.PlayerCommunicator;
import com.tanukismite.fantasy.bot.communicators.TournamentCommunicator;
import com.tanukismite.fantasy.bot.communicators.UserCommunicator;
import com.tanukismite.fantasy.bot.handlers.Handler;
import com.tanukismite.fantasy.bot.listeners.ButtonListener;
import com.tanukismite.fantasy.bot.listeners.ModalListener;
import com.tanukismite.fantasy.bot.listeners.SlashCommandListener;
import com.tanukismite.fantasy.bot.listeners.StringSelectListener;


/**
 * The {@code App} class initialises the Discord bot, configures the JDA (Java Discord API)
 * instance, and registers various event listeners and slash commands. It is the main entry point
 * for running the bot and handles the bot's online status, activity, and command registration.
 *
 * <p>It reads necessary configuration (such as bot tokens and commands) from external JSON files
 * and sets up appropriate handlers and communicators for user interactions via the bot.</p>
 *
 * @author Rory Caston
 * @since 1.0
 */
public class App {

    private static final Logger consoleLogger = LogManager.getLogger("ConsoleLogger");
    private static final Logger fileLogger = LogManager.getLogger("FileLogger");

    /**
     * Returns the appropriate {@link OptionType} based on the provided integer value.
     *
     * @param option The integer value representing the option type.
     * @return The corresponding {@code OptionType}.
     */
    public static OptionType getOptionType(int option) {

        switch (option) {
            case 3:
                return OptionType.STRING;
            case 4:
                return OptionType.INTEGER;
            default:
                return OptionType.UNKNOWN;
        }

    }

    /**
     * Main method to initialize the bot, configure its commands and event listeners, and 
     * start the bot session using the JDA API.
     *
     * @param args Command line arguments (not used in this implementation).
     * @throws IOException if there is an issue reading configuration files.
     */
    public static void main(String[] args) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode secretNode = objectMapper.readTree(new File("secrets.json"));
        
        String token = secretNode.get("tokens").get("discord").asText();

        // Start bot
        JDABuilder build = JDABuilder.createDefault(token);
        build.setActivity(Activity.playing("Donkey Kong Country"));
        build.setStatus(OnlineStatus.ONLINE);
        JDA jda = build.enableIntents(GatewayIntent.MESSAGE_CONTENT).build();

        // Add listeners
        Handler handler = new Handler(jda);
        jda.addEventListener(new ButtonListener(handler));
        jda.addEventListener(new ModalListener(handler));
        jda.addEventListener(new SlashCommandListener(handler));
        jda.addEventListener(new StringSelectListener(handler));

        // Add communicators
        handler.addCommunicator("user", new UserCommunicator());
        handler.addCommunicator("player", new PlayerCommunicator());
        handler.addCommunicator("tournament", new TournamentCommunicator());
        handler.addCommunicator("captain", new CaptainCommunicator());

        CommandListUpdateAction commands = jda.updateCommands();

        JsonNode arrayNode = objectMapper.readTree(new File(
            "src/main/java/com/tanukismite/fantasy/bot/config/slashCommands.json"
        ));

        SlashCommandData slashCommand;
        
        // Load commands
        for (JsonNode commandNode : arrayNode) {

            slashCommand = Commands.slash(commandNode.get("name").asText(), commandNode.get("description").asText());

            for (JsonNode optionNode : commandNode.get("options")) {
                slashCommand.addOption(
                    App.getOptionType(optionNode.get("type").asInt()),
                    optionNode.get("name").asText(),
                    optionNode.get("description").asText(),
                    optionNode.get("required").asBoolean()
                );
            }

            slashCommand.setGuildOnly(commandNode.get("guildOnly").asBoolean());
            slashCommand.setDefaultPermissions(commandNode.get("adminOnly").asBoolean()
                ? DefaultMemberPermissions.DISABLED
                : DefaultMemberPermissions.ENABLED
            );

            commands.addCommands(
                slashCommand
            );
        }

        commands.queue();

        fileLogger.info("Commands created");
        consoleLogger.info("Commands created");

    }

}