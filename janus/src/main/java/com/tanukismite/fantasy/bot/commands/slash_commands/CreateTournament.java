package com.tanukismite.fantasy.bot.commands.slash_commands;

import com.tanukismite.fantasy.bot.commands.Context;
import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.handlers.Components;
import com.tanukismite.fantasy.bot.handlers.Handler;
import com.tanukismite.fantasy.bot.signup.TournamentData;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.requests.FluentRestAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ModalCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class CreateTournament implements Command {
    
    private SlashCommandInteractionEvent event;

    public CreateTournament(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute(Handler handler) {

        Context context = handler.getContext();

        String modalId = event.getUser().getId() + ":tournament-modal";

        TextInput[] inputs = new TextInput[3];

        inputs[0] = TextInput.create("datetime", "Date", TextInputStyle.SHORT)
            .setPlaceholder("%Y-%m-%dT%D:%M:%SZ")
            .setMinLength(10)
            .setMaxLength(30)
            .build();

        inputs[1] = TextInput.create("title", "Title", TextInputStyle.SHORT)
            .setPlaceholder("Tanuki Smite League - Fantasy Tournament")
            .setMinLength(5)
            .setMaxLength(100)
            .build();

        inputs[2] = TextInput.create("description", "Description", TextInputStyle.PARAGRAPH)
            .setPlaceholder("Brief summary of the tournament")
            .setMinLength(40)
            .setMaxLength(1000)
            .build();

        event.replyModal(Components.createModal(modalId, modalId, inputs)).queue();

        context.setTournamentRoot(this);

    }

    public void submitModal(Handler handler, ModalInteractionEvent modalEvent) {

        TournamentData data = new TournamentData();

        data.setDateTime(modalEvent.getValue("datetime").getAsString());
        data.setTitle(modalEvent.getValue("title").getAsString());
        data.setDescription(modalEvent.getValue("description").getAsString());
        
        try {
            handler.getCommunicator("tournament").post(data);
        } catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
        }

        modalEvent.replyEmbeds(data.toEmbed()).queue();

    }

}
