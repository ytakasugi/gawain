package co.jp.gawain.server.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                System.out.println("Auto Commit is True");
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
                closeConnection(conn);
            } catch (Exception e) {
                e.printStackTrace();
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
                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ロールバック
     */
    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
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
                target.close();
            } catch (SQLException e) {
                e.printStackTrace();
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
    public void cloeseDatasSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

}
