package jp.co.gawain.server.api;

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

    public static List<Map<String, Object>> get() {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        try {
            UserDao dao = new UserDao();
            logger.info(GawainMessageConstants.APPLICATION_INFO_MESSAGE_001);
            resultList = dao.get();
            Utility.printUser(resultList);
            logger.info(GawainMessageConstants.APPLICATION_INFO_MESSAGE_002);
        } catch (Exception e) {
            logger.error(GawainMessageConstants.APPLICATION_ERROR_MESSAGE_001, e);
        } finally {
            DatabaseManager.close();
        }
        return resultList;
    }

    public static void create(UserDto dto) {
        try {
            DatabaseManager.begin();
            UserDao dao = new UserDao();
            logger.info(GawainMessageConstants.APPLICATION_INFO_MESSAGE_003);
            dao.create(dto);
            logger.info(GawainMessageConstants.APPLICATION_INFO_MESSAGE_004);
            DatabaseManager.commit();
        } catch (Exception e) {
            logger.error(GawainMessageConstants.APPLICATION_ERROR_MESSAGE_001, e);
            DatabaseManager.rollback();
        } finally {
            DatabaseManager.close();
        }
    }
}
