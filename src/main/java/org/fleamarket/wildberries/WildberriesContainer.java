package org.fleamarket.wildberries;

import org.fleamarket.bot.SetupBotSettings;
import org.fleamarket.impl.IContainer;

import java.io.*;
import java.util.Random;

public class WildberriesContainer implements IContainer {


    private final String containerCatalogName;
    private final int minPrice;
    private final int minSale;
    private final int maxPrice;
    public final SetupBotSettings setupBotSettings;
    private final String containerUrl;

    public WildberriesContainer(String containerCatalogName, SetupBotSettings setupBotSettings) {

        String[] params = containerCatalogName.split(" # ");
        this.containerCatalogName = params[2];
        this.minPrice = Integer.parseInt(params[1].split(" ")[0]);
        this.minSale = Integer.parseInt(params[1].split(" ")[1]);
        this.maxPrice = Integer.parseInt(params[1].split(" ")[2]);
        this.setupBotSettings = setupBotSettings;
        this.containerUrl = params[0];
    }

    public void startParseItems()
    {
        WildberriesItemsParser wildberriesItemsParser = new WildberriesItemsParser(containerCatalogName,containerUrl);
        wildberriesItemsParser.parse(this, minPrice, minSale, maxPrice);
    }

    public WildberriesItem getRandomItem() throws IOException, ClassNotFoundException {
        File dir = new File(setupBotSettings.getUrlToCatalogsDirWieldberries() + containerCatalogName);

        File[] files = dir.listFiles();

        if(files == null) return null;

        Random random = new Random();

        int randomNum = 0;

        try {
            randomNum = random.nextInt(files.length - 1);
        } catch (Exception e)
        {
            return null;
        }
        File file = files[randomNum];

        InputStream inputStream = new FileInputStream(file);

        ObjectInputStream ois = new ObjectInputStream(inputStream);

        WildberriesItem wildberriesItem = (WildberriesItem) ois.readObject();

        file.deleteOnExit();

        return wildberriesItem;

    }

    public File getRandomFile() {
        File dir = new File(setupBotSettings.getUrlToCatalogsDirWieldberries() + containerCatalogName);

        File[] files = dir.listFiles();

        if(files == null) return null;

        Random random = new Random();

        int randomNum = 0;

        try {
            randomNum = random.nextInt(files.length - 1);
        } catch (Exception e)
        {
            return null;
        }

        return files[randomNum];
    }

    public String getContainerCatalogName() {
        return containerCatalogName;
    }
}
