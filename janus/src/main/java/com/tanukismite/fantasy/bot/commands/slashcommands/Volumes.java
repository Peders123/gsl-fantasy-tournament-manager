package com.tanukismite.fantasy.bot.commands.slashcommands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.handlers.Handler;


public class Volumes implements Command {

    private static final Logger logger = LogManager.getLogger("ConsoleLogger");
    
    private SlashCommandInteractionEvent event;

    public Volumes(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute(Handler handler) {

        String testMessage = event.getOption("testmessage").getAsString();
        StringBuilder message = new StringBuilder("Error");

        File f = new File("mount/janus/test_dir/test_file.txt");

        if (f.exists()) {
            try {
                Scanner reader = new Scanner(f);
                message = new StringBuilder("");
                while (reader.hasNextLine()) {
                    message.append(reader.nextLine());
                }
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("mount/janus/test_dir/test_file.txt"));
                writer.write(testMessage);
                writer.close();
                message.append("Written to file.");
            } catch (IOException error) {
                logger.error("Could not write to test file.", error);
            }
        }
        
        event.reply(message.toString()).queue();

    }

}
