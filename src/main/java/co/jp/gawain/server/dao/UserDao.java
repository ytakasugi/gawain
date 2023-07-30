package co.jp.gawain.server.dao;

import java.util.List;

import co.jp.gawain.server.dto.UserDto;
import co.jp.gawain.server.util.DatabaseUtility;
import co.jp.gawain.server.util.Utility;

import java.util.ArrayList;
import java.sql.Connection;

public class UserDao {
    // ユーザー情報を登録するメソッド
    public void insertNewUser(Connection connection, UserDto userDto) throws Exception {
        String sql = Utility.getSql("insertNewUser");
        List<Object> paramList = new ArrayList<Object>();

        try {
            paramList.add(userDto.getUserName());
            paramList.add(userDto.getEMail());
            DatabaseUtility.executeUpdate(connection, sql, paramList);

        } catch (Exception e) {
            throw new Exception();
        }
    }
}
