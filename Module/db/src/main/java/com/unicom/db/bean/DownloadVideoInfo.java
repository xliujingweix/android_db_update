package com.unicom.db.bean;

import com.unicom.db.anotation.DbField;
import com.unicom.db.anotation.DbTable;

import java.io.Serializable;

/**
 * Created by xliutaox on 2016/9/18.
 */
@DbTable("tbl_downloadvideoinfo")
public class DownloadVideoInfo implements Serializable {

    private static final long serialVersionUID = 2652486292646721636L;
    @DbField(column = "videoUrlName", notNull = true, unique = true)
    public String videoUrlName;
    @DbField(column = "downloadId")
    public String downloadId;
    @DbField(column = "downloadTime")
    public String downloadTime;

    public DownloadVideoInfo() {
    }

    public DownloadVideoInfo(String fileName, String downloadId, String downloadTime) {
        this.videoUrlName = fileName;
        this.downloadId = downloadId;
        this.downloadTime = downloadTime;
    }

    @Override
    public String toString() {
        return "DownloadVideoInfo{" +
                "videoUrlName='" + videoUrlName + '\'' +
                ", downloadId='" + downloadId + '\'' +
                ", downloadTime='" + downloadTime + '\'' +
                '}';
    }
}
