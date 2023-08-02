package jp.co.gawain.server.dao;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.gawain.server.constans.GawainMessageConstants;
import jp.co.gawain.server.dto.UserDto;
import jp.co.gawain.server.util.DatabaseUtility;
import jp.co.gawain.server.util.Utility;

public class UserDao {
    private static Logger logger = LoggerFactory.getLogger(UserDao.class);

    // ユーザー情報を登録するメソッド
    public void create(UserDto userDto) throws Exception {
        String sql = Utility.getSql("insertNewUser");
        List<Object> paramList = new ArrayList<Object>();

        try {
            paramList.add(userDto.getUserName());
            paramList.add(userDto.getEMail());
            DatabaseUtility.executeUpdate(sql, paramList);

        } catch (Exception e) {
            logger.error(GawainMessageConstants.DATABASE_ERROR_MESSAGE_000, e);
        }
    }

    // 全ユーザーを取得するメソッド
    public List<Map<String, Object>> get() throws Exception {
        // 結果格納用配列の作成
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        // SQL文作成
        String sql = Utility.getSql("getAllUser");
        
        try {
            resultList = DatabaseUtility.executeQueryWithoutParams(sql);
        } catch (Exception e) {
            logger.error(GawainMessageConstants.DATABASE_ERROR_MESSAGE_000, e);
        }
        return resultList;
    }
}
