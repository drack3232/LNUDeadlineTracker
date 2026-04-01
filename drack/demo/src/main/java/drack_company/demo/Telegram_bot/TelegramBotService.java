package drack_company.demo.Telegram_bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TelegramBotService extends TelegramLongPollingBot {
    private final String botName;

    public TelegramBotService(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botName
    ){
super(botToken);
this.botName =botName;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
    @Override
    public void onUpdateReceived(Update update) {
if(update.hasMessage()&& update.getMessage().hasText()){
    String massegeText = update.getMessage().getText();


    long chatId = update.getMessage().getChatId();
    String userFirstName = update.getMessage().getChat().getFirstName();

    if (massegeText.equals("/start")){
        startCommandReceived(chatId, userFirstName);

    }else {
        sendMessage(chatId, "Sorry, I`m undestand only this command '/start' ");
    }
}

    }
    public void  startCommandReceived(long chatId, String name){
        String answer = "Hello " + name;
        sendMessage(chatId, answer);
    }
private void sendMessage(long chartId, String textToSend){
        SendMessage message =  new SendMessage();
        message.setChatId(String.valueOf(chartId));
        message.setText(textToSend);

        try {
            execute(message);
        }catch (TelegramApiException ex){
System.err.println("Failed send message: " + ex.getMessage() );
        }
}

}
