package org.hardsign.factories;

import org.hardsign.models.settings.BotDatabaseSettings;
import org.hardsign.models.users.UserStateEntity;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;

public class HibernateSessionFactoryFactory {
    public static SessionFactory create(BotDatabaseSettings settings) throws HibernateException {
        var configuration = new Configuration()
                .setProperty("hibernate.connection.driver_class", org.postgresql.Driver.class.getName())
                .setProperty("hibernate.connection.url", createConnectionString(settings))
                .setProperty("hibernate.connection.username", settings.getUser())
                .setProperty("hibernate.connection.password", settings.getPassword())
                .setProperty("hibernate.hbm2ddl.auto", "update")
                .addAnnotatedClass(UserStateEntity.class);

        var builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());

        return configuration.buildSessionFactory(builder.build());
    }

    @NotNull
    private static String createConnectionString(BotDatabaseSettings settings) {
        var databaseUrl = settings.getDatabaseUrl();
        System.out.println(databaseUrl);
        // note (lunev.d): this means that we are launching on heroku
        if (databaseUrl != null)
            return getConnectionStringFromHerokuUri(settings);

        var host = settings.getHost();
        var port = settings.getPort();
        var databaseName = settings.getDatabaseName();
        return "jdbc:postgresql://" + host + ":" + port + "/" + databaseName;
    }

    private static String getConnectionStringFromHerokuUri(BotDatabaseSettings settings) {
        try {
            var dbUri = new URI(settings.getDatabaseUrl());
            var username = dbUri.getUserInfo().split(":")[0];
            var password = dbUri.getUserInfo().split(":")[1];
            settings.setUser(username);
            settings.setPassword(password);
            return "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        }
        catch (URISyntaxException e){
            return null;
        }
    }
}
