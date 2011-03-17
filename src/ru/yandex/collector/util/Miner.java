package ru.yandex.collector.util;

import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;

/**
 * Created by IntelliJ IDEA.
 * User: system.29a
 * Date: 08.03.11
 * Time: 17:33
 * Decription: Класс для парсинга сайта и сборки данных.
 */

public class Miner {
    private final String path;
    private final String configFile;

    public Miner(String path, String configFile) {
        this.path = path;
        this.configFile = configFile;
    }

    public void minerStart() {
        try {
            ScraperConfiguration config = new ScraperConfiguration(path + configFile);
            Scraper scraper = new Scraper(config, path);
            scraper.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
