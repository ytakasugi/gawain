package jp.co.gawain.server.util;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utility {
    private static final String COMMON_PROP_PATH = "./application.properties";
    private static final String SQL_PROP_PATH = "./sql.properties";

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
        Properties prop = new Properties();
        try {
            InputStream is = new FileInputStream(COMMON_PROP_PATH);
            prop.load(is);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(key);
    }

    /**
     * SQLプロパティを読み込み、keyと一致する値(SQL)を取得する
     * @param key
     * @return String
     */
    public static String getSql(String key) {
        Properties prop = new Properties();
        try {
            InputStream is = new FileInputStream(SQL_PROP_PATH);
            prop.load(is);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(key);
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
