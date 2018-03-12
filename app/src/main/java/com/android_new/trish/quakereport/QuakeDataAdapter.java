package com.android_new.trish.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by trish on 2/24/2018.
 */

public class QuakeDataAdapter extends ArrayAdapter<QuakeData>{

    private static final String LOCATION_SEPERATOR = " of ";
    public QuakeDataAdapter(@NonNull Context context, @NonNull ArrayList<QuakeData> qdata) {
        super(context, 0, qdata);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView;
        listItemView = convertView;
        if(listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.quake_list_item_view,parent,false);
        }
        final QuakeData quakeListObject = getItem(position);
        //Convert the magnitude to one decimal and display it
        float magQuake = quakeListObject.getMagOfQuake();
        String magQuakeString = convertToSingleDecimal(magQuake);
        TextView magView = (TextView) listItemView.findViewById(R.id.mag_View);
        magView.setText(magQuakeString);
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(quakeListObject.getMagOfQuake());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);
        //Splitting the strings into two parts
        String mainPlace = quakeListObject.getPlaceOfQuake();
        String placeOffset,placePrimary;
        if(mainPlace.contains(LOCATION_SEPERATOR)) {
            String placeTwoParts[] = mainPlace.split(LOCATION_SEPERATOR);
            placeOffset = placeTwoParts[0] + " of ";
            placePrimary = placeTwoParts[1];
        }
        else
        {
            placeOffset = getContext().getString(R.string.near);
            placePrimary = mainPlace;
        }
        //Displaying the two Strings
        TextView placeOffsetView = (TextView) listItemView.findViewById(R.id.offset_View);
        placeOffsetView.setText(placeOffset);
        TextView actualPlaceView = (TextView) listItemView.findViewById(R.id.actual_location_View);
        actualPlaceView.setText(placePrimary);
        //Displaying the date and time in two seperate TextViews
        TextView dateView = (TextView) listItemView.findViewById(R.id.date_View);
        dateView.setText(convertToDate(quakeListObject.getDateofQuake()));
        TextView timeView = (TextView) listItemView.findViewById(R.id.time_View);
        timeView.setText(convertToTime(quakeListObject.getDateofQuake()));

        return listItemView;
    }
    public String convertToDate(long time)
    {
        Date dateObject = new Date(time);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd,yyyy");
        return dateFormatter.format(dateObject);
    }
    public String convertToTime(long time)
    {
        Date dateObject = new Date(time);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("h:mm a");
        return dateFormatter.format(dateObject);
    }
    public String convertToSingleDecimal(float mag)
    {
        DecimalFormat decimal = new DecimalFormat("0.0");
        return decimal.format(mag);
    }
    public int getMagnitudeColor(float magReference)
    {
        int actualMagColor;
        int magColor = (int) Math.floor(magReference);
        switch(magColor)
        {
            case 0:
            case 1:
            actualMagColor = R.color.magnitude1;
            break;
            case 2:
                actualMagColor = R.color.magnitude2;
                break;
            case 3:
                actualMagColor = R.color.magnitude3;
                break;
            case 4:
                actualMagColor = R.color.magnitude4;
                break;
            case 5:
                actualMagColor = R.color.magnitude5;
                break;
            case 6:
                actualMagColor = R.color.magnitude6;
                break;
            case 7:
                actualMagColor = R.color.magnitude7;
                break;
            case 8:
                actualMagColor = R.color.magnitude8;
                break;
            case 9:
                actualMagColor = R.color.magnitude9;
                break;
            default:
                actualMagColor = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(),actualMagColor);
    }
}
