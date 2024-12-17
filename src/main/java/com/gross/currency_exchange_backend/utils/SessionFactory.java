package com.gross.currency_exchange_backend.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SessionFactory {

    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://db:5432/currency_db"); // URL вашей базы данных
        config.setUsername("myuser"); // Имя пользователя
        config.setPassword("mypassword"); // Пароль
        config.setDriverClassName("org.postgresql.Driver"); // Класс драйвера PostgreSQL

        // Настройки пула
        config.setMaximumPoolSize(10); // Максимальное количество соединений в пуле
        config.setMinimumIdle(2);     // Минимальное количество простаивающих соединений
        config.setIdleTimeout(30000); // Тайм-аут простоя соединений (в миллисекундах)
        config.setMaxLifetime(1800000); // Максимальное время жизни соединения (в миллисекундах)
        config.setConnectionTimeout(30000); // Тайм-аут ожидания соединения из пула

        dataSource = new HikariDataSource(config);
    }

    // Метод для получения соединения
    public static Connection getConnection() throws  SQLException {
        return dataSource.getConnection();
    }

    // Метод для закрытия пула соединений
    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}


