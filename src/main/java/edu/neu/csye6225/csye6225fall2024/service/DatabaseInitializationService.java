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
                "account_updated DATETIME" +
                ")");

        // image metadata table
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS images_metadata (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "user_id CHAR(36) NOT NULL," +
                "s3_object_key VARCHAR(255) NOT NULL," +
                "bucket_name VARCHAR(255) NOT NULL," +
                "content_type VARCHAR(50)," +
                "file_size BIGINT," +
                "upload_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (user_id) REFERENCES users(id)" +
                ")");
    }

}
