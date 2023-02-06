package com.example.e_commerce.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.example.e_commerce.Model.CategoryModel;
import com.example.e_commerce.Model.CustomerModel;
import com.example.e_commerce.Model.ProductModel;
import java.util.ArrayList;

public class MyDatabase extends SQLiteOpenHelper {

    final static String dataName = "Mydatabase7";
    SQLiteDatabase database;

    public MyDatabase(@Nullable Context context) {
        super(context, dataName, null, 3);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table customer (id INTEGER primary key AUTOINCREMENT,username TEXT,email TEXT,password TEXT,birthdate TEXT)");

        sqLiteDatabase.execSQL("create table category (id integer primary key  autoincrement , name text not null , count integer )");

        sqLiteDatabase.execSQL("create table rating (id integer primary key  autoincrement , ratenum REAL, nametxt text )");

        sqLiteDatabase.execSQL("create table product (id integer primary key autoincrement, name text not null ,image blob ," +
                "price real not null , quantity integer not null , quantitySelected integer not null , cate_id integer not null ," +
                "foreign key (cate_id)references category (id))");

        sqLiteDatabase.execSQL("create table transactions (id integer primary key  autoincrement , customername text, productname text , catgoryname text ,image blob ,date TEXT ,price real ,quantity integer) ");

        sqLiteDatabase.execSQL("create table cost (id integer primary key  autoincrement , costproducts REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists customer");
        db.execSQL("drop table if exists category");
        db.execSQL("drop table if exists product");
        db.execSQL("drop table if exists rating");
        db.execSQL("drop table if exists transactions");
        db.execSQL("drop table if exists cost");
        onCreate(db);
    }

    public String Insert_transactions(String customername, String productname, String catgoryname,byte [] image, String date, float price, int quantity){
        SQLiteDatabase s=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("customername",customername);
        values.put("productname",productname);
        values.put("catgoryname",catgoryname);
        values.put("image",image);
        values.put("date",date);
        values.put("price",price);
        values.put("quantity",quantity);
        long re=s.insert("transactions",null,values);
        s.close();
        if(re==-1)
            return "error";
        else
            return "inserted row";
    }

    public Cursor getTransactions(){
        database=getReadableDatabase();
        String[] fields={"customername","productname","catgoryname","image","date","price","quantity","id"};
        Cursor cursor= database.query("transactions",fields,null,null,null,null,null);
        if (cursor!=null)
            cursor.moveToFirst();
        database.close();
        return cursor;
    }

    public String Insert_cust(String username, String email, String password, String birthdate){
        SQLiteDatabase s=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("username",username);
        values.put("email",email);
        values.put("password",password);
        values.put("birthdate",birthdate);
        long re=s.insert("customer",null,values);
        s.close();
        if(re==-1)
            return "error";
        else
            return "inserted row";
    }

    public String Insert_rate(float ratenum, String txtrate){
        SQLiteDatabase s=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("ratenum",ratenum);
        values.put("nametxt",txtrate);
        long re=s.insert("rating",null,values);
        s.close();
        if(re==-1)
            return "error rating";
        else
            return "inserted row rating";
    }

    public Cursor get_rating(){
        SQLiteDatabase s=this.getReadableDatabase();
        String[] rateDetails={"ratenum","nametxt","id"};
        Cursor cursor=s.query("rating",rateDetails,null,null,null,null,null);
        if(cursor!=null)
            cursor.moveToFirst();
        s.close();
        return cursor;
    }

    public ArrayList<CustomerModel> Get_Data(){
        ArrayList<CustomerModel> arrayList = new ArrayList<CustomerModel>();
        SQLiteDatabase s = this.getReadableDatabase();
        Cursor c=s.rawQuery("select * from customer",null);
        c.moveToFirst();
        while (c.isAfterLast()==false){
            arrayList.add(new CustomerModel(
                c.getInt(0),
                c.getString(1),
                c.getString(2),
                c.getString(3),
                c.getString(4)
            ));
            c.moveToNext();
        }
        s.close();
        return arrayList;
    }

    public void daletecust(String id){
        SQLiteDatabase sq=this.getWritableDatabase();
        sq.delete("customer","id=?",new String[]{id});
        sq.close();
    }

    public String  insertProduct(ProductModel product){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("name",product.getProName());
        values.put("image",product.getProImage());
        values.put("price",product.getPrice());
        values.put("quantity",product.getPro_quantity());
        values.put("cate_id",product.getCatId());
        values.put("quantitySelected",0);
        long re=database.insert("product",null,values);
        database.close();
        if(re==-1)
            return "error quantitySelected";
        else
            return "inserted quantitySelected";
    }

    public String updateProductSelected(int quantitySelected,String prodName){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("quantitySelected",quantitySelected);
        long re= database.update("product",values,"name=?",new String[]{prodName});
        database.close();
        if(re==-1)
            return "error Selected";
        else
            return "updateProductSelected";
    }

