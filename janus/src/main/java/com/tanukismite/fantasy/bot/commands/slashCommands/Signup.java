package com.tanukismite.fantasy.bot.commands.slashCommands;

import java.lang.reflect.Method;

import com.tanukismite.fantasy.bot.Role;
import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.commands.Context;
import com.tanukismite.fantasy.bot.handlers.Action;
import com.tanukismite.fantasy.bot.handlers.Components;
import com.tanukismite.fantasy.bot.handlers.Handler;

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
    public void execute(Handler handler) {

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
    public void executeMethod(String methodName, Handler handler, Object... params) throws Exception {

        Context context = handler.getContext();

        Method method = Signup.class.getDeclaredMethod(methodName, Handler.class, Object[].class);
        method.invoke(this, handler, new Object[] {params});

    }

    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {

        request.queue();

    }

}
