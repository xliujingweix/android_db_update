package com.unicom.db.bean;

import android.text.TextUtils;

import com.unicom.db.anotation.DbField;
import com.unicom.db.anotation.DbTable;

import java.io.Serializable;


/**
 * Created by Terry on 2017/11/22.
 * 对应接口orgDetail返回的数据
 * 其中phone和menu里面的数据是以String整理存储在数据库
 */
@DbTable("tbl_organizationinfo")
public class OrganizationInfo {
    /**
     * 对应phone里面的第一个phoneNumber
     */
    @DbField(column = "officePhone")
    public String officePhone;

    @DbField(column = "orgPicUrl")
    public String orgPicUrl;

    @DbField(column = "orgVideoUrl")
    public String orgVideoUrl;

    @DbField(column = "orgYpPicUrl")
    public String orgYpPicUrl;

    @DbField(column = "orgYpVideoUrl")
    public String orgYpVideoUrl;

    @DbField(column = "menu")
    public String menu;

    @DbField(column = "updateTime")
    public String updateTime;

    @Override
    public String toString() {
        return "OrganizationInfo{" +
                "officePhone='" + officePhone + '\'' +
                ", orgPicUrl='" + orgPicUrl + '\'' +
                ", orgVideoUrl='" + orgVideoUrl + '\'' +
                ", orgYpPicUrl='" + orgYpPicUrl + '\'' +
                ", orgYpVideoUrl='" + orgYpVideoUrl + '\'' +
                ", menu='" + menu + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
