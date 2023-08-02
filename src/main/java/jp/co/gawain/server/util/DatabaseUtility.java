package jp.co.gawain.server.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseUtility {
    /**
     * コンストラクタ
     */
    private DatabaseUtility() {
    }

    /**
     * PreparedStatementを作成し、パラメータを設定する
     * @param connection コネクション
     * @param sql
     * @param paramList
     * @return PreparedStatement
     * @throws SQLException
     */
    private static PreparedStatement setSqlParam(Connection connection, String sql, List<Object> paramList) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql);
        if (paramList != null) {
            int index = 1;
            for (Object param : paramList) {
                ps.setObject(index, param);
                index++;
            }
        }
        return ps;
    }

     /**
     * SQLの実行結果をマップのリストで取得する
     * @param connection コネクション
     * @param sql 実行するSQL
     * @param paramList 設定するパラメーター
     * @return 実行結果
     * @throws SQLException
     */
    public static List<Map<String, Object>> executeQuery(String sql, List<Object> paramList) throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            ps = setSqlParam(connection, sql, paramList);
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            List<String> columnList = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                columnList.add(rsmd.getColumnName(i + 1));
            }
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                result.add(row);

                for (int i = 0; i < columnList.size(); i++) {
                    String key = columnList.get(i);
                    row.put(key, rs.getObject(key));
                }
            }
            return result;
        } catch (SQLException e) {
            // エラーをログに出力するか、カスタム例外にラップするなど、適切な処理を行う
            throw new RuntimeException("executeQueryでエラーが発生しました", e);
        } finally {
            // リソースを適切にクローズする
            DatabaseManager.closeResultSet(rs);
            DatabaseManager.closeStatement(ps);
            DatabaseManager.close();
        }
    }

    /**
     * SQLの実行結果をマップのリストで取得する
     * @param connection コネクション
     * @param sql 実行するSQL
     * @return 実行結果
     * @throws SQLException
     */
    public static List<Map<String, Object>> executeQueryWithoutParams(String sql) throws SQLException {
        return executeQuery(sql, null);
    }

    /**
     * SQLを実行する。
     * @param connection コネクション
     * @param sql 実行するSQL
     * @param params 設定するパラメーター
     * @return 実行結果
     */
    public static int executeUpdate(String sql, List<Object> paramList) {
        Connection connection = DatabaseManager.get();
        PreparedStatement ps = null;
        try {
            ps = setSqlParam(connection, sql, paramList);
            return ps.executeUpdate();
        } catch (SQLException e) {
            // エラーをログに出力するか、カスタム例外にラップするなど、適切な処理を行う
            throw new RuntimeException("executeUpdateでエラーが発生しました", e);
        } finally {
            // リソースを適切にクローズする
            DatabaseManager.closeStatement(ps);
        }
    }
}
