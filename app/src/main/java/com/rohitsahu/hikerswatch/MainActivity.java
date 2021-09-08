package com.rohitsahu.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    TextView lngtextview;
    TextView lattextview;
    TextView acctextview;
    TextView altextview;
    TextView addtextview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lngtextview = findViewById(R.id.lngTextView);
        lattextview = findViewById(R.id.latTextView);
        acctextview = findViewById(R.id.accTextView);
        altextview = findViewById(R.id.altTextView);
        addtextview = findViewById(R.id.addTextView);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);

                Log.i("Location", location.toString());
            }

            public void updateLocationInfo(Location location){
                lngtextview.setText(String.format(Locale.getDefault(), "Longitude: %.4f", location.getLongitude()));
                lattextview.setText(String.format(Locale.getDefault(), "Latitude: %.4f", location.getLatitude()));
                acctextview.setText(String.format(Locale.getDefault(), "Accuracy: %.4f", location.getAccuracy()));
                altextview.setText(String.format(Locale.getDefault(), "Altitude: %.4f", location.getAltitude()));

                String address = "Could not find address :(";

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try{
                    List<Address> listAddress =  geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if(listAddress != null && listAddress.size() > 0){
                        address = "Address: \n";

                        if(listAddress.get(0).getSubLocality() != null){
                            address += listAddress.get(0).getSubLocality() + "\n";
                        }

                        if(listAddress.get(0).getLocality() != null){
                            address += listAddress.get(0).getLocality() + "\n";
                        }

                        if(listAddress.get(0).getSubThoroughfare() != null){
                            address += listAddress.get(0).getSubThoroughfare() + "\n";
                        }

                        if(listAddress.get(0).getThoroughfare() != null){
                            address += listAddress.get(0).getThoroughfare() + "\n";
                        }

                        if(listAddress.get(0).getSubAdminArea() != null){
                            address += listAddress.get(0).getSubAdminArea() + "\n";
                        }

                        if(listAddress.get(0).getAdminArea() != null){
                            address += listAddress.get(0).getAdminArea() + "\n";
                        }

                        if(listAddress.get(0).getPostalCode() != null){
                            address += listAddress.get(0).getPostalCode() ;
                        }


                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                addtextview.setText(address);
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
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnowLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnowLocation != null){
                updateLocationInfo(lastKnowLocation);
            }

        }
    }

    public void updateLocationInfo(Location location){
        Log.i("Location", location.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startListening();
        }
    }

    public void startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }


}