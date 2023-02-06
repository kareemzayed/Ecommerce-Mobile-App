package com.example.e_commerce.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.Adapter.CartAdapter;
import com.example.e_commerce.Database.MyDatabase;
import com.example.e_commerce.Model.ProductModel;
import com.example.e_commerce.R;
import com.example.e_commerce.activity.CustomDialogClass;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cart extends Fragment implements LocationListener {

    private ListView cart_products;
    private CartAdapter adapter;
    private ArrayList<ProductModel> data = new ArrayList<>();
    private EditText add_address;
    private MyDatabase database;
    private SharedPreferences sharedPreferences;
    private  final  static int REQUEST_CODE=100;
    String addr=null;
    FusedLocationProviderClient fusedLocationProviderClient;



    TextView orignal_price,delivery_cost,total_cost;
    Button apply_address,confirm_order;

    double cost=0;

    int PERMISSION_ID = 44;
    String value=null;
    public Cart() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        //viewproduct=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1);
        cart_products = view.findViewById(R.id.cart_product);
        database = new MyDatabase(getContext());
        orignal_price=view.findViewById(R.id.order_price);
        delivery_cost=view.findViewById(R.id.delivery_cost);
        total_cost=view.findViewById(R.id.total_cost);
        apply_address=view.findViewById(R.id.apply_address);
        confirm_order=view.findViewById(R.id.confirm_order);
        add_address=view.findViewById(R.id.add_address);

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getContext());


        apply_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //Toast.makeText(getContext(),"address",Toast.LENGTH_SHORT).show();
            //Intent google_map=new Intent(Intent.ACTION_VIEW);
            //google_map.setData(Uri.parse("geo:30.1123143,31.3350959?z=15"));
            //startActivity(google_map);
            //Intent intent=new Intent(getContext(), UserLocation.class);
            //startActivity(intent);
            getLastLocation();
            if(addr!=null){
                Toast.makeText(getContext(), addr, Toast.LENGTH_SHORT).show();
                add_address.setText(addr);
            }
            }
        });
        confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(orignal_price.getText().toString().equalsIgnoreCase("0.0"+" $") || orignal_price.getText().toString().equalsIgnoreCase("000"))
                Toast.makeText(getContext(),"not confirm",Toast.LENGTH_SHORT).show();
            else{
                Toast.makeText(getContext(),"confirm",Toast.LENGTH_SHORT).show();
                CustomDialogClass cdd=new CustomDialogClass(getActivity());
                cdd.show();
            }
            }
        });

        getProductsids();

        return view;
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                if (location !=null){
                    Geocoder geocoder=new Geocoder(getContext(), Locale.getDefault());
                    List<Address> addresses= null;
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        addr=addresses.get(0).getAddressLine(0)+", "+addresses.get(0).getLocality()+", "+addresses.get(0).getCountryName();
                    }
                    catch (IOException e) {
                        Toast.makeText(getContext(), "IOException", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                }
            });
        }else {
            askPermission();
        }
    }
    private void askPermission() {
        ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
    }


    private void getProductsids() {
        sharedPreferences = this.getActivity().getSharedPreferences("cart", Context.MODE_PRIVATE);
        String ids = sharedPreferences.getString("lastorder", null);
        if (ids != null) {
            Gson gson = new Gson();
            ArrayList id = gson.fromJson(ids, ArrayList.class);
            getCartProduct(id);


            adapter = new CartAdapter(getContext(), data);
            adapter.setTotal_cost(cost);
            cart_products.setAdapter(adapter);


            orignal_price.setText( database.getCost(1).getFloat(0)+" $");
            delivery_cost.setText("20.0 $");
            float currentorignal_price= Float.parseFloat(orignal_price.getText().toString().substring(0, orignal_price.getText().toString().length() - 2));
            float currentdelivery_cost= Float.parseFloat(delivery_cost.getText().toString().substring(0, delivery_cost.getText().toString().length() - 2));
            total_cost.setText( currentorignal_price+currentdelivery_cost + " $");
        }


    }

    private void getCartProduct(ArrayList<Integer> ids) {

        data.clear();
        for (int i = 0; i < ids.size(); i++) {
            Cursor cursor = database.getProductbyId(String.valueOf(ids.get(i)));
            if (cursor != null) {
                ProductModel productModel = new ProductModel(getContext(),Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5)),
                        cursor.getString(1), cursor.getBlob(2),
                        Double.parseDouble(cursor.getString(3)));
                productModel.setPro_id(Integer.parseInt(cursor.getString(0)));
                data.add(productModel);
                cost+=Double.parseDouble(cursor.getString(3));
            }
        }
    }


    public void getUserLocation(View view){
        //
    }
    @Override
    public void onLocationChanged(Location location) {
        //this.location=location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }





    private boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Granted. Start getting the location information
            }
        }
    }

}
