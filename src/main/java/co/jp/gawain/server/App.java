package co.jp.gawain.server;

import java.sql.Connection;

import co.jp.gawain.server.dao.UserDao;
import co.jp.gawain.server.dto.UserDto;

import co.jp.gawain.server.util.DatabaseManager;

public class App {
    public static void main(String[] args) {
        Connection connection = null;
        UserDto dto = new UserDto();

        dto.setUserName("admin");
        dto.setEMail("admin@example.com");

        try {
            connection = DatabaseManager.getConnection();
            UserDao dao = new UserDao();
            dao.insertNewUser(connection, dto);
            DatabaseManager.commit(connection);
        } catch (Exception e) {
            DatabaseManager.rollback(connection);
        } finally {
            DatabaseManager.end(connection);
        }

    }
}
