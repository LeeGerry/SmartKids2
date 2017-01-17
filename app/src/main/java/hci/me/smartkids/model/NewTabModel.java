package hci.me.smartkids.model;

import java.util.ArrayList;

/**
 * 新闻center model
 * Author: Gary
 * Time: 17/1/16
 */

public class NewTabModel extends BaseModel {
    public NewsTab data;
    public class NewsTab{
        public String more;
        public ArrayList<NewsData> news;
        public ArrayList<TopData> topnews;
    }

    /**
     * 新闻列表对象
     */
    public class NewsData{
        public int id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;
    }
    /**
     * top列表对象
     */
    public class TopData{
        public int id;
        public String topimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;
    }
}
