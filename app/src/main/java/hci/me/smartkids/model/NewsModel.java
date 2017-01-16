package hci.me.smartkids.model;

import java.util.ArrayList;

/**
 * Author: Gary
 * Time: 17/1/13
 * 使用Gson解析时，书写技巧
 * 1. 逢{}创建对象，逢[]创建集合ArrayList
 * 2. 所有字段名要和json返回字段高度一致
 */

public class NewsModel extends BaseModel {
    public int retCode;
    public ArrayList<Integer> extend;
    public ArrayList<NewsMenuData> data;

    public class NewsMenuData{
        public int id;
        public String title;
        public int type;
        public ArrayList<NewsTabData> children;

        @Override
        public String toString() {
            return "NewsMenuData{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", children=" + children +
                    '}';
        }
    }
    public class NewsTabData{
        public int id;
        public String title;
        public int type;
        public String url;

        @Override
        public String toString() {
            return "NewsTabData{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NewsModel{" +
                "retCode=" + retCode +
                ", extend=" + extend +
                ", data=" + data +
                '}';
    }
}
