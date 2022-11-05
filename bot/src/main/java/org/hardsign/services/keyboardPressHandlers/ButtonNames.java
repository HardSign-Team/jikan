package org.hardsign.services.keyboardPressHandlers;

public enum ButtonNames {
    ACTIVITIES("Активности"),
    CREATE_ACTIVITY("Создать");

    private final String name;

    ButtonNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
