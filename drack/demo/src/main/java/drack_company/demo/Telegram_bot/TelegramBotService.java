package drack_company.demo.Telegram_bot;

import drack_company.demo.entity.Task;
import drack_company.demo.entity.tasktracker;
import drack_company.demo.services.TaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TelegramBotService extends TelegramLongPollingBot {
    private final Map<Long, String> userState = new ConcurrentHashMap<>();
    private final String botName;
    private final TaskService taskService;
    private final BotUiService botUiService;

    public TelegramBotService(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botName,
            TaskService taskService, BotUiService botUiService
    ){
super(botToken);
this.botName =botName;
        this.taskService = taskService;
        this.botUiService = botUiService;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String massegeText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userFirstName = update.getMessage().getChat().getFirstName();

            if ("AWAITING_TASK_TITLE".equals(userState.get(chatId))) {
                handleTaskInput(chatId, massegeText);
                return;

            }

            if (massegeText.equals("➕ Add Task")) {
                sendMessage(chatId, "Okay, send me the title of your new task: ");
                userState.put(chatId, "AWAITING_TASK_TITLE");
                return;
            }

            if (massegeText.equals("/start")) {
                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(chatId));
                message.setText("Welcome to your Task Tracker! 🚀\nChoose an action below:");

                message.setReplyMarkup(getMainMenuKeyboard());

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                return;
            }

            if (massegeText.startsWith("/add") || massegeText.equals("➕ Add Task")) {
                if (massegeText.length() <= 5 || massegeText.equals("➕ Add Task")) {
                    sendMessage(chatId, "Please type `/add` followed by your task name. Example:\\n/add Buy groceries");
                    return;
                }


                String taskTitle = massegeText.substring(5);

                Task newTask = new Task();
                newTask.setTitle(taskTitle);
                newTask.setStatus(tasktracker.TODO);
                newTask.setChatId(chatId);
                taskService.createTask(newTask);
                sendMessage(chatId, "Task add: " + taskTitle);

            } else if (massegeText.startsWith("/my_task") || massegeText.equals("📋 My Tasks")) {
                Page<Task> taskPage = taskService.getTaskPage(chatId,tasktracker.TODO,0);

                if (!taskPage.hasContent()) {
                    sendMessage(chatId, "You don`t have any active task. Please add by '/add'");
                } else{
                    Task t = taskPage.getContent().get(0);
                    SendMessage message = new SendMessage();
                    message.setChatId(String.valueOf(chatId));
                    message.setParseMode("HTML");

                    String cardText = "📌 <b>" + t.getTitle() + "</b>\n" +
                            "⏳ Status: <i>" + t.getStatus() + "</i>\n" +
                            "🆔 ID: <code>" + t.getId() + "</code>";
                    message.setText(cardText);

                    InlineKeyboardMarkup keyboardMarkup = botUiService.generateTaskButtons(taskPage,0);
                    message.setReplyMarkup(keyboardMarkup);

                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            System.err.println("Error by send message " + e.getMessage());
                        }
                    }


            } else if (massegeText.startsWith("/delete")) {
                String idText = massegeText.substring(8).trim();
                if (massegeText.length() <= 8) {
                    sendMessage(chatId, "enter id the task after command");
                    return;
                }
                try {
                    Long taskId = Long.parseLong(idText);
                    taskService.deleteTask(taskId, chatId);

                    sendMessage(chatId, "Task №" + taskId + " delete successful!");
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Number the task" + idText + " is`t correct. Enter again.");
                } catch (Exception e) {
                    sendMessage(chatId, "Task is`n deleted.");
                }
            } else if (massegeText.equals("⚙️ Settings")) {
                sendMessage(chatId, "Settings menu is under construction. ");
            }


        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();

            long chatId = update.getCallbackQuery().getMessage().getChatId();

            Integer messageID = update.getCallbackQuery().getMessage().getMessageId();

            if (callbackData.startsWith("/delete")) {
                String idText = callbackData.substring(8);
                try {
                    Long taskId = Long.parseLong(idText);
                    taskService.deleteTask(taskId, chatId);

                    org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery answer =
                            new org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery();
                    answer.setCallbackQueryId(update.getCallbackQuery().getId());
                    answer.setText("Task delete");
                    answer.setShowAlert(true);
                    execute(answer);

                    org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage deleteMessage =
                            new org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage();
                    deleteMessage.setChatId(String.valueOf(chatId));
                    deleteMessage.setMessageId(messageID);
                    execute(deleteMessage);

                } catch (NumberFormatException e) {
                    System.out.println("Error parsing id: " + idText);
                } catch (TelegramApiException e) {
                    System.out.println("Error deleting a task: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Error. Trouble with BD: " + e.getMessage());
                }

            } else if (callbackData.startsWith("/done" )) {
                String idText = callbackData.substring(5);
                try {
                    Long taskId = Long.parseLong(idText.trim());
                     taskService.markAsDone(taskId);

                    String updatedText = "<b>✅ Completed:</b> <s>Task #\" + taskId + \"</s>\\n\uD83D\uDD52 Deadline: <i> null </i>";
                    botUiService.editTaskMessage(this, chatId, messageID, updatedText, null);
                } catch (Exception e) {
                    System.err.println("Error with (Done): " + e.getMessage());
                }
            }else if(callbackData.startsWith("/page_todo")){
                String idText = callbackData.substring(10).trim();
                int pageNumber = Integer.parseInt(idText);

                Page<Task> page = taskService.getTaskPage(chatId,tasktracker.TODO,pageNumber);
                Task t = page.getContent().get(0);

                String cardText = "📌 <b>" + t.getTitle() + "</b>\n" +
                        "⏳ Status: <i>" + t.getStatus() + "</i>\n" +
                        "🆔 ID: <code>" + t.getId() + "</code>";

                botUiService.generateTaskButtons(page,pageNumber);

                EditMessageText editMessageText = new EditMessageText();
               editMessageText.setChatId(String.valueOf(chatId));
               editMessageText.setMessageId(messageID);
               editMessageText.setText(cardText);
               editMessageText.setParseMode("HTML");
               InlineKeyboardMarkup button = botUiService.generateTaskButtons(page,pageNumber);
               editMessageText.setReplyMarkup(button);

               try {
                   execute(editMessageText);
               }catch (TelegramApiException e){
                   e.printStackTrace();
               }
            }
        }
    }

    private ReplyKeyboardMarkup getMainMenuKeyboard(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("📋 My Tasks"));
        row1.add(new KeyboardButton("➕ Add Task"));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("⚙️ Settings"));

        keyboardRows.add(row1);
        keyboardRows.add(row2);

        keyboardMarkup.setKeyboard(keyboardRows);
return keyboardMarkup;
    }

    private void handleTaskInput(long chatId, String taskTitle){
        Task newTask =new Task();
        newTask.setTitle(taskTitle);
        newTask.setChatId(chatId);
        newTask.setStatus(tasktracker.TODO);

        taskService.createTask(newTask);

        userState.remove(chatId);

        sendMessage(chatId,"Task saved: " + taskTitle);
    }

    public void  startCommandReceived(long chatId, String name){
        String answer = "Hello " + name;
        sendMessage(chatId, answer);
    }
private void sendMessage(long chartId, String textToSend){
        SendMessage message =  new SendMessage();
        message.setChatId(String.valueOf(chartId));
        message.setText(textToSend);

        message.setParseMode("HTML");

        try {
            execute(message);
        }catch (TelegramApiException ex){

System.err.println("Failed send message: " + ex.getMessage() );

        }
}

}
