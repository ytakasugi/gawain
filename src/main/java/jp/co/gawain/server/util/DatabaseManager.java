package jp.co.gawain.server.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseManager {
    // データベース関連のプロパティ
    private static final String JDBC_URL = Utility.getProp("jdbc.url");
    private static final String DB_USER = Utility.getProp("db.user");
    private static final String DB_PASS = Utility.getProp("db.password");

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;

    private static ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<>();

    private static Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

    static {
        try {
            config.setJdbcUrl(JDBC_URL);
            config.setUsername(DB_USER);
            config.setPassword(DB_PASS);

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
            logger.info("コネクションを取得します");
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
                logger.info("自動コミットが有効です");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                logger.info("コネクションをクローズします");
                closeConnection(conn);
                logger.info("コネクションをクローズしました");
            } catch (Exception e) {
                logger.error("コネクションのクローズに失敗しました", e);
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
                logger.info("トランザクションをコミットします");
                conn.commit();
                logger.info("トランザクションをコミットしました");
            } catch (SQLException e) {
                logger.error("トランザクションのコミットに失敗しました", e);
            }
        }
    }

    /**
     * ロールバック
     */
    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                logger.info("トランザクションをロールバックします");
                conn.rollback();
                logger.info("トランザクションをロールバックしました");
            } catch (SQLException e) {
                logger.error("トランザクションのロールバックに失敗しました", e);
            }
        }
    }

    /**
     * Connectionをクローズする
     * @param target
     */
    private static void closeConnection(Connection target) {
        if (target != null) {
            try {
                logger.info("コネクションをクローズします");
                target.close();
                logger.info("コネクションをクローズしました");
            } catch (SQLException e) {
                logger.error("コネクションのロールバックに失敗しました", e);
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
            logger.info("データソースをクローズします");
            dataSource.close();
            logger.info("データソースをクローズしました");
        }
    }

}
