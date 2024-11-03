package com.tanukismite.fantasy.bot.handlers;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.Nullable;


/**
 * This class is a utility class to support Java Discord components.
 *
 * <p>It provides methods for creating components such as: select menus, modals, and select options
 * for use in interactions with the Discord API. This class primarily interacts with JSON config
 * files to dynamically build options based on pre-defined values.</p>
 *
 * <p><b>Note:</b> This class is not intended to be instantiated.</p>
 *
 * @author Rory Caston
 * @since 1.0
 */
public class Components {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Components() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Creates a {@link SelectOption} from the given {@link JsonNode} data. The data is expected to
     * contain fields for the option's label, value, default status, description, and emoji.
     *
     * @param data A {@link JsonNode} containing the option's details.
     * @return A constructed {@link SelectOption} based on the provided JSON data.
     */
    private static SelectOption createSelectOptionFromJson(JsonNode data) {

        return SelectOption.of(data.get("label").asText(), data.get("value").asText())
            .withDefault(data.get("isDefault").asBoolean())
            .withDescription(data.get("description").asText())
            .withEmoji(Emoji.fromFormatted(data.get("emoji").asText()));

    }

    /**
     * Reads a JSON config file containing select options and returns an array of
     * {@link SelectOptions} for the specified key.
     *
     * @param key The key for which select options should be retrieved.
     * @return An array of {@link SelectOption} objects, or {@code null} if an error occurs.
     */
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

        // Forms the options into useable data.
        JsonNode optionKey = json.get(key);
        SelectOption[] options = new SelectOption[optionKey.size()];
        for (int i = 0; i < optionKey.size(); i++) {
            options[i] = createSelectOptionFromJson(optionKey.get(i));
        }

        return options;

    }

    /**
     * Creates a {@link StringSelectMenu} with the given ID and an array of {@link SelectOption}.
     * This menu allows users to select from pre-defined options for a Discord interaction.
     *
     * @param id      The ID of the select menu.
     * @param options The list of {@link SelectOption} to be included in the menu.
     * @return A constructed {@link StringSelectMenu} object.
     */
    public static StringSelectMenu createSelectMenu(String id, SelectOption... options) {

        return StringSelectMenu.create(id).addOptions(options).build();

    }

    /**
     * Creates a {@link Modal} with the given ID, title, and input fields. The modal can be used
     * to collect text inputs from users during Discord interactions.
     *
     * @param id     The unique ID of the modal.
     * @param title  The title of the modal.
     * @param inputs The {@link TextInput} fields to include in the modal.
     * @return A constructed {@link Modal} object.
     */
    public static Modal createModal(String id, String title, TextInput... inputs) {

        Modal.Builder modal = Modal.create(id, title);

        for (TextInput input : inputs) {
            modal.addActionRow(input);
        }

        return modal.build();

    }

}