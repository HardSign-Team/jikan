package org.hardsign.factories;

import org.hardsign.models.settings.BotDatabaseSettings;
import org.hardsign.models.users.UserStateEntity;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.jetbrains.annotations.NotNull;

public class HibernateSessionFactoryFactory {
    public static SessionFactory create(BotDatabaseSettings settings) throws HibernateException {
        System.out.println("Start debugging with prints...");

        var configuration = new Configuration()
                .setProperty("hibernate.connection.driver_class", org.postgresql.Driver.class.getName())
                .setProperty("hibernate.connection.url", createConnectionString(settings))
                .setProperty("hibernate.hbm2ddl.auto", "update")
                .addAnnotatedClass(UserStateEntity.class);

        if (settings.getDatabaseUrl() == null)
            configuration
                    .setProperty("hibernate.connection.username", settings.getUser())
                    .setProperty("hibernate.connection.password", settings.getPassword());

        System.out.println("configuration build");

        var builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());

        System.out.println("builder created");

        return configuration.buildSessionFactory(builder.build());
    }

    @NotNull
    private static String createConnectionString(BotDatabaseSettings settings) {
        var databaseUrl = settings.getDatabaseUrl();
        System.out.println(databaseUrl);
        // note (lunev.d): this means that we are launching on heroku
        if (databaseUrl != null)
            return databaseUrl;

        var host = settings.getHost();
        var port = settings.getPort();
        var databaseName = settings.getDatabaseName();
        return "jdbc:postgresql://" + host + ":" + port + "/" + databaseName;
    }
}
