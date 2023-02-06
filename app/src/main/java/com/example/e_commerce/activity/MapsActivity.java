package com.example.e_commerce.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.e_commerce.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    LocationManager locationManager;
    myLocationListener locationListener;
    Button getLocation;
    EditText addressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        addressText=(EditText)findViewById(R.id.editTextTextPersonName);
        getLocation=(Button)findViewById(R.id.mapbtn);
        locationListener=new myLocationListener(getApplicationContext());
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,6000,0,locationListener);
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(),"you are not allowed to access the current location 1",Toast.LENGTH_LONG).show();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap=googleMap;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30,31),8));
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                Geocoder coder=new Geocoder(getApplicationContext());
                List<Address> addressList;
                Location loc=null;
                try {
                    loc=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
                catch (SecurityException e) {
                    Toast.makeText(getApplicationContext(),"you are not allowed to access the current location 2",Toast.LENGTH_LONG).show();
                }
                if (loc!=null){
                    LatLng myPosition=new LatLng(loc.getLatitude(),loc.getLongitude());
                    try {
                        addressList=coder.getFromLocation(myPosition.latitude,myPosition.longitude,1);
                        if(!addressList.isEmpty()){
                            String address="";
                            for (int i=0;i<=addressList.get(0).getMaxAddressLineIndex();i++)
                                address += addressList.get(0).getAddressLine(i)+", ";
                            mMap.addMarker(new MarkerOptions().position(myPosition).title("My Location").snippet(address)).setDraggable(true);
                            addressText.setText(address);
                        }
                    }catch (IOException e){
                        mMap.addMarker(new MarkerOptions().position(myPosition).title("My Location"));
                    }
                }else
                    Toast.makeText(getApplicationContext(),"please wait untill your position is determined",Toast.LENGTH_LONG).show();
            }
        });
    }
}