package com.cli.knowledgebase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ActivityBeforeLoging extends Activity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_login);



        TextView tv = (TextView)findViewById(R.id.btn_login);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityBeforeLoging.this, MainActivity.class);
                startActivity(i);

                // close this activity
//                finish();
            }
        });
    }
}
