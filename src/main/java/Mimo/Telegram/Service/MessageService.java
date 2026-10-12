package Mimo.Telegram.Service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageService {

    private final Map<MessageKey, String> toRussian = new HashMap<>();
    private final Map<MessageKey, String> toUzbek = new HashMap<>();

    public MessageService() {
        // Russian
        toRussian.put(MessageKey.WELCOME,
                "Привет! Вы раньше не регистрировались в нашем сервисе. " +
                        "Пожалуйста, выберите группу для начала.");
        toRussian.put(MessageKey.CHOOSE_GROUP,
                "Отправьте группу.");

        // Uzbek
        toUzbek.put(MessageKey.WELCOME,
                "Salom! Siz avval xizmatimizda ro'yxatdan o'tmagansiz. " +
                        "Boshlash uchun guruhingizni tanlang.");
        toUzbek.put(MessageKey.CHOOSE_GROUP,
                "Guruhingizni yuboring.");
    }

    public String getMessage(MessageKey key, Enum language) {
        Map<MessageKey, String> messages = switch (language) {
            case Language.RU -> toRussian;
            case Language.UZ -> toUzbek;
            default -> toUzbek;
        };

        return messages.getOrDefault(key, key.name());
    }
}
