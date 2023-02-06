package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.e_commerce.R;

public class AdminLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        getSupportActionBar().setTitle("Login");
        final EditText useradmin=(EditText)findViewById(R.id.useradmin);
        final EditText passadmin=(EditText)findViewById(R.id.passadmin);
        Button loginadmin=(Button)findViewById(R.id.loginadmin);
        loginadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(useradmin.getText().toString().replace(" ", "").trim().equalsIgnoreCase("admin") && passadmin.getText().toString().replace(" ", "").trim().equalsIgnoreCase("admin")){
                Intent intent=new Intent(AdminLogin.this,UploadProduct.class);
                startActivity(intent);
                finish();
            }
            }
        });
    }
}