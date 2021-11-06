package org.fleamarket.test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        WebClient webClient = new WebClient();
        HtmlPage myPage = webClient.getPage("https://aliexpress.ru/");

// convert to jsoup dom
        Document doc = Jsoup.parse(myPage.asXml());

// extract data using jsoup selectors
        Elements images = doc.select("img[src~=(?i)\\.(png|jpe?g|gif)]");
        for (Element image : images) {
            System.out.println("src : " + image.attr("src"));
        }

// clean up resources
        webClient.close();
    }
}
