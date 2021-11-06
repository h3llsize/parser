package org.fleamarket.wildberries;

import org.apache.commons.io.FileUtils;
import org.fleamarket.bot.SetupBotSettings;
import org.fleamarket.impl.ICatalogs;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class WildberriesCatalogs implements ICatalogs {
    private final List<WildberriesContainer> wbContainerList = new ArrayList<>();

    public WildberriesCatalogs() {
        SetupBotSettings setupBotSettings = new SetupBotSettings();
        String allCatalogs = readUsingApacheCommonsIO(setupBotSettings.getUrlToCatalogsFile());

        if(allCatalogs.equals("")) {
            System.err.println("Napiwi normalno catalogi dolboeb!");
            System.exit(1);
        }

        String[] ctg = allCatalogs.split("&&&");


        for(int i = 0; i < ctg.length; i++)
        {
            WildberriesContainer wildberriesContainer = new WildberriesContainer(ctg[i], setupBotSettings);
            wbContainerList.add(wildberriesContainer);
        }
        try {
            parseAllContainers();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void parseAllContainers() throws InterruptedException {
        for(int i = 0; i < wbContainerList.size(); i++)
        {
            Thread.sleep(500);
            wbContainerList.get(i).startParseItems();
        }
    }

    public List<WildberriesContainer> getWbContainerList() {
        return wbContainerList;
    }

    public WildberriesContainer getRandomWbContainer() {

        return wbContainerList.get(new Random().nextInt(wbContainerList.size()));
    }

    public WildberriesItem getRandomItem() {
        File firstItem = null;
        while (firstItem == null) {
            firstItem = getRandomWbContainer().getRandomFile();
        }

        File secondItem = null;
        while (secondItem == null) {
            secondItem = getRandomWbContainer().getRandomFile();
        }

        File thirdItem = null;
        while (thirdItem == null) {
            thirdItem = getRandomWbContainer().getRandomFile();
        }

        ArrayList<File> wildberriesItems = new ArrayList<>();
        wildberriesItems.add(firstItem);
        wildberriesItems.add(secondItem);
        wildberriesItems.add(thirdItem);

        return findBestItem(wildberriesItems);
    }

    private WildberriesItem findBestItem(ArrayList<File> files) {
        ArrayList<String> betterCatalogs = new ArrayList<>();
        betterCatalogs.add("Свитшоты");
        betterCatalogs.add("Кеды и кроссовки");
        betterCatalogs.add("Худи");
        betterCatalogs.add("Брюки");
        betterCatalogs.add("Толстовки");
        betterCatalogs.add("Футболки и топы");
        betterCatalogs.add("Рюкзаки");
        betterCatalogs.add("Джинсы");

        ArrayList<WildberriesItem> items = new ArrayList<>();
        files.forEach(f -> {
            try {
                InputStream inputStream = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(inputStream);
                WildberriesItem wildberriesItem = (WildberriesItem) ois.readObject();
                items.add(wildberriesItem);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        HashMap<WildberriesItem, Integer> points = new HashMap<>();
        items.forEach(i -> {
            points.put(i,0);
        });

        WildberriesItem bestSaleItem = items.get(0);
        WildberriesItem bestStarsItem = items.get(0);
        WildberriesItem bestReviews = items.get(0);


        for (WildberriesItem wildberriesItem : items) {
            if (bestSaleItem.getSale() < wildberriesItem.getSale()) {
                bestSaleItem = wildberriesItem;
            }

            if (bestStarsItem.getStars() < wildberriesItem.getStars()) {
                bestStarsItem = wildberriesItem;
            }

            if (bestReviews.getNumReviews() < wildberriesItem.getNumReviews()) {
                bestReviews = wildberriesItem;

            }
        }



        points.put(bestSaleItem, points.get(bestSaleItem) + 1);
        points.put(bestStarsItem, points.get(bestStarsItem) + 1);
        points.put(bestReviews, points.get(bestReviews) + 1);

        points.keySet().forEach(k ->
        {
            if(betterCatalogs.contains(k.getCatalogName()))
            {
                points.put(k, points.get(k) + 1);
            }
        });


        WildberriesItem bestItem = items.get(0);
        for (WildberriesItem k : points.keySet()) {
            if (points.get(k) > points.get(bestItem))
                bestItem = k;
        }

        for (int i = 0; i < items.size(); i++)
        {
            if(items.get(i).equals(bestItem)) {
                System.out.println(files.get(i).getAbsolutePath());
                files.get(i).deleteOnExit();
            }
        }

        return bestItem;
    }

    private static String readUsingApacheCommonsIO(String fileName){
        try {
            return FileUtils.readFileToString(new File(fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
