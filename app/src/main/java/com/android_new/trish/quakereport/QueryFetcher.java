package com.android_new.trish.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by trish on 2/25/2018.
 */

public final class QueryFetcher {
    private static final String LOG_TAG = QueryFetcher.class.getSimpleName();

    public static ArrayList<QuakeData> extractQuakes(String requestURL)
    {
        try
        {
            Thread.sleep(5000);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        ArrayList<QuakeData> quakeDataList = new ArrayList<>();
        URL url = createURL(requestURL);
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        }
        catch(IOException e)
        {
            Log.i("Exception: ",e+"");
            e.printStackTrace();
        }
        if(!jsonResponse.equals(""))
        {
            quakeDataList = getAllQuakeData(jsonResponse);
            Log.i("In extractQuakes()","Successfully extracted quakes");
            return quakeDataList;
        }
        else
            return null;
    }
    private static URL createURL(String reqURL)
    {
        URL url = null;
        try
        {
            url = new URL(reqURL);
        }
        catch (MalformedURLException e)
        {
            Log.e(LOG_TAG,"Exception --> ",e);
        }
        return url;
    }
    private static String makeHttpRequest(URL url) throws IOException
    {
        String jsonResponse="";
        if(url == null)
        {
            return null;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200)
            {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readInputStream(inputStream);
            }
            else
            {
                Log.i("ErrorResponse Code --> ",urlConnection.getResponseCode()+"");
            }
        }
        catch(IOException e)
        {
            Log.e(LOG_TAG,"Exception --> ",e);
            e.printStackTrace();
        }
        finally {
            if(urlConnection != null)
            {
                urlConnection.disconnect();
            }
            if(inputStream != null)
            {
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    public static String readInputStream(InputStream inputStream) throws IOException
    {
        StringBuilder jsonResponse = new StringBuilder("");
        try
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while(line != null)
            {
                jsonResponse.append(line);
                line = bufferedReader.readLine();
            }
        }
        catch(IOException e)
        {
            Log.e(LOG_TAG,"Exception --> ",e);
            e.printStackTrace();
        }
        return jsonResponse.toString();
    }
    private static ArrayList<QuakeData> getAllQuakeData(String quakeJSON)
    {
        ArrayList<QuakeData> quakeDataList= new ArrayList<>();
        if(quakeJSON.isEmpty())
        {
            return null;
        }
        try {
            JSONObject quakeData = new JSONObject(quakeJSON);
            JSONArray featureArray = quakeData.getJSONArray("features");
            for(int i=0;i<featureArray.length();i++)
            {
                JSONObject eachQuake = featureArray.optJSONObject(i);
                JSONObject properties = eachQuake.getJSONObject("properties");
                float quakeMag = Float.parseFloat(properties.get("mag").toString());
                String placeOfQuake = properties.getString("place");
                long timeInUnix = properties.getLong("time");
                String url = properties.getString("url");

                quakeDataList.add(new QuakeData(quakeMag,placeOfQuake,timeInUnix,url));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return quakeDataList;
    }
    /*public static String  getQuakeURL(int position)
    {
        String quakeURL="";
        try {
            JSONObject quakeData = new JSONObject();
            JSONArray featureArray = quakeData.getJSONArray("features");
            JSONObject eachQuake = featureArray.optJSONObject(position);
            JSONObject properties = eachQuake.getJSONObject("properties");
            String url = properties.getString("url");

            quakeURL = url;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return quakeURL;
    }*/
    /*
    JSONObject quakeData = new JSONObject(SAMPLE_JSON_QUERY);
            JSONArray featureArray = quakeData.getJSONArray("features");

            for(int i=0;i<10;i++)
            {
                JSONObject eachQuake = featureArray.optJSONObject(i);
                JSONObject properties = eachQuake.getJSONObject("properties");
                float mag = Float.parseFloat(properties.getString("mag").toString());
                String place = properties.getString("place").toString();
                long timeInUnix = Long.parseLong(properties.getString("time").toString());
                //String quakeURL = properties.getString("url");

                quakeDataList.add(new QuakeData(mag,place,timeInUnix));
            }
     */
}
