package org.fleamarket.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public interface IItem {
    String getUrl();

    int getPrice();

    int getSale();

    String getName();

    InputStream getImage();

    int getPriceWithoutSale();

    void createItem(String catalogName, String urlToCatalogs);

    String getCatalogName();

    int getStars();

    int getNumReviews();

    String getFileName();
}
