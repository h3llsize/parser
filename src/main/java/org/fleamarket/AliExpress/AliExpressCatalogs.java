package org.fleamarket.AliExpress;

import org.apache.commons.io.FileUtils;
import org.fleamarket.bot.SetupBotSettings;
import org.fleamarket.impl.ICatalogs;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class AliExpressCatalogs implements ICatalogs {
    private final List<AliExpressContainer> AliExpressContainerList = new ArrayList<>();

    public AliExpressCatalogs() {
        SetupBotSettings setupBotSettings = new SetupBotSettings();
        String allCatalogs = readUsingApacheCommonsIO(setupBotSettings.getUrlToAliExpressCatalog());

        if(allCatalogs.equals("")) {
            System.err.println("Napiwi normalno catalogi dolboeb!");
            System.exit(1);
        }

        String[] ctg = allCatalogs.split("&&&");


        for(int i = 0; i < ctg.length; i++)
        {
            AliExpressContainer AliExpressContainer = new AliExpressContainer(ctg[i], setupBotSettings);
            AliExpressContainerList.add(AliExpressContainer);
        }
        try {
            parseAllContainers();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void parseAllContainers() throws InterruptedException {
        for(int i = 0; i < AliExpressContainerList.size(); i++)
        {
            Thread.sleep(500);
            AliExpressContainerList.get(i).startParseItems();
        }
    }

    public List<AliExpressContainer> getWbContainerList() {
        return AliExpressContainerList;
    }

    public AliExpressContainer getRandomWbContainer() {

        return AliExpressContainerList.get(new Random().nextInt(AliExpressContainerList.size()));
    }

    public AliExpressItem getRandomItem() {
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

        ArrayList<File> AliExpressItems = new ArrayList<>();
        AliExpressItems.add(firstItem);
        AliExpressItems.add(secondItem);
        AliExpressItems.add(thirdItem);

        return findBestItem(AliExpressItems);
    }

    private AliExpressItem findBestItem(ArrayList<File> files) {
        ArrayList<String> betterCatalogs = new ArrayList<>();
        betterCatalogs.add("Свитшоты");
        betterCatalogs.add("Кеды и кроссовки");
        betterCatalogs.add("Худи");
        betterCatalogs.add("Брюки");
        betterCatalogs.add("Толстовки");
        betterCatalogs.add("Футболки и топы");
        betterCatalogs.add("Рюкзаки");
        betterCatalogs.add("Джинсы");

        ArrayList<AliExpressItem> items = new ArrayList<>();
        files.forEach(f -> {
            try {
                InputStream inputStream = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(inputStream);
                AliExpressItem AliExpressItem = (AliExpressItem) ois.readObject();
                items.add(AliExpressItem);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        HashMap<AliExpressItem, Integer> points = new HashMap<>();
        items.forEach(i -> {
            points.put(i,0);
        });

        AliExpressItem bestSaleItem = items.get(0);
        AliExpressItem bestStarsItem = items.get(0);
        AliExpressItem bestReviews = items.get(0);


        for (AliExpressItem AliExpressItem : items) {
            if (bestSaleItem.getSale() < AliExpressItem.getSale()) {
                bestSaleItem = AliExpressItem;
            }

            if (bestStarsItem.getStars() < AliExpressItem.getStars()) {
                bestStarsItem = AliExpressItem;
            }

            if (bestReviews.getNumReviews() < AliExpressItem.getNumReviews()) {
                bestReviews = AliExpressItem;

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


        AliExpressItem bestItem = items.get(0);
        for (AliExpressItem k : points.keySet()) {
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
