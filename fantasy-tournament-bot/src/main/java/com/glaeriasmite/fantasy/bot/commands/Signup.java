package com.glaeriasmite.fantasy.bot.commands;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.glaeriasmite.fantasy.bot.HttpHandler;
import com.glaeriasmite.fantasy.bot.Role;
import com.glaeriasmite.fantasy.bot.SignupData;
import com.glaeriasmite.fantasy.bot.handlers.Action;
import com.glaeriasmite.fantasy.bot.handlers.Components;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.requests.FluentRestAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class Signup implements Command {

    private SlashCommandInteractionEvent event;

    public Signup(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute(Context context) {

        SignupData data = new SignupData();

        data.setRole1(Role.ADC);
        data.setRole2(Role.SUPPORT);

        context.putUserSignupData(this.event.getUser().getId(), data);

        SelectOption[] options = Components.createSelectOptions("role1");

        StringSelectMenu selection = Components.createSelectMenu("role1", options);

        FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithMessage(event, "TESTING");

        Action.addActionRow(action, selection);

        this.queue(action);
 
    }

    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {

        request.queue();

    }

}
