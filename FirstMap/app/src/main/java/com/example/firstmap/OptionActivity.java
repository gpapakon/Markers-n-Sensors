package com.example.firstmap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.JsonUtils;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;



public class OptionActivity extends AppCompatActivity implements  SensorEventListener{
    //Dhlwnoume ta antikeimena gia na metaxeirizomaste ta sustatika tou activity_option.xml
    private Button button;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextView humidityText;
    private SensorManager DeviseSensorManager;
    private TextView commentsText;
    private Sensor humiditySensor;

    //Dhlwnoume 2 metaulites
    private String humidity;
    private String comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        //Pairnoume to textView me id="HumidityID"
        humidityText = findViewById(R.id.HumidityID);
        //Arxikopoioume ton sensora
        DeviseSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //Arxikopoioume ta antikeimena to prwto me to ButtonGroup kai to deutero me to GobackBt xrhshmopoiontas ta ID tous
        radioGroup = findViewById(R.id.ButtonGroup);
        button = findViewById(R.id.GobackBt);

        //Pairnoume to textView me ID="CaptainId"
        commentsText = findViewById(R.id.CaptionId);

        //Thetoume onclickListener sto koumpi
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pairnoume to epelegmeno koumpi ap oto RadioButton kai sthn sunexeia pairnoume to Text
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                String color = (String) radioButton.getText();//Apothikeuoume to Text sthn metavlhth color

                //Dhmiourgoume ena intet gia na metaferthoume pisw sto arxiko activity kai tou prosthetoume to xrwma,to humidity kai ta sxolia tou xrhsth
                Intent returnIntent = new Intent(getApplicationContext(),MapsActivity.class);
                returnIntent.putExtra("color",color);
                returnIntent.putExtra("humidity", humidity);

                comments = commentsText.getText().toString();
                returnIntent.putExtra("comments",comments);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }

    @Override
    protected  void onPause(){
        super.onPause();
        //Otan to activity vriskete se onPause katastash kanoume unregister ton sensora
        //auto ginete kurios gia e3oikonomish porwn kai mpatarias sta kinhta thlefwna
        DeviseSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        //Otan to activity vriskete se onResume katastash  tote kanoume register ton Listener ston sensora
        humiditySensor = DeviseSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if(humiditySensor != null){
            DeviseSensorManager.registerListener(this,humiditySensor,SensorManager.SENSOR_DELAY_UI);
        }else{
            Toast.makeText(this,"NO sensor",Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Otan allazei h timh tou sensora tote  allazoume to text tou antistoixou TextView kathos katame thn timh se mia metavlhth
        humidityText.setText("Humidity: "+ event.values[0]);
        humidity = String.valueOf(event.values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
