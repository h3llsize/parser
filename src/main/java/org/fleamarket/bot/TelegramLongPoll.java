package org.fleamarket.bot;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramLongPoll extends TelegramLongPollingBot {
    private final String botUserName;
    private final String botToken;

    public TelegramLongPoll() {
        SetupBotSettings sb = new SetupBotSettings();
        this.botUserName = sb.getBotUserName();
        this.botToken = sb.getBotToken();
    }
    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
    }
}
