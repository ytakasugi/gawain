package co.jp.gawain.server.api;

import java.sql.Connection;

import co.jp.gawain.server.dao.UserDao;

import co.jp.gawain.server.util.DatabaseManager;
import co.jp.gawain.server.util.Utility;

public class getUserApi {
    public static void main(String[] args) {
        Connection connection = null;

        try {
            connection = DatabaseManager.getConnection();
            UserDao dao = new UserDao(connection);

            Utility.printUser(dao.getAllUserInfo());
        } catch (Exception e) {
            // 何もしない
        } finally {
            DatabaseManager.close(connection);
        }
    }
}
