package Mimo.Telegram;


import Mimo.Telegram.Handlers.MainHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final MainHandler mainHandler;

    public MyTelegramBot(@Value("${bot.token}") String botToken,
                         @Value("${bot.name}") String botUsername, MainHandler mainHandler) {
        super(botToken);
        this.botUsername = botUsername;
        this.mainHandler = mainHandler;
    }


    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {

            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            String sendMessage = mainHandler.handle(messageText, chatId);

            if(!sendMessage.isBlank()){
                SendMessage(chatId, sendMessage);
            }

        }

    }

    public void SendMessage(long chatId, String messageText){
        if(!messageText.isEmpty()){
            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text(messageText)
                    .build();
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }


}
