package com.unicom.module;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.unicom.db.bean.CommonRelation;
import com.unicom.db.operation.DbOperation;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CommonRelation commonRelation = new CommonRelation();
        commonRelation.key = "key";
        commonRelation.value = "value";
        DbOperation<CommonRelation> dbOperation = new DbOperation(this, CommonRelation.class);
        dbOperation.insert(commonRelation);

        List<CommonRelation> list = dbOperation.queryList(CommonRelation.class, null, null, null,null);
        Log.e("TAG", list.toString());
    }
}
