package com.glaeriasmite.fantasy.bot.listeners;

import com.glaeriasmite.fantasy.bot.commands.slashCommands.Edit;
import com.glaeriasmite.fantasy.bot.handlers.Handler;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class ButtonListener extends BaseListener {

    public ButtonListener(Handler handler) {
        super(handler);
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        String[] id = event.getComponentId().split(":");
        String type = id[1];

        event.deferEdit().queue();

        switch (type) {

            case "test":
                System.out.println("TEST");
                break;

            case "bad":
                System.out.println("BAD");
                break;

            case "edit":
                this.edit(event);
                break;

            case "delete":
                this.delete(event);
                break;

        }

    }

    private void edit(ButtonInteractionEvent event) {

        try {
            this.handler.executeMethod(
                new Edit(null),
                "editMessage",
                event.getMessageChannel(),
                event.getMessageId()
            );
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e);
        }

    }

    private void delete(ButtonInteractionEvent event) {

        try {
            this.handler.executeMethod(
                new Edit(null),
                "deleteMessage",
                event.getMessageChannel(),
                event.getMessageId()
            );
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e);
        }

    }

}
