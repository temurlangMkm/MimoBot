package Mimo.Telegram.Entity;

import Mimo.Telegram.Service.MessageKey;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Builder
public class MessageToSend {

    private final MessageKey messageKey;
    private final String text;
    private final Long id;

}
