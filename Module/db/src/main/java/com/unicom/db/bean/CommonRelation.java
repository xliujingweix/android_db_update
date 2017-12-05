package com.unicom.db.bean;


import com.unicom.db.anotation.DbField;
import com.unicom.db.anotation.DbTable;

@DbTable("tbl_common_relation")
public class CommonRelation {

    @DbField(column = "key", unique = true)
    public String key;

    @DbField(column = "value")
    public String value;

    @Override
    public String toString() {
        return "CommonRelation{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
