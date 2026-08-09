package Mimo.telegram;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    private final String botUsername;

    public MyTelegramBot(@Value("${bot.token}") String botToken,
                             @Value("${bot.name}") String botUsername) {
        super(botToken);
        this.botUsername = botUsername;
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

            // Echo the received text back to the user;
            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text("You said: " + messageText)
                    .build();

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
