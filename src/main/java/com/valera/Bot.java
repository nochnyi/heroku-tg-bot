package com.valera;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        Model model = new Model();
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/info":
                    sendMsg(message, "На данный момент у меня ограниченная возможность поиска погоды по городам!" + "\n" +
                            "Но наши инженеры, вернее один инженер, обещает расширить мои возможности и сделать меня крутым ботом!"+ "\n" +
                            "Остаёмся на связи. Приятного пользования!"
                            );
                    break;
                case "/contacts":
                    sendMsg(message, "С нами Вы можете связаться по ел.адрессу: metcastbot@gmail.com");
                    break;
                default:
                    try {
                        sendMsg(message, Weather.getWeather(message.getText(), model));
                    } catch (IOException e) {
                        sendMsg(message, "По заданному запросу - город не найденно!:(");
                    }
            }

        }
    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        sendMessage.enableHtml(true);
        try {
            setButtons(sendMessage);
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void setButtons(SendMessage sendMessage) {
        //Create a keyBoard:
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        //Setup markup for keyboard:
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        //Create a list of buttons:
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/info"));
        keyboardFirstRow.add(new KeyboardButton("/contacts"));

//        keyboardSecondRow.add(new KeyboardButton("/contacts"));
//        keyboardSecondRow.add(new KeyboardButton("/callbacks"));

        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    public String getBotUsername() {
        return Enums.BOT_NAME;
    }

    public String getBotToken() {
        return Enums.COFFEE_LAND_TOKEN;
    }
}
