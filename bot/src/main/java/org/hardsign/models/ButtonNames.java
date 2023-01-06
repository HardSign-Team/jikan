package org.hardsign.models;

public enum ButtonNames {
    ACTIVITIES(Emoji.Clipboard + " Активности"),
    CREATE_ACTIVITY(Emoji.Memo + " Создать"),
    ACCEPT_DELETE(Emoji.Ok + " Да"),
    CANCEL_DELETE(Emoji.RedCross + " Нет"),
    START_TIMESTAMP(Emoji.Play + " Старт"),
    STOP_TIMESTAMP(Emoji.Stop + " Стоп"),
    BACK(Emoji.Back + " Вернуться"),
    STATISTICS(Emoji.UpwardChart + " Статистика"),
    CURRENT_MONTH_STATISTICS(Emoji.Calendar + " Текущий месяц"),
    CUSTOM_DATE_STATISTICS(Emoji.Calendar + " Указать период");

    private final String name;

    ButtonNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
