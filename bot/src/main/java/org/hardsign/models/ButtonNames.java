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
    CUSTOM_DATE_STATISTICS(Emoji.Calendar + " Указать период"),
    CURRENT_DAY_STATISTICS(Emoji.Calendar + " Сегодня"),
    LAST_START_STATISTICS(Emoji.Calendar + " Последний старт"),
    TIME_SINCE_LAST_START(Emoji.Calendar + " Время трекинга"),
    ACTIVITY_MENU(Emoji.Gear + " Меню активности"),
    ADD_TIMESTAMP(Emoji.Memo + " Добавить фиксацию"),
    TIMESTAMPS(Emoji.Memo + " Фиксации")
    ;

    private final String name;

    ButtonNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
