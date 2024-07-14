package com.tanukismite.fantasy.bot.commands.slashCommands;

import java.io.IOException;
import java.lang.reflect.Method;

import com.tanukismite.fantasy.bot.commands.Command;
import com.tanukismite.fantasy.bot.communicators.MercuryCommunicator;
import com.tanukismite.fantasy.bot.handlers.Handler;
import com.tanukismite.fantasy.bot.signup.UserSignupData;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.FluentRestAction;

public class Delete implements Command {
    
    private SlashCommandInteractionEvent event;

    public Delete(SlashCommandInteractionEvent event) {

        this.event = event;

    }

    @Override
    public void execute(Handler handler) {

        UserSignupData data = new UserSignupData(this.event.getUser().getId(), this.event.getUser().getName());

        MercuryCommunicator communicator = handler.getCommunicator("user");

        try {
            boolean success = communicator.post(data);
            System.out.println(success);
            System.out.println(communicator.get().toString());
            success = communicator.delete(Long.parseLong(data.getId()));
            System.out.println(success);
            System.out.println(communicator.get().toString());
        } catch (IOException e) {
            System.out.println("ERROR");
            e.printStackTrace();
        }

    }

    @Override
    public void executeMethod(String methodName, Handler handler, Object... params) throws Exception {

        Method method = User.class.getDeclaredMethod(methodName, Handler.class, Object[].class);
        method.invoke(this, handler, new Object[] {params});

    }

    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {

        request.queue();

    }

}
