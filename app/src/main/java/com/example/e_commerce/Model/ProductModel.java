package com.example.e_commerce.Model;

import android.content.Context;

import com.example.e_commerce.Database.MyDatabase;

public class ProductModel {

    MyDatabase db;
    private int pro_id;
    private int pro_quantity;
    private int catId;


    private int quantitySelected;
    private String proName;
    private byte[] proImage;
    private double price;

    public byte [] getProImage() {
        return proImage;
    }

    public void setProImage(byte [] proImage) {
        this.proImage = proImage;
    }

    public ProductModel(Context context,int pro_quantity, int catId, String proName, byte [] proImage, double price) {
        db=new MyDatabase(context);
        this.pro_quantity = pro_quantity;
        this.catId = catId;
        this.proName = proName;
        this.proImage = proImage;
        this.price = price;
    }

    public int getSelectedQuantity(String prodName) {
        int i=db.getProductSelected(prodName);
        return i;
    }

    public void setQuantitySelected(int quantitySelected) {
        this.quantitySelected = quantitySelected;
    }

    public ProductModel() {
    }

    public int getPro_id() {
        return pro_id;
    }

    public void setPro_id(int pro_id) {
        this.pro_id = pro_id;
    }

    public int getPro_quantity() {
        return pro_quantity;
    }

    public void setPro_quantity(int pro_quantity) {
        this.pro_quantity = pro_quantity;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
