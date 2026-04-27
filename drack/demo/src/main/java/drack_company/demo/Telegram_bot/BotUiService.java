package drack_company.demo.Telegram_bot;
import java.util.List;
import drack_company.demo.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

@Service
public class BotUiService {
    public InlineKeyboardMarkup generateTaskButtons(Page<Task> taskPage, int curentPage){

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        Task curentTask = taskPage.getContent().get(0);
       List <InlineKeyboardButton> actionRow = new ArrayList<>();
       List<InlineKeyboardButton> navRow = new ArrayList<>();

       if(taskPage.hasPrevious()){
           InlineKeyboardButton backButton = new InlineKeyboardButton();
           backButton.setText("↩ Back");
           backButton.setCallbackData("/page_todo" + (curentPage - 1));
           navRow.add(backButton);
       }
       if(taskPage.hasNext()){
           InlineKeyboardButton nextButton = new InlineKeyboardButton();
           nextButton.setText(" ╰┈➤ˎˊ˗Next");
           nextButton.setCallbackData("/page_todo" + (curentPage + 1));
           navRow.add(nextButton);
       }
        InlineKeyboardButton deleteButton = new InlineKeyboardButton();
        deleteButton.setText("❌ Delete");
        deleteButton.setCallbackData("/delete " + curentTask.getId());

        InlineKeyboardButton doneButton = new InlineKeyboardButton();
        doneButton.setText("✅ Done");
        doneButton.setCallbackData("/done " + curentTask.getId());

        actionRow.add(deleteButton);
        actionRow.add(doneButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(actionRow);

        if(!navRow.isEmpty()){
            rowList.add(navRow);
        }

        keyboardMarkup.setKeyboard(rowList);

        return keyboardMarkup;
    }

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
