package me.ele.panicbuying.utils;

/**
 * Created by xczhang on 15/11/12 下午3:22.
 */
public class Config {
    public static String APP_HOST;
    public static String APP_PORT;

    public static String DB_HOST;
    public static String DB_PORT;
    public static String DB_NAME;
    public static String DB_USER;
    public static String DB_PASS;

    public static String REDIS_HOST;
    public static String REDIS_PORT;


    static {
        load_config();
    }


    public static void load_config() {
        APP_HOST = System.getenv("APP_HOST");
        APP_PORT = System.getenv("APP_PORT");

        DB_HOST = System.getenv("DB_HOST");
        DB_PORT = System.getenv("DB_PORT");
        DB_NAME = System.getenv("DB_NAME");
        DB_USER = System.getenv("DB_USER");
        DB_PASS = System.getenv("DB_PASS");

        REDIS_HOST = System.getenv("REDIS_HOST");
        REDIS_PORT = System.getenv("REDIS_PORT");
    }
}
