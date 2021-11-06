package org.fleamarket.bot;

import org.fleamarket.impl.IItem;
import org.fleamarket.AliExpress.AliExpressCatalogs;
import org.fleamarket.AliExpress.AliExpressContainer;
import org.fleamarket.AliExpress.AliExpressItem;
import org.fleamarket.AliExpress.AliExpressItemsParser;
import org.fleamarket.wildberries.WildberriesCatalogs;
import org.fleamarket.wildberries.WildberriesItem;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.util.Random;

public class BotTimerFunctions extends Thread {

    private WildberriesCatalogs wildberriesCatalogs;
    private String chatID;

    public void startEndlessTimer(String chatID)
    {
        this.wildberriesCatalogs = new WildberriesCatalogs();
        this.chatID = chatID;
        start();
    }

    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(1000 * 2);

                WildberriesItem wildberriesItem = wildberriesCatalogs.getRandomItem();
                sendBotPostForGroup(wildberriesItem, wildberriesItem.getCatalogName());

                System.gc();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendBotPostForGroup(IItem wildberriesItem, String name) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(new InputFile().setMedia(wildberriesItem.getImage(), "photo"));
        sendPhoto.setChatId(chatID);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\uD83D\uDC41 <b>Категория : </b>" + name + "\n" +
                "⚡️ <b>Действующая скидка : </b>" + wildberriesItem.getSale() + "%\n" +
                "\uD83D\uDCB0 <b>Цена : </b>" + wildberriesItem.getPrice() + " рублей\n" +
                "\uD83D\uDCB2 <b>Старая цена : </b> <s>" + wildberriesItem.getPriceWithoutSale() + "</s> рублей\n" +
                "\uD83D\uDC64 <b>Количество отзывов : </b>" + wildberriesItem.getNumReviews() +"\n");

        for(int i = 0; i < wildberriesItem.getStars(); i++)
        {
            stringBuilder.append("\uD83C\uDF1F");
        }

        stringBuilder.append("\n\n<a href=\"" + wildberriesItem.getUrl() + "\">Ссылка</a>");



        sendPhoto.setCaption(stringBuilder.toString());
        sendPhoto.setParseMode("html");

        try {
            new TelegramLongPoll().execute(sendPhoto);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void sendAliexpressMessage(AliExpressItem aliExpressItem, String name) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(new InputFile().setMedia(aliExpressItem.getImage(), "photo"));
        sendPhoto.setChatId(chatID);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\uD83D\uDC41 <b>Категория : </b>" + name + "\n" +
                "⚡️ <b>11 ноября будет скидка : </b>" + aliExpressItem.getSale() + "%\n" +
                "\uD83D\uDCB0 <b>Цена : </b>" + aliExpressItem.getPrice() + " рублей\n" +
                "\uD83D\uDCB2 <b>Цена до 11 ноября : </b> <s>" + aliExpressItem.getPriceWithoutSale() + "</s> рублей\n" +
                "\uD83D\uDC64 <b>Количество отзывов : </b>" + aliExpressItem.getNumReviews() +"\n");

        for(int i = 0; i < aliExpressItem.getStars(); i++)
        {
            stringBuilder.append("\uD83C\uDF1F");
        }

        stringBuilder.append("\n\n<a href=\"" + aliExpressItem.getUrl() + "\">Ссылка</a>");



        sendPhoto.setCaption(stringBuilder.toString());
        sendPhoto.setParseMode("html");

        try {
            new TelegramLongPoll().execute(sendPhoto);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
