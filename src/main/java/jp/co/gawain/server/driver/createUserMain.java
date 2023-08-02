package jp.co.gawain.server.driver;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.gawain.server.constans.GawainMessageConstants;
import jp.co.gawain.server.dao.UserDao;
import jp.co.gawain.server.dto.UserDto;
import jp.co.gawain.server.util.DatabaseManager;

public class createUserMain {
    private static Logger logger = LoggerFactory.getLogger(createUserMain.class);
    public static void main(String[] args) {
        List<UserDto> newUserList = new ArrayList<UserDto>();

        for (int i = 0; i < 50; i++) {
            UserDto dto = new UserDto();

            String name = "test" + String.format("%05d", i);
            String email = "test" + String.format("%05d", i) + "@example.com";

            dto.setUserName(name);
            dto.setEMail(email);

            newUserList.add(dto);
        }

        int size = newUserList.size();

        try {
            UserDao dao = new UserDao();

            for (int i = 0; i < size; i++) {
                DatabaseManager.begin();
                logger.info(GawainMessageConstants.APPLICATION_INFO_MESSAGE_003);
                dao.create(newUserList.get(i));
                logger.info(GawainMessageConstants.APPLICATION_INFO_MESSAGE_004);
                DatabaseManager.commit();
            }
        } catch (Exception e) {
            logger.error(GawainMessageConstants.APPLICATION_ERROR_MESSAGE_001, e);
            DatabaseManager.rollback();
        } finally {
            DatabaseManager.close();
            DatabaseManager.closeDatasSource();
        }
    }
}
