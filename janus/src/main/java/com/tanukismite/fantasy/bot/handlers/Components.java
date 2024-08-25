package com.tanukismite.fantasy.bot.handlers;

import java.io.File;
import java.io.IOException;

import org.jetbrains.annotations.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.requests.FluentRestAction;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class Components {

    private static SelectOption createSelectOptionFromJson(JsonNode data) {

        SelectOption option = SelectOption.of(data.get("label").asText(), data.get("value").asText())
            .withDefault(data.get("isDefault").asBoolean())
            .withDescription(data.get("description").asText())
            .withEmoji(Emoji.fromFormatted(data.get("emoji").asText()));

        return option;

    }

    @Nullable
    public static SelectOption[] createSelectOptions(String key) {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json;

        try {
            json = objectMapper.readTree(new File(
                "src/main/java/com/tanukismite/fantasy/bot/config/selectOptions.json"
            ));
        } catch (IOException e) {
            return null;
        }

        JsonNode optionKey = json.get(key);

        SelectOption[] options = new SelectOption[optionKey.size()];

        for (int i = 0; i < optionKey.size(); i++) {

            options[i] = createSelectOptionFromJson(optionKey.get(i));

        }

        return options;

    }

    public static StringSelectMenu createSelectMenu(String id, SelectOption... options) {

        return StringSelectMenu.create(id).addOptions(options).build();

    }

    public static Modal createModal(String id, String title, TextInput... inputs) {

        Modal.Builder modal = Modal.create(id, title);

        for (TextInput input : inputs) {
            modal.addActionRow(input);
        }

        return modal.build();

    }

}