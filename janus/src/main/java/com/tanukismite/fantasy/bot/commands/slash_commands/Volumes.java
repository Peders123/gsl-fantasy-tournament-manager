package com.tanukismite.fantasy.bot.commands.slash_commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.handlers.Handler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Volumes implements Command {
    
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
            } catch (IOException e) {
                System.out.println("Could not write to test file");
                e.printStackTrace();
            }
        }
        
        event.reply(message.toString()).queue();

    }

}
