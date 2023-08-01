# gawain

## DatabaseManagerクラス改善案

```java
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

public class ConnectionManager {
    private static final String JDBC_URL = Utility.getProp("jdbc.url");
    private static final String DB_USER = Utility.getProp("db.user");
    private static final String DB_PASS = Utility.getProp("db.password");
    private static final int CONNECTION_POOL_SIZE = Integer.parseInt(Utility.getProp("connection.pool.size"));

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;

    private static ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<>();

    private static Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

    private static ConnectionManager instance = new ConnectionManager();

    private ConnectionManager() {
        try {
            config.setJdbcUrl(JDBC_URL);
            config.setUsername(DB_USER);
            config.setPassword(DB_PASS);
            config.setMaximumPoolSize(CONNECTION_POOL_SIZE);
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            logger.error("コネクションプール作成に失敗しました", e);
        }
    }

    public static ConnectionManager getInstance() {
        return instance;
    }

    /**
     * コンネクションを取得・作成する
     * @return Connection
     */
    public Connection getConnection() throws SQLException {
        Connection connection = threadLocalConnection.get();
        if (connection == null || connection.isClosed()) {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            threadLocalConnection.set(connection);
        }
        return connection;
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

    // 新しいトランザクションを開始する
    public void begin() {
        Connection connection = threadLocalConnection.get();
        if (connection == null) {
            try {
                connection = dataSource.getConnection();
                connection.setAutoCommit(false);
                threadLocalConnection.set(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * コンミットを行う
     */
    public void commit() {
        Connection connection = threadLocalConnection.get();
        if (connection != null) {
            try {
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ロールバックを行う
     */
    public void rollback() {
        Connection connection = threadLocalConnection.get();
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * データベース接続をクローズする
     */
    public void closeConnection() {
        Connection connection = threadLocalConnection.get();
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                remove(true);
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
```

## DatabaseUtilityクラス改善案

```java
public static List<Map<String, Object>> executeQuery(String sql, List<Object> paramList) {
    ConnectionManager connectionManager = ConnectionManager.getInstance();
    Connection connection = null;
    try {
        connection = connectionManager.getConnection();
        return executeQuery(connection, sql, paramList);
    } catch (SQLException e) {
        // エラーをログに出力するか、カスタム例外にラップするなど、適切な処理を行う
        throw new RuntimeException("executeQueryでエラーが発生しました", e);
    } finally {
        // リソースを適切にクローズする
        connectionManager.closeConnection();
    }
}
```
