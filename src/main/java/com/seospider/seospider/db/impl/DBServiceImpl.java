package com.seospider.seospider.db.impl;


import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.seospider.seospider.SimpleLauncher;
import com.seospider.seospider.db.DBService;

import org.slf4j.Logger;

import java.sql.*;


/**
 * Implementation of DBservice which is an interface that deals with DB
 */
public class DBServiceImpl implements DBService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(com.seospider.seospider.db.impl.DBServiceImpl.class);

    private ComboPooledDataSource comboPooledDataSource;

    private PreparedStatement insertBacklinkStatement;

    public DBServiceImpl(ComboPooledDataSource comboPooledDataSource) throws SQLException {
        this.comboPooledDataSource = comboPooledDataSource;

    }

    @Override
    public void storeBacklink(String srcUrl,String targetUrl,String anchor,Boolean isDofollow) {
        try {
            insertBacklinkStatement =  comboPooledDataSource.getConnection().prepareStatement("insert into backlinks values " +
                    "(?,?,?,?,?)");
            insertBacklinkStatement.setString(1,srcUrl);
            insertBacklinkStatement.setString(2,targetUrl);
            insertBacklinkStatement.setString(3,anchor);
            insertBacklinkStatement.setBoolean(4,isDofollow);
            insertBacklinkStatement.setTimestamp(5, SimpleLauncher.getCurrentTimeStamp());
            insertBacklinkStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQL Exception while storing backlink", e);
            throw new RuntimeException(e);
        }finally {
//            if (insertBacklinkStatement != null) {
//                try {
//                    insertBacklinkStatement.close();
//                } catch (SQLException e) {
//                    logger.error("SQL Exception while closing insertBacklinkStatement'{}'", e);
//                }
//            }
            try {
                if (insertBacklinkStatement.getConnection() != null)
                    insertBacklinkStatement.getConnection().close();
            } catch (SQLException e) {
                logger.error("SQL Exception while closing connection insertBacklinkStatement'{}'",e);
            }
        }
    }

    @Override
    public void close() {
        if (comboPooledDataSource != null) {
            comboPooledDataSource.close();
        }
    }
}