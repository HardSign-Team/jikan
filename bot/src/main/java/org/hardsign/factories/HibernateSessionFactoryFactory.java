package org.hardsign.factories;

import org.hardsign.models.settings.BotDatabaseSettings;
import org.hardsign.models.users.UserStateEntity;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.jetbrains.annotations.NotNull;

public class HibernateSessionFactoryFactory {
    public static SessionFactory create(BotDatabaseSettings settings) {
        var configuration = new Configuration()
                .setProperty("hibernate.connection.driver_class", org.postgresql.Driver.class.getName())
                .setProperty("hibernate.connection.username", settings.getUser())
                .setProperty("hibernate.connection.password", settings.getPassword())
                .setProperty("hibernate.connection.url", createConnectionString(settings))
                .setProperty("hibernate.hbm2ddl.auto", "update")
                .addAnnotatedClass(UserStateEntity.class);

        var builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());

        return configuration.buildSessionFactory(builder.build());
    }

    @NotNull
    private static String createConnectionString(BotDatabaseSettings settings) {
        var host = settings.getHost();
        var port = settings.getPort();
        var databaseName = settings.getDatabaseName();
        return "jdbc:postgresql://" + host + ":" + port + "/" + databaseName;
    }
}
