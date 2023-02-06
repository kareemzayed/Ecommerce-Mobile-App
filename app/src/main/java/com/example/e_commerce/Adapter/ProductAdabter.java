package com.example.e_commerce.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.e_commerce.Database.MyDatabase;
import com.example.e_commerce.Model.ProductModel;
import com.example.e_commerce.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;

public class ProductAdabter extends ArrayAdapter {

   private ArrayList<ProductModel>data;
   private ArrayList<ProductModel>data_copy;

   MyDatabase db;
    private LinkedHashSet<Integer> selected_items=new LinkedHashSet<>();
    SharedPreferences sharedPreferences,preferences;
    SharedPreferences.Editor editor;
    //static int electronics=0,fashion=0,cars=0,sport=0;
    String loginCustname;
    public static int quantityitem=0;

    public ProductAdabter(@NonNull Context context, int resource, @NonNull ArrayList<ProductModel> objects) {
        super(context, resource, objects);
        data=objects;
        data_copy=objects;
        sharedPreferences=getContext().getSharedPreferences("cart",Context.MODE_PRIVATE);
        db=new MyDatabase(getContext());
        getSelectedProducts();
        preferences=getContext().getSharedPreferences("Cname",Context.MODE_PRIVATE);
        loginCustname =preferences.getString("CNAME","");

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView==null)
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);

        ImageView product_image = convertView.findViewById(R.id.image_of_product);
        TextView product_name = convertView.findViewById(R.id.name_of_product);
        final TextView product_price = convertView.findViewById(R.id.price_of_product);
        ImageButton add_cart = convertView.findViewById(R.id.add_to_cart);
        ImageButton add_quan = convertView.findViewById(R.id.increase_quantity1);
        ImageButton dec_quan = convertView.findViewById(R.id.decrase_quantity1);
        final TextView item_quan=convertView.findViewById(R.id.quantity);
        TextView product_id = convertView.findViewById(R.id.viewid);

        if (data.get(position).getProImage() != null) {
            byte[] image_byte = data.get(position).getProImage();
            Bitmap bmp = BitmapFactory.decodeByteArray(image_byte, 0, image_byte.length);
            product_image.setImageBitmap(bmp);
        }

        product_name.setText(data.get(position).getProName());
        product_price.setText(data.get(position).getPrice() + " $");
        product_id.setText(String.valueOf(data.get(position).getPro_id()));

        add_quan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            int quan = Integer.parseInt(item_quan.getText().toString());
            quan++;
            item_quan.setText(String.valueOf(quan));
            }
        });

        dec_quan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            int quan = Integer.parseInt(item_quan.getText().toString());
            if (quan > 0)
                quan--;
            item_quan.setText(String.valueOf(quan));
            }
        });

        add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Cursor c= db.getQuantity(data.get(position).getPro_id()+"");
            if(Integer.parseInt(item_quan.getText().toString())>0 && c.getInt(0)- Integer.valueOf(item_quan.getText().toString())>=0){
                String str = db.getCateName(data.get(position).getPro_id()+"");
                String aaa= db.updateProductSelected(Integer.parseInt(item_quan.getText().toString()),data.get(position).getProName());
                //Toast.makeText(getContext(), aaa, Toast.LENGTH_SHORT).show();

                switch (str){
                    case "electronics":
                        int electronics=db.getQuan(1).getInt(0);
                        Toast.makeText(getContext(), "electronics: "+electronics, Toast.LENGTH_SHORT).show();
                        electronics+=Integer.valueOf(item_quan.getText().toString());
                        db.updateCategory(electronics,1);
                        Toast.makeText(getContext(), "electronics: "+electronics, Toast.LENGTH_SHORT).show();
                        break;
                    case "fashion":
                        int fashion=db.getQuan(2).getInt(0);
                        fashion+=Integer.valueOf(item_quan.getText().toString());
                        db.updateCategory(fashion,2);
                        Toast.makeText(getContext(), "fashion: "+fashion, Toast.LENGTH_SHORT).show();
                        break;
                    case "cars":
                        int cars=db.getQuan(3).getInt(0);
                        cars+=Integer.valueOf(item_quan.getText().toString());
                        db.updateCategory(cars,3);
                        Toast.makeText(getContext(), "cars: "+cars, Toast.LENGTH_SHORT).show();
                        break;
                    case "sport":
                        int sport=db.getQuan(4).getInt(0);
                        sport+=Integer.valueOf(item_quan.getText().toString());
                        db.updateCategory(sport,4);
                        Toast.makeText(getContext(), "sport: "+sport, Toast.LENGTH_SHORT).show();
                        break;
                }
                selected_items.add(data.get(position).getPro_id());
                Gson gson = new Gson();
                String json = gson.toJson(selected_items);
                editor = sharedPreferences.edit();
                editor.putString("lastorder", json);
                editor.apply();

                Toast.makeText(getContext(), "product added", Toast.LENGTH_SHORT).show();


                String uu= db.Insert_transactions(
                        loginCustname,
                        data.get(position).getProName(),
                        db.getCateName(data.get(position).getPro_id()+""),
                        data.get(position).getProImage(),
                        Calendar.getInstance().getTime().toString(),
                        (float) (Integer.parseInt(item_quan.getText().toString())*data.get(position).getPrice()),
                        Integer.parseInt(item_quan.getText().toString())
                );
                //Toast.makeText(getContext(), uu, Toast.LENGTH_SHORT).show();

                Cursor c1=db.getCost(1);
                db.updateCost(1, (float) (c1.getFloat(0)+Integer.parseInt(item_quan.getText().toString())*data.get(position).getPrice()));

                quantityitem=Integer.parseInt(item_quan.getText().toString());

                db.updateQuantity(c.getInt(0)- Integer.valueOf(item_quan.getText().toString()),data.get(position).getPro_id());

            }
            else if(c.getInt(0)- Integer.valueOf(item_quan.getText().toString()) < 0 ){
                Toast.makeText(getContext(), "item was finished", Toast.LENGTH_SHORT).show();
            }
            else if(Integer.parseInt(item_quan.getText().toString())==0){
                Toast.makeText(getContext(), "choose quantity", Toast.LENGTH_SHORT).show();
            }
            }
        });
        return convertView;
    }

    public void filter(ArrayList<ProductModel>filterlist){
        data.clear();
        data.addAll(filterlist);
        notifyDataSetChanged();
    }

    private void getSelectedProducts() {
        String ids = sharedPreferences.getString("lastorder", null);
        selected_items.clear();
        if (ids != null) {
            Gson gson = new Gson();
            selected_items = gson.fromJson(ids, LinkedHashSet.class);
        }
    }
}
