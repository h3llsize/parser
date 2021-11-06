package org.fleamarket.bot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SetupBotSettings {
    private String botUserName;
    private String botToken;
    private String privateChatId;
    private String urlToCatalogsFile;
    private String urlToCatalogsDirWieldberries;
    private String urlToCatalogsDirAliExpress;
    private String urlToAliExpressCatalog;

    public SetupBotSettings()
    {
        getSettings();
    }

    private void getSettings()
    {
        try {
        Properties prop = new Properties();
        prop.load(new FileInputStream("src/main/resources/propUrls.properties"));
        String urlToConfig = prop.getProperty("url_to_config");
        prop.load(new FileInputStream(urlToConfig));

        botUserName = prop.getProperty("bot_name");
        botToken = prop.getProperty("bot_token");
        privateChatId = prop.getProperty("private_chat_id");
        urlToCatalogsFile = prop.getProperty("url_to_catalogs");
        urlToCatalogsDirWieldberries = prop.getProperty("url_to_catalogs_dir_wb");
        urlToAliExpressCatalog = prop.getProperty("url_to_aliexpress_catalogs");
        urlToCatalogsDirAliExpress = prop.getProperty("url_to_catalogs_dir_aliexpress");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getBotUserName() {
        return botUserName;
    }

    public String getBotToken() {
        return botToken;
    }

    public String getPrivateChatId() {
        return privateChatId;
    }

    public String getUrlToCatalogsFile() {
        return urlToCatalogsFile;
    }

    public String getUrlToCatalogsDirWieldberries() {
        return urlToCatalogsDirWieldberries;
    }

    public String getUrlToCatalogsDirAliExpress() {
        return urlToCatalogsDirAliExpress;
    }

    public String getUrlToAliExpressCatalog() {
        return urlToAliExpressCatalog;
    }
}
