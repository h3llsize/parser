package org.fleamarket.wildberries;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

import static org.fleamarket.wildberries.ParserMethods.getPage;

public class WildberriesItemsParser extends Thread {
    private final String catalogUrlName;
    private final String catalogFileName;
    private WildberriesContainer wildberriesContainer;
    private int minPrice;
    private int minSale;
    private int maxPrice;

    public WildberriesItemsParser(String catalogName, String catalogUrlName) {
        this.catalogUrlName = catalogUrlName;
        this.catalogFileName = catalogName;
    }

    public void parse(WildberriesContainer wildberriesContainer, int minPrice, int minSale, int maxPrice)
    {
        this.wildberriesContainer = wildberriesContainer;
        this.minPrice = minPrice;
        this.minSale = minSale;
        this.maxPrice = maxPrice;
        start();
    }

    @Override
    public void run() {
        int page = 0;


        while (true) {
            try {
                page++;
                String pageUrl = catalogUrlName + "&page=" + page;
                Document itemsPage = getPage(pageUrl);
                Elements elements = null;

                try {
                     elements = itemsPage.select("div[class=product-card j-card-item]");
                } catch (Exception e) {
                    continue;
                }
                for (Element element : elements) {
                    try {
                        String url = "https://www.wildberries.ru" + element.select("a[class=product-card__main j-open-full-product-card]")
                                .attr("href");
                        String sPrice = element.select("span[class=price-commission__current-price]")
                                .text().replace("₽", "").replaceAll(" ", "").replaceAll("от","");

                        if(sPrice.equals("")) continue;

                        int price = Integer.parseInt(sPrice);

                        if (!(price > minPrice && price < maxPrice)) continue;

                        int sale = Integer.parseInt(element.select("span[class=product-card__sale]")
                                .text().replace("%", "").replace("-", ""));

                        if (sale < minSale) continue;

                        int priceWithoutSale = Integer.parseInt(element.select("del[class=price-commission__old-price]").
                                text().replace("₽", "").replaceAll(" ", ""));
                        Document itemPage = getPage(url);

                        if (itemPage == null) continue;

                        if (itemPage.select("div[class=sold-out-product__text ]").text().equals("Товара нет в наличии")) {
                            continue;
                        }
                        String name = itemPage.select("h1[class=same-part-kt__header]").text();

                        String imageUrl = "https:" + itemPage.select("img[class=photo-zoom__preview j-zoom-preview]").attr("src");


                        BufferedImage img = ImageIO.read(new URL(imageUrl));
                        InputStream image = bufferedImageToInputStream(img);

                        String stars = itemPage.select("div[class=same-part-kt__common-info]").select("span[data-link=text{: product^star}]").first().text();

                        String rewies = itemPage.select("span[class=same-part-kt__count-review]").first().text().replaceAll("[^0-9]","");

                        new WildberriesItem(url, price, sale, priceWithoutSale, name, image, catalogFileName, Integer.parseInt(stars), Integer.parseInt(rewies))
                                .createItem(catalogFileName, wildberriesContainer.setupBotSettings.getUrlToCatalogsDirWieldberries());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.gc();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private InputStream bufferedImageToInputStream(BufferedImage img) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(img, "jpeg", os);                          // Passing: ​(RenderedImage im, String formatName, OutputStream output)
        InputStream is = new ByteArrayInputStream(os.toByteArray());

        return is;
    }
}
