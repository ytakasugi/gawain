package jp.co.gawain.server.api;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.gawain.server.constans.GawainMessageConstants;
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
            logger.info(GawainMessageConstants.APPLICATION_INFO_MESSAGE_001);
            resultList = dao.getAllUser();
            Utility.printUser(resultList);
            logger.info(GawainMessageConstants.APPLICATION_INFO_MESSAGE_002);
        } catch (Exception e) {
            logger.error(GawainMessageConstants.APPLICATION_ERROR_MESSAGE_001, e);
        } finally {
            DatabaseManager.close(connection);
        }
        return resultList;
    }

    public static void create(UserDto dto) {
        Connection connection = null;

        try {
            connection = DatabaseManager.getConnection();
            UserDao dao = new UserDao(connection);
            logger.info(GawainMessageConstants.APPLICATION_INFO_MESSAGE_003);
            dao.create(dto);
            logger.info(GawainMessageConstants.APPLICATION_INFO_MESSAGE_004);
            DatabaseManager.commit(connection);
        } catch (Exception e) {
            logger.error(GawainMessageConstants.APPLICATION_ERROR_MESSAGE_001, e);
            DatabaseManager.rollback(connection);
        } finally {
            DatabaseManager.close(connection);
        }
    }
}
