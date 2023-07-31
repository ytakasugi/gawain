package jp.co.gawain.server.api;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.gawain.server.dao.UserDao;
import jp.co.gawain.server.dto.UserDto;
import jp.co.gawain.server.util.DatabaseManager;
import jp.co.gawain.server.util.Utility;

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
            logger.error("エラーが発生しました", e);
        } finally {
            DatabaseManager.close(connection);
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
            DatabaseManager.commit(connection);
        } catch (Exception e) {
            logger.error("エラーが発生しました。", e);
            DatabaseManager.rollback(connection);
        } finally {
            DatabaseManager.close(connection);
        }
    }
}
