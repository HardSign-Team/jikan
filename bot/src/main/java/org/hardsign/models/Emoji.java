package org.hardsign.models;

public enum Emoji {
    FaceWithTongue("\uD83D\uDE1B"),
    Pensive("😔")
    ;
    private final String value;

    Emoji(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
