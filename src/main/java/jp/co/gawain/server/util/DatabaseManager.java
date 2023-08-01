package jp.co.gawain.server.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jp.co.gawain.server.constans.GawainMessageConstants;

public class DatabaseManager {
    // データベース関連のプロパティ
    private static final String JDBC_URL = Utility.getProp("jdbc.url");
    private static final String DB_USER = Utility.getProp("db.user");
    private static final String DB_PASS = Utility.getProp("db.password");
    private static final int CONNECTION_POOL_SIZE = Integer.parseInt(Utility.getProp("connection.pool.size"));

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;

    private static ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<>();

    private static Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

    static {
        try {
            config.setJdbcUrl(JDBC_URL);
            config.setUsername(DB_USER);
            config.setPassword(DB_PASS);
            config.setMaximumPoolSize(CONNECTION_POOL_SIZE);

            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     /**
     * コンストラクタ
     */
    private DatabaseManager() {
    }

    /**
     * コネクションを取得・作成する
     * @return Connection
     */
    public static Connection getConnection() throws SQLException {
        Connection connection = threadLocalConnection.get();
        if (connection == null || connection.isClosed()) {
            logger.info(GawainMessageConstants.DATABASE_INFO_MESSAGE_001);
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            threadLocalConnection.set(connection);
        }
        return connection;
    }

    /**
     * 自動コミットがTrueだった場合、メッセージを出力する
     */
    public static void validate(Connection conn) {
        try {
            if (conn.getAutoCommit()) {
                logger.info(GawainMessageConstants.DATABASE_INFO_MESSAGE_002);
            }
        } catch (SQLException e) {
            logger.error(GawainMessageConstants.DATABASE_ERROR_MESSAGE_001, e);
        }
    }

    /**
     * ThreadLocalからスレッドを削除する
     * @param flag
     */
    private static void remove(boolean flag) {
        if (flag) {
            threadLocalConnection.remove();
        }
    }

    /**
     * コネクションをクローズする
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                logger.info(GawainMessageConstants.DATABASE_INFO_MESSAGE_003);
                conn.close();
                logger.info(GawainMessageConstants.DATABASE_INFO_MESSAGE_004);
            } catch (Exception e) {
                logger.error(GawainMessageConstants.DATABASE_ERROR_MESSAGE_002, e);
            } finally {
                remove(true);
            }
        }
    }

    /**
     * コミット
     */
    public static void commit(Connection conn) {
        if (conn != null) {
            try {
                logger.info(GawainMessageConstants.DATABASE_INFO_MESSAGE_005);
                conn.commit();
                logger.info(GawainMessageConstants.DATABASE_INFO_MESSAGE_006);
            } catch (SQLException e) {
                logger.error(GawainMessageConstants.DATABASE_ERROR_MESSAGE_003, e);
            }
        }
    }

    /**
     * ロールバック
     */
    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                logger.info(GawainMessageConstants.DATABASE_INFO_MESSAGE_007);
                conn.rollback();
                logger.info(GawainMessageConstants.DATABASE_INFO_MESSAGE_008);
            } catch (SQLException e) {
                logger.error(GawainMessageConstants.DATABASE_ERROR_MESSAGE_004, e);
            }
        }
    }

    /**
     * PreparedStatementのクローズする
     * @param target
     */
    public static void closeStatement(PreparedStatement target) {
        if (target != null) {
            try {
                target.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ResultSetのクローズする
     * @param target
     */
    public static void closeResultSet(ResultSet target) {
        if (target != null) {
            try {
                target.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * データソースをクローズする
     */
    public static void cloeseDatasSource() {
        if (dataSource != null) {
            logger.info(GawainMessageConstants.DATABASE_INFO_MESSAGE_009);
            dataSource.close();
            logger.info(GawainMessageConstants.DATABASE_INFO_MESSAGE_010);
        }
    }

}
