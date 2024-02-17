package kz.azamatbakyt.BarberTelegramBot.service.bot.model;

import lombok.Builder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public class Message {

    private final MessageType messageType;
    private final SendMessage sendMessage;
    private final EditMessageText editMessageText;
    private final SendPhoto sendPhoto;

    public Message(SendMessage sendMessage) {
        this.messageType = MessageType.MESSAGE;
        this.sendMessage = sendMessage;
        this.sendPhoto = null;
        this.editMessageText = null;
    }

    public Message(SendPhoto sendPhoto) {
        this.messageType = MessageType.PHOTO;
        this.sendPhoto = sendPhoto;
        this.sendMessage = null;
        this.editMessageText = null;
    }

    public Message(EditMessageText editMessageText) {
        this.messageType = MessageType.EDIT_MESSAGE_TEXT;
        this.editMessageText = editMessageText;
        this.sendMessage = null;
        this.sendPhoto = null;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public SendMessage getSendMessage() {
        return sendMessage;
    }

    public SendPhoto getSendPhoto() {
        return sendPhoto;
    }

    public EditMessageText getEditMessageText() {
        return editMessageText;
    }
}
