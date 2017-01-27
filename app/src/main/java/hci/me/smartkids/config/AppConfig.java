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
//    public final static String SERVER_ADDR = "http://172.17.83.159:8080/zhbj";
    public final static String SERVER_ADDR = "http://192.168.1.6:8080/zhbj";
    public final static String CURR_SERVER_IP = "192.168.1.6";
    /**
     * 侧边栏category API
     */
    public final static String CATEGORY_URL = SERVER_ADDR + "/categories.json";


    public static final String PHOTO_GROUP_URL = SERVER_ADDR + "/photos/photos_1.json";
}
