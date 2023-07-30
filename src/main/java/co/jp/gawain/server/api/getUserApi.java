package co.jp.gawain.server.api;

import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.jp.gawain.server.dao.UserDao;

import co.jp.gawain.server.util.DatabaseManager;
import co.jp.gawain.server.util.Utility;

public class getUserApi {
    private static Logger logger = LoggerFactory.getLogger(getUserApi.class);
    public static void main(String[] args) {
        Connection connection = null;

        try {
            logger.info("ユーザー情報を取得します");
            connection = DatabaseManager.getConnection();
            UserDao dao = new UserDao(connection);
            Utility.printUser(dao.getAllUserInfo());
            logger.info("ユーザー情報を取得しました");
        } catch (Exception e) {
            // 何もしない
            logger.error("エラーが発生しました");
        } finally {
            logger.info("コネクションをクローズします");
            DatabaseManager.close(connection);
            logger.info("コネクションをクローズしました");
        }
    }
}
