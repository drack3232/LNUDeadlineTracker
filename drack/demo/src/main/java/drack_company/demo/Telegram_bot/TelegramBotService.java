package drack_company.demo.Telegram_bot;

import drack_company.demo.entity.Task;
import drack_company.demo.entity.tasktracker;
import drack_company.demo.services.TaskService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TelegramBotService extends TelegramLongPollingBot {
    private final Map<Long, String> userState = new ConcurrentHashMap<>();
    private final String botName;
    private final TaskService taskService;

    public TelegramBotService(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botName,
            TaskService taskService
    ){
super(botToken);
this.botName =botName;
        this.taskService = taskService;


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

            if("AWAITING_TASK_TITLE".equals(userState.get(chatId))){
                handleTaskInput(chatId, massegeText);
                return;
            }

            if(massegeText.equals("➕ Add Task")){
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

            if (massegeText.startsWith("/add")||massegeText.equals("➕ Add Task")) {
                if(massegeText.length() <= 5 || massegeText.equals("➕ Add Task")){
                    sendMessage(chatId,"Please type `/add` followed by your task name. Example:\\n/add Buy groceries");
                    return;
                }


                String taskTitle = massegeText.substring(5);

                Task newTask = new Task();
                newTask.setTitle(taskTitle);
                newTask.setStatus(tasktracker.TODO);
                newTask.setChatId(chatId);
                taskService.createTask(newTask);
                sendMessage(chatId, "Task add: " + taskTitle);

            } else if (massegeText.startsWith("/my_task")|| massegeText.equals("📋 My Tasks")) {
                List<Task> tasks = taskService.getTasksByChatId(chatId);
                //List<Task> tasks = taskService.getAllTask();
                if (tasks.isEmpty()) {
                    sendMessage(chatId, "You don`t have any task. Please add by '/add");
                } else {
                    StringBuilder response = new StringBuilder("Your tasks \n");
                    for (Task t : tasks) {
                        response.append("_ ").append(t.getTitle()).append("\n");
                    }
                    sendMessage(chatId, response.toString());
                }
            }else if (massegeText.startsWith("/delete ")){
                String idText = massegeText.substring(8).trim();
                if(massegeText.length() <= 8){
                    sendMessage(chatId,"enter id the task after command");
                    return;
                }
                try {
                    Long taskId = Long.parseLong(idText);
                    taskService.deleteTask(taskId, chatId);

                    sendMessage(chatId, "Task №" + taskId + " delete successful!");
                }catch (NumberFormatException e){
                    sendMessage(chatId, "Number the task" + idText + " is`t correct. Enter again.");
                }catch (Exception e){
                    sendMessage(chatId, "Task is`n deleted.");
                }
            }else if (massegeText.equals("⚙️ Settings")) {
                sendMessage(chatId, "Settings menu is under construction. 🛠️");
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

        try {
            execute(message);
        }catch (TelegramApiException ex){
System.err.println("Failed send message: " + ex.getMessage() );
        }
}

}
