package com.glaeriasmite.fantasy.bot;

public enum Role {

    ADC("1258183526050824262", "hunter"),
    SUPPORT("1258183794289283152", "guardian"),
    MID("1258183814220615820", "mage"),
    JUNGLE("1258183803722137611", "assassin"),
    SOLO("1258183823615725740", "warrior"),
    FILL("762432341838397440", "Fill");

    private final String emoteId;
    private final String emoteName;

    private Role(String emoteId, String emoteName) {
        this.emoteId = emoteId;
        this.emoteName = emoteName;
    }

    public String getEmoteId() {
        return this.emoteId;
    }

    public String getEmoteName() {
        return this.emoteName;
    }

    public String getFormattedEmote() {
        return "<:" + this.emoteName + ":" + this.emoteId + ">";
    }

}
