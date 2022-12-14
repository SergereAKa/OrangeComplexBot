package com.ksa.telegram.orangecomplexbot.config;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@Data
@PropertySource("application.properties")
@EnableScheduling
@EnableTransactionManagement
public class BotConfig {
    @Value("${bot.name}")
    String botUserName;
    @Value("${bot.token}")
    String botToken;
    @Value("${bot.owner}")
    long ownerId;

}


