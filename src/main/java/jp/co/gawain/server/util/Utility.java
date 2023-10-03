package jp.co.gawain.server.util;

import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.util.ResourceBundle;

public class Utility {
    private static final String COMMON_PROP_NAME = "application";
    private static final String SQL_PROP_NAME = "sql";

    /**
     * コンストラクタ
     */
    private Utility() {
    }
    /**
     * 共通プロパティを読み込み、keyと一致する値を取得する
     * @param key
     * @return String
     */
    public static String getProp(String key) {
        ResourceBundle rb = ResourceBundle.getBundle(COMMON_PROP_NAME, Locale.US);
        return rb.getString(key);
    }

    /**
     * SQLプロパティを読み込み、keyと一致する値(SQL)を取得する
     * @param key
     * @return String
     */
    public static String getSql(String key) {
        ResourceBundle rb = ResourceBundle.getBundle(SQL_PROP_NAME, Locale.US);
        return rb.getString(key);
    } 

    /**
     * ユーザー情報を標準出力する
     * @param UserDto
     */
    public static void printUser(List<Map<String, Object>> user) {
        for (int i = 0; i < user.size(); i++) {
            System.out.printf(
             "%s, %s, %s \n", 
                user.get(i).get("user_id"),
                user.get(i).get("user_name"),
                user.get(i).get("e_mail")
            );
        }
    }
}