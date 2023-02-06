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

public class ReportGenerated extends AppCompatActivity {
    ListView reportList;
    ArrayAdapter<String> reportAdapter;
    MyDatabase report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_generated);
        getSupportActionBar().setTitle("Report Generated");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reportList=(ListView)findViewById(R.id.report);
        reportAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1);
        reportList.setAdapter(reportAdapter);
        report=new MyDatabase(getApplicationContext());
        Cursor cursor=report.getTransactions();
        while (!cursor.isAfterLast()){
            reportAdapter.add(
                "Customer: "+cursor.getString(0) +"\n" +
                "Product: "+cursor.getString(1)+"\n" +
                "Catgory: "+cursor.getString(2)+"\n" +
                //"Image: "+cursor.getBlob(3)+"\n" +
                "Date: "+cursor.getString(4)+"\n" +
                "Quantity: "+cursor.getInt(6)+"\n"+
                "Price: "+cursor.getFloat(5)+"\n"
            );
            cursor.moveToNext();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.adminmenu3,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.uploadproduct11:
                Intent i2 = new Intent(ReportGenerated.this, UploadProduct.class);
                startActivity(i2);
                return true;
            case R.id.feedback:
                Intent i1 = new Intent(ReportGenerated.this, ShowRating.class);
                startActivity(i1);
                return true;
            case R.id.chart:
                Intent i3 = new Intent(ReportGenerated.this, ChartGenerated.class);
                startActivity(i3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        //onBackPressed();
        Intent intent=new Intent(ReportGenerated.this,Choise.class);
        startActivity(intent);
        return super.onSupportNavigateUp();
    }
}