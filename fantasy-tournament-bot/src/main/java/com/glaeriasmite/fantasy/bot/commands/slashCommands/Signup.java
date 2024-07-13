package com.glaeriasmite.fantasy.bot.commands.slashCommands;

import java.lang.reflect.Method;

import com.glaeriasmite.fantasy.bot.Role;
import com.glaeriasmite.fantasy.bot.commands.Command;
import com.glaeriasmite.fantasy.bot.commands.Context;
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

        /* SignupData data = new SignupData(this);

        data.setRole1(Role.ADC);
        data.setRole2(Role.SUPPORT);

        context.putUserSignupData(this.event.getUser().getId(), data);

        SelectOption[] options = Components.createSelectOptions("role1");

        StringSelectMenu selection = Components.createSelectMenu("role1", options);

        FluentRestAction<InteractionHook, ReplyCallbackAction> action = Action.replyWithMessage(event, "TESTING");

        Components.addActionRowReply(action, selection);

        this.queue(action); */
 
    }

    @Override
    public void executeMethod(String methodName, Context context, Object... params) throws Exception {

        Method method = Signup.class.getDeclaredMethod(methodName, Context.class, Object[].class);
        method.invoke(this, context, new Object[] {params});

    }

    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {

        request.queue();

    }

}
