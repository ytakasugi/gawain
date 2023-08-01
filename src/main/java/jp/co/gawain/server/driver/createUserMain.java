package jp.co.gawain.server.driver;

import java.util.List;
import java.util.ArrayList;

import jp.co.gawain.server.api.UserApi;
import jp.co.gawain.server.dto.UserDto;

public class createUserMain {
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
        
        for (int i = 0; i < size; i++) {
            UserApi.create(newUserList.get(i));
        }
    }
}
