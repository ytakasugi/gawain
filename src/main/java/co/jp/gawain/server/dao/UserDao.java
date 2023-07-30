package co.jp.gawain.server.dao;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.sql.Connection;

import co.jp.gawain.server.dto.UserDto;
import co.jp.gawain.server.util.DatabaseUtility;
import co.jp.gawain.server.util.Utility;

public class UserDao {
    private Connection connection;
    /**
     * コンストラクタ
     * @param connection
     */
    public UserDao(Connection connection) {
        this.connection = connection;
    }
    // ユーザー情報を登録するメソッド
    public void insertNewUser(UserDto userDto) throws Exception {
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

    // 全ユーザーを取得するメソッド
    public List<Map<String, Object>> getAllUserInfo() throws Exception {
        // 結果格納用配列の作成
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        // SQL文作成
        String sql = Utility.getSql("getAllUser");
        
        try {
            resultList = DatabaseUtility.executeQueryWithoutParams(connection, sql);
        } catch (Exception e) {
            throw new Exception();
        } finally {
        }
        return resultList;
    }
}
