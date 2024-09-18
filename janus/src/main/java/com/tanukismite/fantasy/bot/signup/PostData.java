package com.tanukismite.fantasy.bot.signup;

import java.util.Map;

import net.dv8tion.jda.api.entities.MessageEmbed;

/**
 * The {@code PostData} interface defines methods that should be implemented by classes 
 * representing data objects to be posted in a signup or tournament system.
 * 
 * <p>Implementing classes should provide functionality for converting the data to a 
 * {@link Map} for easier storage and a {@link MessageEmbed} for rendering the data as a 
 * rich Discord embed message.
 * </p>
 * 
 * @author Rory Caston
 * @since 1.0
 */
public interface PostData {

    /**
     * Converts the implementing class's data into a map for processing.
     * @return A {@link Map} representing the data in key-value pairs.
     */
    Map<String, Object> toMap();

    /**
     * Converts the implementing class's data into a {@link MessageEmbed} for rich display in
     * Discord messages.
     * @return A {@link MessageEmbed} representation of the data.
     */
    MessageEmbed toEmbed();

}