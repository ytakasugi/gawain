package jp.co.gawain.server;

import jp.co.gawain.server.api.UserApi;
import jp.co.gawain.server.dto.UserDto;
import jp.co.gawain.server.util.DatabaseManager;

public class App {
    public static void main(String[] args) {
        UserDto dto = new UserDto();
        dto.setUserName("test0001");
        dto.setEMail("test0001@example.com");
        UserApi.newUser(dto);
        UserApi.getAllUser();
        DatabaseManager.cloeseDatasSource();
    }
}
