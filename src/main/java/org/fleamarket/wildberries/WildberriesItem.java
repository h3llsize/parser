package org.fleamarket.wildberries;

import com.google.common.io.ByteStreams;
import org.fleamarket.impl.IItem;

import java.io.*;

public class WildberriesItem implements IItem, Serializable {
    private final String url;
    private final int price;
    private final int sale;
    private final int priceWithoutSale;
    private final String name;
    private final int stars;
    private final byte[] imageByteArray;
    private final String catalogName;
    private final int numReviews;
    private String fileName;

    public WildberriesItem(String url, int price, int sale, int priceWithoutSale, String name, InputStream image, String catalogName, int stars, int numReviews) throws IOException {
        this.url = url;
        this.price = price;
        this.sale = sale;
        this.priceWithoutSale = priceWithoutSale;
        this.name = name.replaceAll("/","").replaceAll("\\|","").replaceAll("\"","");
        this.imageByteArray = ByteStreams.toByteArray(image);
        this.catalogName = catalogName;
        this.stars = stars;
        this.numReviews = numReviews;
        this.fileName = "not";

        System.out.println(catalogName + " | " + price + " RUB | " + sale +  "% | " + name + " | " + url + " | " + stars + " | " + numReviews);
    }

    public String getUrl() {
        return url;
    }

    public int getPrice() {
        return price;
    }

    public int getSale() {
        return sale;
    }

    public String getName() {
        return name;
    }

    public InputStream getImage() {
        return new ByteArrayInputStream(this.imageByteArray);
    }

    public int getPriceWithoutSale() {
        return priceWithoutSale;
    }

    public void createItem(String catalogName, String urlToCatalogs) {

        int limit = 15;
        StringBuilder subStr = new StringBuilder(name.length() > limit ? name.substring(0, limit) : name);

        new File(urlToCatalogs + catalogName + "/").mkdirs();

        StringBuilder dir = new StringBuilder(urlToCatalogs + catalogName + "/" + subStr);

        try {
            File file = new File(dir + ".item");
            this.fileName = subStr.toString();

            FileOutputStream fout = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fout);

            oos.writeObject(this);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getCatalogName() {
        return catalogName;
    }

    public int getStars() {
        return stars;
    }

    public int getNumReviews() {
        return numReviews;
    }

    public String getFileName() {
        return fileName;
    }
}
