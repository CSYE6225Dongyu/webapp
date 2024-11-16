package edu.neu.csye6225.csye6225fall2024.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class DatabaseInitializationService implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseInitializationService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void run(String... args) throws Exception {
//        jdbcTemplate.execute("CREATE DATABASE IF NOT EXISTS myapp");
//
//        jdbcTemplate.execute("USE myapp");

        // users table
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users (" +
                "id CHAR(36) PRIMARY KEY," +
                "email VARCHAR(255) UNIQUE NOT NULL," +
                "password VARCHAR(255) NOT NULL," +
                "first_name VARCHAR(255)," +
                "last_name VARCHAR(255)," +
                "account_created DATETIME," +
                "account_updated DATETIME," +
                "is_verified BOOLEAN DEFAULT FALSE" +
                ")");

        // image metadata table
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS images_metadata (" +
                "id CHAR(36) PRIMARY KEY," +
                "user_id CHAR(36) NOT NULL," +
                "file_name VARCHAR(255) NOT NULL," +
                "url VARCHAR(255) NOT NULL," +
                "upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)" +
                ")");

        // verification info table
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS email_verification (" +
                "id CHAR(36) PRIMARY KEY," +
                "user_id CHAR(36) NOT NULL," +
                "token VARCHAR(255) NOT NULL," +
                "expires_at DATETIME NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "CONSTRAINT fk_verification_user FOREIGN KEY (user_id) REFERENCES users(id)" + // 外键关联 users 表
                ")");

        // trigger , delete expired data when insert
        jdbcTemplate.execute("DROP TRIGGER IF EXISTS clean_expired_verifications;");


    }
}
