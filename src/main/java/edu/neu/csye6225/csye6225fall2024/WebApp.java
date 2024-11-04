package edu.neu.csye6225.csye6225fall2024;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebApp {

    public static void main(String[] args) {
//         load env file
//        Dotenv dotenv = Dotenv.configure().directory("/etc/webapp").load();
//
//        System.setProperty("DB_URL", dotenv.get("DB_URL"));
//        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
//        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
//        System.setProperty("AWS_S3_BUCKET_NAME",dotenv.get("AWS_S3_BUCKET_NAME"));
//        System.setProperty("AWS_REGION",dotenv.get("AWS_REGION"));

        SpringApplication.run(WebApp.class, args);
    }

}
