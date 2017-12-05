package com.unicom.db.bean;

import com.unicom.db.anotation.DbField;
import com.unicom.db.anotation.DbTable;

@DbTable("tbl_simple_orginfo")
public class SimpleOrgInfo {
    @DbField(column = "subId", notNull = true, unique = true)
    public String subId;
    @DbField(column = "orgId")
    public String orgId;
    @DbField(column = "orgName")
    public String orgName;
    @DbField(column = "orgIcon")
    public String orgIcon;
    @DbField(column = "orgIsV", type = "Integer")
    public int orgIsV;
    @DbField(column = "orgSeq", type = "Integer")
    public int orgSeq;
    @DbField(column = "updateTime")
    public String updateTime;

    @Override
    public String toString() {
        return "SimpleOrgInfo{" +
                "subId='" + subId + '\'' +
                ", orgId='" + orgId + '\'' +
                ", orgName='" + orgName + '\'' +
                ", orgIcon='" + orgIcon + '\'' +
                ", orgIsV=" + orgIsV +
                ", orgSeq=" + orgSeq +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
