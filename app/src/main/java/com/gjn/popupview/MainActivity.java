package com.gjn.popupview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.gjn.popupviewlibrary.PopupWindowUtils;

public class MainActivity extends AppCompatActivity {

    private PopupWindowUtils popupWindowUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        popupWindowUtils = new PopupWindowUtils(this, R.layout.popup_test) {
            @Override
            public void bindView(final Activity activity, View view) {
                setTextViewText(R.id.tv_pt, "你好测试tv");
                setTextViewText(R.id.tv_pt2, "测试tv2");
                setIdOnClickListener(R.id.btn_pt, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(activity, "点击测试按钮", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowUtils.show(PopupWindowUtils.TYPE_CENTER);
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowUtils.show(PopupWindowUtils.TYPE_LEFT);
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowUtils.show(PopupWindowUtils.TYPE_RIGHT);
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowUtils.show(PopupWindowUtils.TYPE_TOP);
            }
        });

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowUtils.show(PopupWindowUtils.TYPE_BOTTOM);
            }
        });

        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowUtils.show(findViewById(R.id.button6), PopupWindowUtils.TYPE_VIEW_RIGHT);
            }
        });

        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowUtils.show(findViewById(R.id.button7), PopupWindowUtils.TYPE_VIEW_LEFT);
            }
        });

        findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowUtils.show(findViewById(R.id.button8), PopupWindowUtils.TYPE_VIEW_BOTTOM);
            }
        });

        findViewById(R.id.button9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowUtils.show(findViewById(R.id.button9), PopupWindowUtils.TYPE_VIEW_TOP);
            }
        });
    }
}
