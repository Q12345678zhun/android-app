package com.baidu.ardemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xgr.wonderful.R;

public class IntroActivity extends Activity {
    private String mKey;
    private int mType;
    private String mTitle;
    private String mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Bundle bundle = getIntent().getExtras();
        mKey = bundle.getString("");
        mType = bundle.getInt("");
        mTitle = bundle.getString("");
        mDescription = bundle.getString("");
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.intro_title)).setText(mTitle);
        ((TextView) findViewById(R.id.intro_detail)).setText(mDescription);
        findViewById(R.id.call_ar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, ARActivity.class);
                intent.putExtra("", mKey);
                intent.putExtra("", mType);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}