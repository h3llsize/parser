package org.fleamarket.wildberries;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

public class ParserMethods {
    private static final int RECONNECT_TIME = 0;

    public static Document getPage(String url) throws InterruptedException {
        Thread.sleep(RECONNECT_TIME);
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(url),5000);
        } catch (IOException exception) {
            Thread.sleep(3000);
        }
        return doc;
    }
}
