package com.example.firstmap;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;

//ALLAGES STO GRANDLE
//classpath 'com.google.gms:google-services:4.3.3'
//apply plugin: 'com.google.gms.google-services'
//implementation 'com.google.firebase:firebase-firestore:21.4.2'

public  class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback, Serializable {

    private GoogleMap mMap; //Dhlwnoume ena antikeimeno tupou GoogleMap
    private  static FirebaseFirestore db; //Dhlwnoume ena antikeimeno tupou FirebaseFirestore

    ArrayList<LatLng> allPoints = new ArrayList<LatLng>(); //Dhmiourgoume arraylist gia na apothikeusoume to lat kai lng tou kathe marker
    ArrayList<String> colorList = new ArrayList<String>(); //Dhmiourgoume arraylist gia na apothikeusoume to xrwma kathe marker
    ArrayList<String> humidityList = new ArrayList<String>(); //Dhmiourgoume arraylist gia na apothikeusoume to humidity kathe marker
    ArrayList<String> commentsList = new ArrayList<String>(); //Dhmiourgoume arraylist gia na apothikeusoume ta sxolia apo kathe marker


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);// Sungxronizoume ton xarti
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap; //Dinoume timh sto antikeimeno me skopo na elegxoume ton xarti
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(	37.983810,23.727539))); //Kouname thn kamera me skopo na kentraroume ton xarti sthn ellada

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {//Orizoume enan listener otan ginete click panw ston xarti
            @Override
            public void onMapClick(LatLng point) {
                final Context context = getApplicationContext(); //pairnoume to contex
                //Oso o xrhsths den exei kanei panw apo 5 click ston xarti kanoume intent me skopo thn enallagi sto OptionActivity
                if(allPoints.size() < 5){
                    allPoints.add(point);//prosthetoume sthn lista to shmeio pou ekane click o xrhsths
                    Intent i = new Intent(getApplicationContext(),OptionActivity.class);//orizoume to intent me skopo na metaferthoume sto epomeno activity
                    startActivityForResult(i,1);//orizoume pws to kainourgio activity pou tha kalestei tha exei skopo na epistrepsei apotelesma
                    //auto to kanoume gia na mporesoume na metaferoume pisw sthn main activity ths epiloges tous xrhsth
                }else{
                    db = FirebaseFirestore.getInstance();//arxikopoioume to antikeimeno db wste na paroume ena instane apo thn vash mas
                    //gia kathe ena apo ta 5 shmeia pou epele3e o xrhsths tha dhmiourgisoume ena antikeimeno tupou Marker wste na to kataxwrisoume sthn vash
                    for(int i=0;i< allPoints.size();i++){
                        Markers marker = new Markers();
                        marker.setColor(colorList.get(i));
                        marker.setLat(allPoints.get(i).latitude+"");
                        marker.setLng(allPoints.get(i).longitude+"");
                        marker.setHumidity(humidityList.get(i));
                        marker.setComments(commentsList.get(i));

                        //kataxoroume to kathe antikeimeno sto collection "Markers" to kathena 3exwrista se diaforetiko document opou o arithmos tou au3anete seiriaka
                        //kai elenxoume me analogo Toast an kataxorithikan oi markers h oxi sthn vash
                        db.collection("Markers").
                                document(""+i).
                                set(marker).
                                addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context,"You are ready to go",Toast.LENGTH_SHORT).show();
                                    }
                                })

                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Marker not added",Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //Auth h methodos prosthetei tous markers me to antistoixo xrwma pou epele3e o xrhsths sto katallhlo shmeio
    protected void fillMap(){

        for(int i=0; i < allPoints.size();i++){//gia kathe point pou uparxei sto arraylist kanoume elegxo wste o marker na exei to swsto xrwma
            MarkerOptions markerOptions = new MarkerOptions().position(allPoints.get(i));//Dinoume thn thesh pou tha prostethei o marker
            if(colorList.get(i).equals("Red")){//analoga thn epilogh tou xrhsth dinoume kai antistoixo xrwma stis idiotites tou marker
            }else if(colorList.get(i).equals("Blue")){
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }else if(colorList.get(i).equals("Green")){
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }else if(colorList.get(i).equals("Yellow")){
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            }else{
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            }
            mMap.addMarker(markerOptions);//prosthetoume ton marker ston xarti
        }

    }

    //Auth h methodos kaleite otan o xrhsths pathsei to koumpi apo thn OptionActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            //Elenxoume to requestCode pou mas epistrafike apo to OptionActivity
            if(requestCode == 1){
                colorList.add(data.getStringExtra("color"));//Topothetoume to kathe xaraktiristiko pou epele3e o xrhsths sthn analogh lista
                humidityList.add(data.getStringExtra("humidity"));
                commentsList.add(data.getStringExtra("comments"));
                fillMap();//kaloume thn methodo fill map gia na gemisei ton xarth me markers
            }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
