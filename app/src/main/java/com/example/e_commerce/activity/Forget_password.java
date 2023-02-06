package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.e_commerce.Database.MyDatabase;
import com.example.e_commerce.Model.CustomerModel;
import com.example.e_commerce.R;
import java.util.ArrayList;

public class Forget_password extends AppCompatActivity {
    Button check;
    MyDatabase obj1;
    EditText usernameinforget,emailinforget;
    TextView showpassword;
    boolean flag=false;
    String val,val2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().setTitle("Forget password");
        obj1=new MyDatabase(this);
        check=(Button)findViewById(R.id.checkinforget_password);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<CustomerModel> arrayList=obj1.Get_Data();
                usernameinforget=(EditText)findViewById(R.id.usernameinforget_password);
                emailinforget=(EditText)findViewById(R.id.eamilinforget_password);
                showpassword=(TextView)findViewById(R.id.showpassword);
                for(int i=0;i<arrayList.size();i++){
                    if(arrayList.get(i).getUsername().equals(usernameinforget.getText().toString().replace(" ", "").trim()) && arrayList.get(i).getEmail().equals(emailinforget.getText().toString().replace(" ", "").trim())){
                        showpassword.setText(arrayList.get(i).getPassword().replace(" ", "").trim());
                        val=arrayList.get(i).getUsername().replace(" ", "").trim();
                        val2=arrayList.get(i).getPassword().replace(" ", "").trim();
                        flag=true;
                        break;
                    }
                }
                if(flag) {
                    Intent i=new Intent(Forget_password.this,Login.class);
                    i.putExtra("key",val);
                    i.putExtra("key2",val2);
                    startActivity(i);
                }
            }
        });
    }
}