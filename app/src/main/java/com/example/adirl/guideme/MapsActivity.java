package com.example.adirl.guideme;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static com.example.adirl.guideme.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener
{

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);

        if (mMap != null) {
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }
                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.my_info_window, null);
                    TextView tvLocality = (TextView)v.findViewById(R.id.tv_locality);
                    TextView tvSnippet = (TextView)v.findViewById(R.id.tv_snippet);
                    TextView tvLat = (TextView)v.findViewById(R.id.tv_lat);
                    TextView tvLong = (TextView)v.findViewById(R.id.tv_long);

                    LatLng ll = marker.getPosition();
                    tvLocality.setText(marker.getTitle());
                    tvSnippet.setText(marker.getSnippet());
                    tvLat.setText("Latitude: " + ll.latitude);
                    tvLong.setText("Longtitude: " + ll.longitude);

                    return v;
                }
            });
        }
        mMap.setMyLocationEnabled(true);

        LatLng Sepulchre = new LatLng(31.7785, 35.2296);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Sepulchre, 15));
        Marker mSepulchre = mMap.addMarker(new MarkerOptions()
            .position(Sepulchre)
            .title("Church of the Holy Sepulchre")
            .snippet("The Church of the Holy Sepulchre is a church in the Christian Quarter of the Old City of Jerusalem")
        );
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Opennning video", Toast.LENGTH_SHORT).show();
        String url = "https://youtu.be/Oa7PvB2KADo";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void onMapType (View view)
    {
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void onSearch (View view) {
        EditText location_tf = (EditText)findViewById(R.id.TFaddress);
        String location = location_tf.getText().toString();
        List<Address> addressList = null;
        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latlan = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(latlan)
                    .title(address.getLocality()));

            mMap.animateCamera(CameraUpdateFactory.newLatLng(latlan));

            }
    }

}
