package Mimo.Telegram.Entity;

import Mimo.Telegram.Service.MessageKey;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class MessageToSend {


    public MessageKey messageKey;
    public String text;
    public Long id;



    public MessageToSend(MessageKey messageKey,String text, Long id) {
        this.text = text;
        this.messageKey = messageKey;
        this.id = id;
    }
    public MessageToSend() {}
}
