package jp.co.gawain.server;

import jp.co.gawain.server.util.Utility;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!!");
        System.out.println(System.getProperty("java.class.path"));
        System.out.println(Utility.getProp("db.user"));
    }
}
