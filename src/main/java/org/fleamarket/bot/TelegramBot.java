package org.fleamarket.bot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

class TelegramBot {

    void execute()
    {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            TelegramLongPoll telegramBot = new TelegramLongPoll();
            telegramBotsApi.registerBot(telegramBot);

            BotTimerFunctions botTimerFunctions = new BotTimerFunctions();
            botTimerFunctions.startEndlessTimer(new SetupBotSettings().getPrivateChatId());

        } catch (TelegramApiException t)
        {
            System.out.println(t);
        }
    }
}
