package co.jp.gawain.server;

import co.jp.gawain.server.api.UserApi;
import co.jp.gawain.server.dto.UserDto;

public class App {
    public static void main(String[] args) {
        UserDto dto = new UserDto();
        dto.setUserName("test0001");
        dto.setEMail("test0001@example.com");

        UserApi.newUser(dto);

        UserApi.getAllUser();
    }
}
