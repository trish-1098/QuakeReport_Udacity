package com.android_new.trish.quakereport;

/**
 * Created by trish on 2/24/2018.
 */

public class QuakeData {
    private float magOfQuake;
    //private int magCircleOfQuake;

    private String placeOfQuake;
    private long timeInMilliseconds;
    private String quakeURL;

    public String getQuakeURL() {
        return quakeURL;
    }

    public QuakeData(float magOfQuake, String placeOfQuake, long timeInMilliseconds, String quakeURL)
    {
        this.magOfQuake = magOfQuake;
        this.placeOfQuake = placeOfQuake;
        this.timeInMilliseconds = timeInMilliseconds;
        this.quakeURL = quakeURL;

        //this.magCircleOfQuake = magCircleOfQuake;
    }

    public float getMagOfQuake() {

        return magOfQuake;
    }

    public String getPlaceOfQuake() {
        return placeOfQuake;
    }

    public long getDateofQuake() {
        return timeInMilliseconds;
    }

    /*public int getMagCircleOfQuake() {
        return magCircleOfQuake;
    }*/
}
