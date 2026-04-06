package drack_company.demo.config;

import drack_company.demo.Telegram_bot.TelegramBotService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class botConfig {
private final TelegramBotService telegramBotService;

    public botConfig(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    @Bean
    public TelegramBotsApi telegramBotApi(TelegramBotService telegramBotService) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(telegramBotService);
        return api;
    }

}
