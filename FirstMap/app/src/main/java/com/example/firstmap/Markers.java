package com.example.firstmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
//H klash Markers
public class Markers {
    //Ta private paidia tis
    private String lng;
    private String lat;
    private String color;
    private String humidity;
    private String comments;

    //Kenos domiths
    public Markers() { }

    //Getters kai Setters gia na exoume prosvash sta paidia ths
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
