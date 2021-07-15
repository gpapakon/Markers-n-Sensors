package com.example.secondmap;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.versionedparcelable.VersionedParcelable;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;

import java.lang.annotation.Documented;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, VersionedParcelable {

    private GoogleMap mMap;
    DocumentReference documentReference;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = FirebaseFirestore.getInstance();//Pairnoume to instance tis vashs

        for (int i = 0; i < 5; i++) {
            documentReference = db.
                    collection("Markers").//vriskoume to katallhlo collection
                    document("" + i);//vriskoume to katallhlo document

            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String color = documentSnapshot.getString("color");
                        String lat = documentSnapshot.getString("lat");
                        String lng = documentSnapshot.getString("lng");
                        String comments = documentSnapshot.getString("comments");
                        String humidity = documentSnapshot.getString("humidity");
                        fillMap(color, lat, lng, comments, humidity);//apothikeuoume se metavlites tis times pou phrame apo thn vash kai kaloume thn fill map
                    } else {
                        Toast.makeText(getApplicationContext(), "Document doesnt exist", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Document doesnt exist", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.983810, 23.727539)));//topothetoume sthn ellada sto kentro tou xarth

    }


    protected void fillMap(String color, String lat, String lng, String comments, String humidity) {
        LatLng point = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));//dhmiourgoume ena antikeimeno
        MarkerOptions markerOptions = new MarkerOptions().position(point);

        if (color.equals("Red")) {
        } else if (color.equals("Blue")) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        } else if (color.equals("Green")) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else if (color.equals("Yellow")) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        } else {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        }
        mMap.addMarker(markerOptions.title("Humidity: " + humidity + " Comments: " + comments));//Prothetoume to marker ston xarti

    }

}
