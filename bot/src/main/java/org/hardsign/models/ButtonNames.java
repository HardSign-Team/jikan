package org.hardsign.models;

public enum ButtonNames {
    ACTIVITIES("Активности"),
    CREATE_ACTIVITY("Создать"),
    UNSELECT_ACTIVITY("Отменить"),
    START_TIMESTAMP("Старт"),
    STOP_TIMESTAMP("Стоп"),
    BACK("Вернуться"),
    ;

    private final String name;

    ButtonNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
