package hci.me.smartkids.config;

/**
 * Author: Gary
 * Time: 17/1/10
 */

public class AppConfig {
    /**
     * config.xml中记录是否第一次登录
     */
    public final static String IS_FIRST_ENTER = "is_first";
    /**
     * 服务器地址
     */
    public final static String SERVER_ADDR = "http://192.168.142.150:8080/zhbj";
    /**
     * 侧边栏category API
     */
    public final static String CATEGORY_URL = SERVER_ADDR + "/categories.json";


}
