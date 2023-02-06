package com.example.e_commerce.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import com.example.e_commerce.Database.MyDatabase;
import com.example.e_commerce.R;

public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {

    public Activity c;
    public Button sendrate, cancelrate;
    EditText txtrating;
    RatingBar ratingBar;
    MyDatabase rate;

    public CustomDialogClass(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_custom_dialog_class);
        sendrate = (Button) findViewById(R.id.ratingbtn);
        cancelrate = (Button) findViewById(R.id.cancelbtn);
        sendrate.setOnClickListener(this);
        cancelrate.setOnClickListener(this);
        txtrating=(EditText)findViewById(R.id.txtcustrating);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        rate=new MyDatabase(getContext());
    }

    @Override
    public void onClick(View view) {
        boolean f=true;
        switch (view.getId()) {
            case R.id.ratingbtn:
                float totalvalue=ratingBar.getRating();
                String totaltxt=txtrating.getText().toString();

                if(totalvalue!=0){
                    String st= rate.Insert_rate(totalvalue,totaltxt);
                    Toast.makeText(getContext(),st,Toast.LENGTH_SHORT).show();
                    f=true;
                }
                else{
                    Toast.makeText(getContext(),"choose rate",Toast.LENGTH_SHORT).show();
                    f=false;
                }
                break;
            case R.id.cancelbtn:
                dismiss();
                break;
            default:
                break;
        }
        if(f)
            dismiss();
    }
}