package com.example.e_commerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.e_commerce.Database.MyDatabase;
import com.example.e_commerce.Model.CategoryModel;
import com.example.e_commerce.Model.ProductModel;
import com.example.e_commerce.R;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UploadProduct extends AppCompatActivity {

    ImageView productimage;
    EditText productname, productprice, productquantity,idforupdateordalete;
    Spinner proCategory;
    ArrayAdapter adapter;
    Button upload_btn,updateproduct,daleteproduct,Generate;
    TextView reset_btn,addCategory;
    MyDatabase database;
    String str1;
    final static int GALLERY_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);
        getSupportActionBar().setTitle("upload product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intiView();

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productimage.setImageResource(R.drawable.proimg);
                productname.setText("");
                productprice.setText("");
                productquantity.setText("");
            }
        });

        SharedPreferences preferences=getSharedPreferences("addCategory1",MODE_PRIVATE);
        str1=preferences.getString("add1","show");
        if(str1.equals("hiddin2")){
            addCategory.setText("");
        }
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!str1.equals("hiddin2")){
                    addCategory();
                }
                SharedPreferences preferences=getSharedPreferences("addCategory1",MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("add1","hiddin2");
                editor.apply();
                addCategory.setText("");
            }
        });

        getAllcategory();


        productimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProduct();
            }
        });
        updateproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProduct();
            }
        });
        daleteproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteproduct();
            }
        });
        Generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(idforupdateordalete.getText().toString().replace(" ", "").trim().equals(""))
                Toast.makeText(getApplicationContext(),"Enter id",Toast.LENGTH_SHORT).show();
            else{
                Cursor c= database.getProductbyid(idforupdateordalete.getText().toString());
                productname.setText(c.getString(1));
                productprice.setText(c.getFloat(3)+"");
                productquantity.setText(c.getInt(4)+"");
                InputStream is = new ByteArrayInputStream(c.getBlob(2));
                Bitmap bmp = BitmapFactory.decodeStream(is);
                productimage.setImageBitmap(bmp);
            }
            }
        });
    }

    protected void addCategory(){
        database.insertCategory(new CategoryModel("electronics"),0);
        database.insertCategory(new CategoryModel("fashion"),0);
        database.insertCategory(new CategoryModel("cars"),0);
        database.insertCategory(new CategoryModel("sport"),0);
        database.insertCost(0);
    }

    protected void intiView() {
        productimage =(ImageView) findViewById(R.id.product_image);
        productname =(EditText) findViewById(R.id.product_name);
        productprice =(EditText) findViewById(R.id.product_price);
        productquantity =(EditText) findViewById(R.id.product_quantity);
        proCategory =(Spinner) findViewById(R.id.category);
        upload_btn =(Button) findViewById(R.id.btn_upload);
        updateproduct=(Button)findViewById(R.id.UpdateProduct);
        daleteproduct=(Button)findViewById(R.id.DeleteProduct);
        idforupdateordalete=(EditText) findViewById(R.id.id_for_update_del);
        reset_btn =(TextView) findViewById(R.id.reset);
        addCategory=(TextView) findViewById(R.id.addCategory);
        Generate=(Button)findViewById(R.id.generate);
        database = new MyDatabase(this);
    }

    protected void chooseImage() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.adminmenu1,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.feedback:
                Intent i1 = new Intent(UploadProduct.this, ShowRating.class);
                startActivity(i1);
                return true;
            case R.id.report:
                Intent i2 = new Intent(UploadProduct.this, ReportGenerated.class);
                startActivity(i2);
                return true;
            case R.id.chart:
                Intent i3 = new Intent(UploadProduct.this, ChartGenerated.class);
                startActivity(i3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        //onBackPressed();
        Intent intent=new Intent(UploadProduct.this,Choise.class);
        startActivity(intent);
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                productimage.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    protected static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    protected void getAllcategory(){

        List<String>cate=new ArrayList<>();
        Cursor cursor=database.getCategory();
        if (cursor!=null){
            while (!cursor.isAfterLast()){
                cate.add(cursor.getString(1));
                cursor.moveToNext();
            }
            adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,cate);
            proCategory.setAdapter(adapter);
        }
    }

    protected void uploadProduct(){

        String name=productname.getText().toString().replace(" ", "").trim();
        String price=productprice.getText().toString().replace(" ", "").trim();
        String quan=productquantity.getText().toString().replace(" ", "").trim();
        int catid=Integer.parseInt(database.getCatId(proCategory.getSelectedItem().toString()));
        byte [] image=imageViewToByte(productimage);

        if(!name.equals("")||!price.equals("")||!quan.equals(""))
        {
            ProductModel productModel = new ProductModel(getApplicationContext(),Integer.parseInt(quan), catid,name,image,Double.parseDouble(price));
            String sss= database.insertProduct(productModel);

            Toast.makeText(this, sss, Toast.LENGTH_SHORT).show();
            productimage.setImageResource(R.drawable.proimg);
            productname.setText("");
            productprice.setText("");
            productquantity.setText("");


            Toast.makeText(this, "product added", Toast.LENGTH_SHORT).show();
        }
        else {
             Toast.makeText(this, "Check data again", Toast.LENGTH_SHORT).show();
        }
    }

    protected void updateProduct(){

        String name=productname.getText().toString().replace(" ", "").trim();
        String price=productprice.getText().toString().replace(" ", "").trim();
        String quan=productquantity.getText().toString().replace(" ", "").trim();

        int catid=Integer.parseInt(database.getCatId(proCategory.getSelectedItem().toString()));
        byte [] image=imageViewToByte(productimage);

        if(!name.equals("")||!price.equals("")||!quan.equals(""))
        {
            ProductModel productModel = new ProductModel(getApplicationContext(),Integer.parseInt(quan), catid,name,image,Double.parseDouble(price));
            database.updateProduct(productModel,idforupdateordalete.getText().toString());



            Toast.makeText(this, "product updated", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Check data again", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteproduct(){
        String se= database.daleteProduct(idforupdateordalete.getText().toString());
        Toast.makeText(this, se, Toast.LENGTH_SHORT).show();
    }
}
