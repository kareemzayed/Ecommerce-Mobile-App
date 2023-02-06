package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.e_commerce.R;

public class Choise extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choise);
        Button login=(Button)findViewById(R.id.loginonchoise);
        Button signup=(Button)findViewById(R.id.signupinchoise);
        Button loginadmin=(Button)findViewById(R.id.loginasadmin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Choise.this, Login.class);
                startActivity(intent);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Choise.this,SignUp.class);
                startActivity(intent);
            }
        });
        loginadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Choise.this,AdminLogin.class);
                startActivity(intent);
            }
        });
    }
}