package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.Database.MyDatabase;
import com.example.e_commerce.Model.CustomerModel;
import com.example.e_commerce.R;
import java.util.ArrayList;

public class Login extends AppCompatActivity {
    MyDatabase obj;
    EditText username,password;
    TextView error;
    boolean flag=true;
    Button login,forget_password;
    String value=null,value2=null;
    CheckBox rememberme;
    //public static String u_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");

        obj=new MyDatabase(this);
        login=(Button)findViewById(R.id.Login);
        forget_password=(Button)findViewById(R.id.forget_password);

        rememberme=(CheckBox)findViewById(R.id.rememberme);

        SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
        String str=preferences.getString("rememberme","");
        if(str.equals("true")){
            Intent intent=new Intent(Login.this,MainActivity.class);
            startActivity(intent);
        }
        else if(str.equals("false")){
            //Toast.makeText(Login.this,"please sign in.",Toast.LENGTH_SHORT).show();
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            ArrayList<CustomerModel> arrayList=obj.Get_Data();
            //Toast.makeText(Login.this,arrayList.size()+" ",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(Login.this,MainActivity.class);
            username =(EditText)findViewById(R.id.usernameinlogin);
            password=(EditText)findViewById(R.id.passwordinlogin);
            error=(TextView)findViewById(R.id.error);
            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    error.setText("");
                }
            });
            password.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    error.setText("");
                }
            });
            if(username.getText().toString().equals("")){
                error.setText("Enter User Name");
            }
            else {
                for(int i=0;i<arrayList.size();i++){
                    flag=true;
                    if(arrayList.get(i).getUsername().equals(username.getText().toString().replace(" ", "").trim()) ){
                        flag=false;
                        if(arrayList.get(i).getPassword().equals(password.getText().toString().replace(" ", "").trim())){
                            intent.putExtra("username",username.getText().toString().replace(" ", "").trim());
                            //u_name=username.getText().toString();
                            SharedPreferences prefer1=getSharedPreferences("Cname",MODE_PRIVATE);
                            SharedPreferences.Editor editor=prefer1.edit();
                            editor.putString("CNAME",username.getText().toString().replace(" ", "").trim());
                            editor.apply();
                            intent.putExtra("password",password.getText().toString().replace(" ", "").trim());
                            setResult(Activity.RESULT_OK,intent);
                            startActivity(intent);
                            finish();
                            break;
                        }
                        else{
                            error.setText("wrong password");
                            break;
                        }
                    }
                }
                if(flag){
                    error.setText("user not exist");
                    flag=false;
                }
            }
            }
        });
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,Forget_password.class);
                startActivity(intent);
            }
        });



        rememberme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(compoundButton.isChecked()){
                SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("rememberme","true");
                editor.apply();
                //Toast.makeText(Login.this,"Checked",Toast.LENGTH_SHORT).show();
                }
            else if(!compoundButton.isChecked()){
                SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("rememberme","false");
                editor.apply();
                //Toast.makeText(Login.this,"UnChecked",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        value = getIntent().getStringExtra("key");
        value2 = getIntent().getStringExtra("key2");
        if (value != null && value2 != null) {
            username =(EditText)findViewById(R.id.usernameinlogin);
            username.setText(value);
            password=(EditText)findViewById(R.id.passwordinlogin);
            password.setText(value2);
        }
    }
}
