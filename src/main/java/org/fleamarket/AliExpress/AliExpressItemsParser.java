package org.fleamarket.AliExpress;

import org.fleamarket.wildberries.WildberriesContainer;
import org.fleamarket.wildberries.WildberriesItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import static org.fleamarket.wildberries.ParserMethods.getPage;

public class AliExpressItemsParser extends Thread {
    private final String catalogUrlName;
    private final String catalogFileName;
    private AliExpressContainer AliExpressContainer;
    private int minPrice;
    private int minSale;
    private int maxPrice;

    public AliExpressItemsParser(String catalogName, String catalogUrlName) {
        this.catalogUrlName = catalogUrlName;
        this.catalogFileName = catalogName;
    }

    public void parse(AliExpressContainer AliExpressContainer, int minPrice, int minSale, int maxPrice)
    {
        this.AliExpressContainer = AliExpressContainer;
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
                System.out.println(pageUrl);
                Document itemsPage = getPage(pageUrl);
                Elements elements = null;
                try {
                    elements = itemsPage.select("ul > li");
                } catch (Exception e) {
                    continue;
                }
                for (Element element : elements) {
                    try {

                        String url = element.select("a.SearchProductFeed_Link__link__sf54s").attr("href");

                        String[] sp = element.select("span.SearchProductFeed_Price__titleWrapper__1jg3h").text().split("\\.");

                        if(sp.length == 1) continue;

                        int price = (int) (Integer.parseInt(sp[0].split("-")[0].replaceAll("[^0-9]","")) / 100f);

                        if(price > maxPrice || price < minPrice) {
                            continue;
                        }

                        int priceWithoutSale = (int) (Integer.parseInt(sp[1].replaceAll("[^0-9]","")) / 100f);

                        int sale = (int) (((double) (priceWithoutSale - price) / priceWithoutSale) * 100f);

                        if(sale < minSale || price < 0) {
                            continue;
                        }

                        String name = element.select("a[target=_blank]").text().replaceAll("[^a-zA-Zа-яёА-ЯЁ ]","");

                        String starsS = element.select("a[class=SearchProductFeed_Link__link__sf54s SearchProductFeed_Link__grey__sf54s SearchProductFeed_Link__size12__sf54s]").first().text()
                                .replaceAll("[^0-9]","");

                        int stars = (int) (Double.parseDouble(starsS) / 10f);

                        String reviewsS = element.select("a[class=SearchProductFeed_Link__link__sf54s SearchProductFeed_Link__grey__sf54s SearchProductFeed_Link__size12__sf54s]").text().replaceAll("[^0-9]","");

                        String imageUrl = "https:" + element.select("img[class=SearchProductFeed_Preview__img__3zxie]").attr("src");


                        BufferedImage img = ImageIO.read(new URL(imageUrl));
                        InputStream image = bufferedImageToInputStream(img);

                        new AliExpressItem(url, price, sale, priceWithoutSale, name, image, catalogFileName, stars, Integer.parseInt(reviewsS))
                                .createItem(catalogFileName, AliExpressContainer.setupBotSettings.getUrlToCatalogsDirAliExpress());

                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
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
