package com.glaeriasmite.fantasy.bot.commands.slashCommands;

import java.awt.Color;

import com.glaeriasmite.fantasy.bot.commands.Context;
import com.glaeriasmite.fantasy.bot.commands.ExtendedCommand;
import com.glaeriasmite.fantasy.bot.handlers.Action;
import com.glaeriasmite.fantasy.bot.handlers.Components;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.requests.FluentRestAction;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ModalCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.internal.entities.channel.concrete.TextChannelImpl;

public class CreateSignups extends ExtendedCommand {

    private SlashCommandInteractionEvent event;

    public CreateSignups(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute(Context context) {

        MessageChannel channel = event.getMessageChannel();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Embed testing command");
        embed.setDescription("GLAERIA SMITE LEAGUE FANTASY TOURNAMENT\n @peders");
        embed.setColor(new Color(28, 19, 31, 255));
        embed.setFooter("August 10th");

        FluentRestAction<InteractionHook, ReplyCallbackAction> response = Action.replyWithMessage(event, "Created!");
        FluentRestAction<Message, MessageCreateAction> action = Action.sendMessageWithEmbed(channel, embed.build());
        action = Components.addActionRowMessage(action,
            Button.primary(event.getUser().getId() + ":player-signup", "Signup"),
            Button.primary(event.getUser().getId() + ":captain-signup", "Signup (Captain)"),
            Button.secondary(event.getUser().getId() + ":players", "Players")
        );

        this.queue(response);
        this.queue(action);
 
    }

    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {

        request.queue();

    }

    protected void createModal(Context context, ButtonInteractionEvent buttonEvent, Boolean captain) {

        String modalId = captain ? "captain-signup" : "player-signup";
        String title = captain ? "Captain Signup" : "Player signup";
        TextInput[] inputs = new TextInput[2];

        inputs[0] = TextInput.create("username", "Smite IGN", TextInputStyle.SHORT)
                .setPlaceholder("Peders")
                .setMinLength(4)
                .setMaxLength(15)
                .build();

        if (captain) {

            inputs[1] = TextInput.create("reason", "Reasoning", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Why would you like to be a captain?")
                    .setMinLength(20)
                    .setMaxLength(1000)
                    .build();

        } else {

            inputs[1] = TextInput.create("guru", "Smite-guru", TextInputStyle.SHORT)
                    .setPlaceholder("Link to your Smite-guru profile.")
                    .setMinLength(15)
                    .build();

        }

        Modal modal = Action.createModal(modalId, title, inputs);
        FluentRestAction<Void, ModalCallbackAction> action = Action.replyWithModal(buttonEvent, modal);
        this.queue(action);

    }

    protected void sendTestMessage(Context context, TextChannelImpl channel) {

        try {

            FluentRestAction<Message, MessageCreateAction> action = Action.sendMessage(channel, "Not Yet Implemented");
            this.queue(action);

        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
