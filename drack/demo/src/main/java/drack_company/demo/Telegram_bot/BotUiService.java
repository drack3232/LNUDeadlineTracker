package drack_company.demo.Telegram_bot;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class BotUiService {
public void editTaskMessage(AbsSender sender, long chatId, int messageId, String newText, InlineKeyboardMarkup newButtons){
    EditMessageText editMessage = new EditMessageText();
editMessage.setChatId(String.valueOf(chatId));
editMessage.setMessageId(messageId);
editMessage.setText(newText);
editMessage.setParseMode("HTML");
editMessage.setReplyMarkup(newButtons);

try {
    sender.execute(editMessage);

}catch (TelegramApiException e){
    System.err.println("Error in edit message: " + e.getMessage());
}

    }
}
