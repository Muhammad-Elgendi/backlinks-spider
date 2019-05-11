package com.seospider.seospider.db;

import edu.uci.ics.crawler4j.crawler.Page;

import java.sql.Timestamp;
import java.util.Map;

/**
 * DBService interface and its implementation is DBServiceImpl.java
 */
public interface DBService {

    void storeBacklink(String srcUrl,String targetUrl,String anchor_text,Boolean is_dofollow);
    void close();
}