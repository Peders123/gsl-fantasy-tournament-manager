package com.tanukismite.fantasy.bot;


/**
 * The {@code Role} enum represents different roles in the game SMITE.
 * Each role is associated with an emote ID and an emote name that can be
 * used in Discord. The enum provides utility methods to access these values 
 * and return a formatted emote string.
 * 
 * <p>Roles are commonly used for player selection and designation within
 * the game, and this enum facilitates linking game roles to Discord emotes.</p>
 * 
 * <ul>
 *     <li>{@link #ADC} - The Hunter role (Attack Damage Carry).</li>
 *     <li>{@link #SUPPORT} - The Guardian role, providing team support.</li>
 *     <li>{@link #MID} - The Mage role, typically in the middle lane.</li>
 *     <li>{@link #JUNGLE} - The Assassin role, focusing on jungle objectives.</li>
 *     <li>{@link #SOLO} - The Warrior role, usually in the solo lane.</li>
 *     <li>{@link #FILL} - A flexible role to fill in for any position.</li>
 * </ul>
 *
 * @author Rory Caston
 * @since 1.0
 */
public enum Role {

    ADC("1258183526050824262", "hunter"),
    SUPPORT("1258183794289283152", "guardian"),
    MID("1258183814220615820", "mage"),
    JUNGLE("1258183803722137611", "assassin"),
    SOLO("1258183823615725740", "warrior"),
    FILL("762432341838397440", "Fill");

    private final String emoteId;
    private final String emoteName;

    /**
     * Constructs a {@code Role} with the specified emote ID and name.
     *
     * @param emoteId The Discord emote ID for the role.
     * @param emoteName The name of the emote.
     */
    private Role(String emoteId, String emoteName) {
        this.emoteId = emoteId;
        this.emoteName = emoteName;
    }

    /**
     * Getter for emoteId.
     *
     * @return The current emoteId.
     */
    public String getEmoteId() {
        return this.emoteId;
    }

    /**
     * Getter for emoteName.
     *
     * @return The current emoteName.
     */
    public String getEmoteName() {
        return this.emoteName;
    }

    /**
     * Returns the emote in a format to be used in discord messages. The format is:
     * {@code <emote_name:emote_id>}, which allows rendering of custom emotes.
     *
     * @return The formatted emote as a {@link String}.
     */
    public String getFormattedEmote() {
        return "<:" + this.emoteName + ":" + this.emoteId + ">";
    }

}