    public int getProductSelected(String prodName){
        database=getReadableDatabase();
        String[] arg={prodName};
        Cursor cursor=database.rawQuery("Select quantitySelected from product where name like ?",arg);
        cursor.moveToFirst();
        database.close();
        return cursor.getInt(0);
    }

    public void updateProduct(ProductModel product,String p_id){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("name",product.getProName());
        values.put("image",product.getProImage());
        values.put("price",product.getPrice());
        values.put("quantity",product.getPro_quantity());
        values.put("cate_id",product.getCatId());
        database.update("product",values,"id=?",new String[]{p_id});
        database.close();
    }

    public String daleteProduct(String p_id){
        SQLiteDatabase sq=this.getWritableDatabase();
        long re=sq.delete("product","id=?",new String[]{p_id});
        sq.close();
        if(re==-1)
            return "error";
        else
            return "product deleted";
    }

    public Cursor getProducts(){
        database=getReadableDatabase();
        String[]fields={"id","name","image","price","quantity","cate_id"};
        Cursor cursor= database.query("product",fields,null,null,null,null,null);
        if (cursor!=null)
           cursor.moveToFirst();
        database.close();
        return cursor;
    }

    public void insertCategory(CategoryModel cate,int count){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("name",cate.getCat_name());
        values.put("count",count);
        database.insert("category",null,values);
        database.close();
    }

    public void updateCategory(int count ,int c_id){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("count",count);
        database.update("category",values,"id=?",new String[]{String.valueOf(c_id)});
        database.close();
    }

    public Cursor getQuan(int id){
        database=getReadableDatabase();
        String []args={String.valueOf(id)};
        Cursor cursor=  database.rawQuery("select count from category where id =? ",args);
        if (cursor!=null)
            cursor.moveToFirst();
        database.close();
        return cursor;
    }

    public void updateQuantity(int count ,int p_id){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("quantity",count);
        database.update("product",values,"id=?",new String[]{String.valueOf(p_id)});
        database.close();
    }

    public Cursor getQuantity(String id){
        database=getReadableDatabase();
        String []args={id};
        Cursor cursor=  database.rawQuery("select quantity from product where id =? ",args);
        if (cursor!=null)
            cursor.moveToFirst();
        database.close();
        return cursor;
    }

    public void insertCost(float cost){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("costproducts",cost);
        database.insert("cost",null,values);
        database.close();
    }

    public void updateCost(int id ,float cost){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("costproducts",cost);
        database.update("cost",values,"id=?",new String[]{String.valueOf(id)});
        database.close();
    }

    public Cursor getCost(int id){
        database=getReadableDatabase();
        String []args={String.valueOf(id)};
        Cursor cursor=  database.rawQuery("select costproducts from cost where id =? ",args);
        if (cursor!=null)
            cursor.moveToFirst();
        database.close();
        return cursor;
    }

    public Cursor getCategory(){
        database=getReadableDatabase();
        String []fields={"id","name","count"};
        Cursor cursor= database.query("category",fields,null,null,null,null,null);
        if (cursor.getCount()>0)
            cursor.moveToFirst();
        database.close();
        return cursor;
    }

    public Cursor getProductbyid(String id){
        database=getReadableDatabase();
        String []args={id};
        Cursor cursor=  database.rawQuery("select * from product where id =? ",args);
        if (cursor!=null)
            cursor.moveToFirst();
        database.close();
        return cursor;
    }

    public Cursor getProductbyCategor(String cate){
        database=getReadableDatabase();
        String []args={cate};
        Cursor cursor=  database.rawQuery("select * from product where cate_id =? ",args);
        if (cursor!=null)
            cursor.moveToFirst();
        database.close();
        return cursor;
    }

    public Cursor getProductbyId(String id){
        database=getReadableDatabase();
        String []args={id};
        Cursor cursor=  database.rawQuery("select * from product where id =? ",args);
        if (cursor!=null)
            cursor.moveToFirst();
        database.close();
        return cursor;
    }

    public String getCateName(String id){
        database=getReadableDatabase();
        String []args1={id};
        Cursor cursor1 =  database.rawQuery("select cate_id from product where id =? ",args1);
        cursor1.moveToFirst();

        String []args2={cursor1.getString(0)};
        Cursor cursor2 =  database.rawQuery("select name from category where id =? ",args2);
        cursor2.moveToFirst();
        database.close();
        return cursor2.getString(0);
    }

    public String getCatId(String name ){
        database=getReadableDatabase();
        String[]args={name};
        Cursor cursor=database.rawQuery("select id from category where name =?",args);
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            database.close();
            return cursor.getString(0);
        }
        database.close();
        cursor.close();
        return null;
    }
}
