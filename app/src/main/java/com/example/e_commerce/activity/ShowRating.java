package com.example.e_commerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.e_commerce.Database.MyDatabase;
import com.example.e_commerce.R;

public class ShowRating extends AppCompatActivity {

    ListView rateList;
    ArrayAdapter<String> rateAdapter;
    MyDatabase rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rating);
        getSupportActionBar().setTitle("Show Rating");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rateList=(ListView)findViewById(R.id.rating);
        rateAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1);
        rateList.setAdapter(rateAdapter);
        rate=new MyDatabase(getApplicationContext());
        Cursor cursor=rate.get_rating();
        while (!cursor.isAfterLast()){
            rateAdapter.add("rating: "+cursor.getString(0) +"\n"+ cursor.getString(1));
            cursor.moveToNext();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.adminmenu2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.uploadproduct11:
                Intent i1 = new Intent(ShowRating.this, UploadProduct.class);
                startActivity(i1);
                return true;
            case R.id.report:
                Intent i2 = new Intent(ShowRating.this, ReportGenerated.class);
                startActivity(i2);
                return true;
            case R.id.chart:
                Intent i3 = new Intent(ShowRating.this, ChartGenerated.class);
                startActivity(i3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        //onBackPressed();
        Intent intent=new Intent(ShowRating.this,Choise.class);
        startActivity(intent);
        return super.onSupportNavigateUp();
    }
}