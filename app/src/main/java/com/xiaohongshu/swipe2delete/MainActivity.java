package com.xiaohongshu.swipe2delete;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Swipe2DeleteViewGroup mSwipe2Delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipe2Delete = (Swipe2DeleteViewGroup) findViewById(R.id.swipe2delete);
        mSwipe2Delete.setOnItemClickListener(new Swipe2DeleteViewGroup.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int index, boolean isCenterView) {
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    String str = textView.getText().toString();
                    Toast.makeText(MainActivity.this, String.format("%s , isCenterView: %s", str, isCenterView), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
