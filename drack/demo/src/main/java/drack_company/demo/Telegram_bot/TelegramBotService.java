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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

@Service
public class TelegramBotService extends TelegramLongPollingBot {
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

            if (massegeText.equals("/start")) {
                startCommandReceived(chatId, userFirstName);
            }

            if (massegeText.startsWith("/add")) {
                if(massegeText.length() <= 5){
                    sendMessage(chatId,"You forgot to name the task");
                    return;
                }


                String taskTitle = massegeText.substring(5);

                Task newTask = new Task();
                newTask.setTitle(taskTitle);
                newTask.setStatus(tasktracker.TODO);
                newTask.setChatId(chatId);
                taskService.createTask(newTask);
                sendMessage(chatId, "Task add: " + taskTitle);

            } else if (massegeText.startsWith("/my_task")) {
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
            }
            if (massegeText.startsWith("/delete ")){
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
