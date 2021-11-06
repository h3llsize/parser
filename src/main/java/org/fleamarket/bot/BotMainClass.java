package org.fleamarket.bot;

import org.fleamarket.ProjectMethods;

public class BotMainClass {
    public static void main(String[] args) {
        new ProjectMethods().start();
        System.setProperty("file.encoding", "UTF-8");
        System.out.println("Я РУССКИЙ БЛЯТЬ! \\\\");
        TelegramBot telegramBot = new TelegramBot();
        telegramBot.execute();
    }
}
