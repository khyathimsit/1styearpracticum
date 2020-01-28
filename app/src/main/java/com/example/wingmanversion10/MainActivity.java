package com.example.wingmanversion10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    private Button register_btn,login_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Buttons displayed on home page
        register_btn = (Button) findViewById(R.id.main_join_now_btn);
        login_btn = (Button) findViewById(R.id.main_login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent1);
            }
        });
    }
}
