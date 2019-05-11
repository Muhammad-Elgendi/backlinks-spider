package com.seospider.seospider;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import com.seospider.seospider.crawler.PostgresCrawlerFactory;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;


public class SimpleLauncher {

    private static final Logger logger = LoggerFactory.getLogger(SimpleLauncher.class);

    public static void main(String[] args) throws Exception {

//        if (args.length != 5) {
//            logger.info("Needed parameters: ");
//            logger.info("\t Seed URL (start crawling with this URL)");
//            logger.info("\t maxPagesToFetch (number of pages to be fetched)");
//            logger.info("\t nuberOfCrawlers (number of crawlers)");
//            logger.info("\t user id (id of user that request the crawling)");
//            logger.info("\t site id (id of site that being crawled)");
//            return;
//        }

        // Handle arguments
//        URL url = new URL(args[0]);
//        mainUrl= args[0];
//        userId = Integer.valueOf(args[3]);
//        siteId = Integer.valueOf(args[4]);
//        int numberOfCrawlers = Integer.valueOf(args[2]);


        int numberOfCrawlers = 7;

//        logger.info("Crawler Started : ");
//        logger.info("\t Seed URL : "+mainUrl);
//        logger.info("\t maxPagesToFetch : "+Integer.valueOf(args[1]));
//        logger.info("\t nuberOfCrawlers : "+numberOfCrawlers);
//        logger.info("\t user id : "+userId);
//        logger.info("\t site id : "+siteId);

        CrawlConfig config = new CrawlConfig();

        config.setPolitenessDelay(200);

        config.setCrawlStorageFolder("/media/muhammad/disk/crawlerData/backlinks");

        config.setRespectNoFollow(false);

        config.setRespectNoIndex(false);

        config.setUserAgentString("SEO-spider (https://github.com/Muhammad-Elgendi/SEO-spider/)");

        /*
         * Instantiate the controller for this crawl.
         */
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();

        robotstxtConfig.setUserAgentName("SEO-spider");

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
//        controller.addSeed(args[0]);
        controller.addSeed("https://dmoztools.net/");
        controller.addSeed("https://botw.org/");

        Dotenv dotenv = Dotenv.configure().directory("./").load();

//        Flyway flyway = new Flyway();
//        flyway.setDataSource(dotenv.get("JDBC_URL"), dotenv.get("DB_USER_NAME"), dotenv.get("DB_PASSWORD"));
//        flyway.migrate();


        ComboPooledDataSource pool = new ComboPooledDataSource();
        pool.setDriverClass("org.postgresql.Driver");
        pool.setJdbcUrl(dotenv.get("JDBC_URL"));
        pool.setUser(dotenv.get("DB_USER_NAME"));
        pool.setPassword(dotenv.get("DB_PASSWORD"));
//        pool.setMaxPoolSize(numberOfCrawlers);
//        pool.setMinPoolSize(numberOfCrawlers);
//        pool.setInitialPoolSize(1);

        logger.info("Starting Crawling Process ... ");
        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(new PostgresCrawlerFactory(pool), numberOfCrawlers);

        logger.info("Crawling Process Has Finished ... ");

        pool.close();

        logger.info("The End");
    }


    public static java.sql.Timestamp getCurrentTimeStamp() {

        return new java.sql.Timestamp(new java.util.Date().getTime());

    }

}