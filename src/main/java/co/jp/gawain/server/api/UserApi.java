package co.jp.gawain.server.api;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.jp.gawain.server.dao.UserDao;
import co.jp.gawain.server.dto.UserDto;
import co.jp.gawain.server.util.DatabaseManager;
import co.jp.gawain.server.util.Utility;

public class UserApi {
    private static Logger logger = LoggerFactory.getLogger(UserApi.class);

    public static List<Map<String, Object>> getAllUser() {
        Connection connection = null;
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        try {
            connection = DatabaseManager.getConnection();
            UserDao dao = new UserDao(connection);
            logger.info("ユーザー情報を取得します");
            resultList = dao.getAllUserInfo();
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
        return resultList;
    }

    public static void newUser(UserDto dto) {
        Connection connection = null;

        try {
            connection = DatabaseManager.getConnection();
            UserDao dao = new UserDao(connection);
            logger.info("ユーザー情報を作成します");
            dao.insertNewUser(dto);
            logger.info("ユーザー情報を作成しました");
            logger.info("トランザクションのコミットを実行します");
            DatabaseManager.commit(connection);
            logger.info("トランザクションのコミットを実行しました");
        } catch (Exception e) {
            logger.error("エラーが発生しました。トランザクションのロールバックを実行します");
            DatabaseManager.rollback(connection);
            logger.info("トランザクションのロールバックを実行しました");
        } finally {
            logger.info("コネクションをクローズします");
            DatabaseManager.close(connection);
            logger.info("コネクションをクローズしました");
        }
    }
}
