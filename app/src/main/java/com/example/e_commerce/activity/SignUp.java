package com.example.e_commerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;
import com.example.e_commerce.Database.MyDatabase;
import com.example.e_commerce.R;

public class SignUp extends AppCompatActivity {

    EditText username,email,password;
    Button signup;
    MyDatabase obj;
    CalendarView calendarView;
    int cday,cmonth,cyear;
    String birthdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setTitle("Sign up");

        calendarView=findViewById(R.id.calendarView);
        signup=(Button)findViewById(R.id.signup);

        obj=new MyDatabase(this);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                month++;
                cday=day;cmonth=month;cyear=year;
                birthdate= String.valueOf(cday)+"-"+String.valueOf(cmonth)+"-"+String.valueOf(cyear);
                Toast.makeText(SignUp.this,birthdate,Toast.LENGTH_LONG).show();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent=new Intent();
            username=(EditText)findViewById(R.id.username);
            email=(EditText)findViewById(R.id.email);
            password=(EditText)findViewById(R.id.password);
            if(username.getText().toString().replace(" ", "").trim().equals("")||email.getText().toString().replace(" ", "").trim().equals("")||password.getText().toString().replace(" ", "").trim().equals("")||birthdate.equals("")){
                Toast.makeText(SignUp.this,"An empty value is not allowed",Toast.LENGTH_SHORT).show();
            }
            else{
                String str= obj.Insert_cust(username.getText().toString().replace(" ", "").trim(),email.getText().toString().replace(" ", "").trim(),password.getText().toString().replace(" ", "").trim(),birthdate);
                Toast.makeText(SignUp.this,str,Toast.LENGTH_SHORT).show();
                intent.putExtra("username",username.getText().toString().replace(" ", "").trim());
                intent.putExtra("email",email.getText().toString().replace(" ", "").trim());
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
            }
        });
    }
}
