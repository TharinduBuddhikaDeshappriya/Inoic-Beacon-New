package com.sobag.beaconplayground;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * Created by bdesh on 7/25/2017.
 */

public class WelcomeActivity extends Activity {
    Button button;
    Button button1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_welcome);

        // Locate the button in activity_main.xml
        button = (Button) findViewById(R.id.ButtonLogin);
        button1 = (Button) findViewById(R.id.ButtonRegister);

        // Capture button clicks
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Start NewActivity.class
                Intent myIntent = new Intent(WelcomeActivity.this,
                        LoginActivity.class);
                startActivity(myIntent);
            }
        });

        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Start NewActivity.class
                Intent myIntent1 = new Intent(WelcomeActivity.this,
                        RegisterActivity.class);
                startActivity(myIntent1);
            }
        });
    }
}