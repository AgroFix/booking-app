package my.petproject.booking.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@Data
public class TelegramBotConfig {
    @Value("${telegram_bot_name}")
    private String botName;
    @Value("${telegram_bot_token}")
    private String token;
    @Value("${telegram.admin.chatId}")
    private Long adminChatId;
}
