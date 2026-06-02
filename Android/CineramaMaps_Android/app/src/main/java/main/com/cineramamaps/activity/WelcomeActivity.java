package main.com.cineramamaps.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import main.com.cineramamaps.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        TextView loginbut = findViewById(R.id.loginbut);
        loginbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(WelcomeActivity.this,LoginAct.class);
                startActivity(ii);
            }
        });
        TextView signupbut = findViewById(R.id.signupbut);
        signupbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(WelcomeActivity.this,SignupActivity.class);
                startActivity(ii);
            }
        });
    }
}