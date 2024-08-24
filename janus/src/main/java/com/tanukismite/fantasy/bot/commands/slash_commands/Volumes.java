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
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.FluentRestAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class Volumes implements Command {
    
    private SlashCommandInteractionEvent event;

    public Volumes(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute(Handler handler) {

        String testMessage = event.getOption("testmessage").getAsString();
        String message = "Error";

        File f = new File("mount/janus/test_dir/test_file.txt");

        if (f.exists()) {
            try {
                Scanner reader = new Scanner(f);
                message = "";
                while (reader.hasNextLine()) {
                    String data = reader.nextLine();
                    message += data;
                }
                reader.close();
            } catch (FileNotFoundException e) {}
        } else {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("mount/janus/test_dir/test_file.txt"));
                writer.write(testMessage);
                writer.close();
                message = "Written to file.";
            } catch (IOException e) {
                System.out.println("Could not write to test file");
                e.printStackTrace();
            }
        }
        
        event.reply(message).queue();

    }

}
