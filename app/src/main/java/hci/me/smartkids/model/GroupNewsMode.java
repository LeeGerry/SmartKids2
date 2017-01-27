package hci.me.smartkids.model;

import java.util.ArrayList;

/**
 * 组图
 * Author: Gary
 * Time: 17/1/26
 */

public class GroupNewsMode extends BaseModel{
    public GroupNewsData data;
    public class GroupNewsData{
        public ArrayList<PhotoNews> news;
    }
    public class PhotoNews{
        public int id;
        public String listimage;
        public String title;
    }
}
