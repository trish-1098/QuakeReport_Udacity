package com.android_new.trish.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by trish on 2/27/2018.
 */

public class QuakeLoader extends AsyncTaskLoader<ArrayList<QuakeData>> {

    private String url;
    public QuakeLoader(Context context,String url)
    {
        super(context);
        this.url=url;
    }
    @Override
    protected void onStartLoading() {
        Log.i("In onStartLoading()","Successfully started loading");
        forceLoad();
    }
    @Override
    public ArrayList<QuakeData> loadInBackground() {
        if(url.isEmpty() || url.equals(""))
        {
            return null;
        }
        final ArrayList<QuakeData> qdataResult = QueryFetcher.extractQuakes(url);
        Log.i("In loadInBackground()","Successfully finished executing");
        return qdataResult;
    }
}
