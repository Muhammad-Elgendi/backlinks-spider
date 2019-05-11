package com.seospider.seospider.crawler;

import com.seospider.seospider.SimpleLauncher;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import com.seospider.seospider.db.DBService;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.regex.Pattern;
import org.apache.http.Header;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class PostgresWebCrawler extends WebCrawler {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PostgresWebCrawler.class);

    private final DBService postgresDBService;
//    Map<String, Object> problems;

    public PostgresWebCrawler(DBService postgresDBService) {
        this.postgresDBService = postgresDBService;
    }


    /**
     * This function is called just before starting the crawl by this crawler
     * instance. It can be used for setting up the data structures or
     * initializations needed by this crawler instance.
     */
    @Override
    public void onStart() {
        // Do nothing by default
        // Sub-classed can override this to add their custom functionality
//        problems = new HashMap<>();
    }

    /**
     * This method receives two parameters. The first parameter is the page in
     * which we have discovered this new url and the second parameter is the new
     * url. You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic). In this example,
     * we are instructing the crawler to ignore urls that have css, js, git, ...
     * extensions and to only accept urls that start with
     * "http://www.ics.uci.edu/". In this case, we didn't need the referringPage
     * parameter to make the decision.
     *
     * @param referringPage
     * @param url
     * @return
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {

        String newURL = url.getURL().toLowerCase();
        String pageDomain =referringPage.getWebURL().getDomain().toLowerCase();
        return !newURL.contains(pageDomain);
    }

    @Override
    public void visit(Page page) {

        String url = page.getWebURL().getURL();
        int status =page.getStatusCode();
        String parent =page.getWebURL().getParentUrl();
        String anchor =page.getWebURL().getAnchor();
//        controller.insert("LINK : "+url+" STATUS : "+status+" TEXT : "+anchor+" FOUND : "+parent);
        // store new backlinks
        try {
            postgresDBService.storeBacklink(parent,url,anchor,true);
        } catch (RuntimeException e) {
            logger.error("Storing backlinks failed", e);
        }

    }

    @Override
    public void onUnexpectedStatusCode(String urlStr, int statusCode, String contentType,String description){
//        try {
//            if(statusCode==12007 && Whois.isAvailable(Whois.getUrlDomainName(urlStr))==true){
//                controller.insert("Expired Domain : "+urlStr );
//            }
//        } catch (IOException ex) {
//
//        }

    }





    @Override
    public void onBeforeExit() {
//        if (postgresDBService != null) {
//            postgresDBService.close();
//        }
    }
}
