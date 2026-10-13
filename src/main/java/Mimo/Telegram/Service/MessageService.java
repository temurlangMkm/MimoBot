package Mimo.Telegram.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageSource messageSource;

    // Метод для обычных сообщений
    public String getMessage(MessageKey key, Language language) {
        return getMessage(key, language, (Object[]) null);
    }

    // Перегруженный метод для сообщений с параметрами (например: "Привет, {0}!")
    public String getMessage(MessageKey key, Language language, Object... args) {
        Locale locale = getLocale(language);
        // Spring ищет ключ по названию enum'а в нижнем регистре
        return messageSource.getMessage(key.name().toLowerCase(), args, locale);
    }

    private Locale getLocale(Language language) {
        return switch (language) {
            case RU -> new Locale("ru");
            case EN -> new Locale("en");
            case UZ -> new Locale("uz");
            default -> new Locale("uz");
        };
    }
}