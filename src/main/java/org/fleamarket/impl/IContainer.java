package org.fleamarket.impl;

import org.fleamarket.wildberries.WildberriesItem;

import java.io.File;
import java.io.IOException;

public interface IContainer {
    void startParseItems();
    IItem getRandomItem() throws IOException, ClassNotFoundException;

    File getRandomFile() throws IOException;

    String getContainerCatalogName();
}
