package com.android_new.trish.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;;
import android.widget.ListView;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<ArrayList<QuakeData>>{

    private TextView emptyTextView;
    private ProgressBar progressBar;
    private static final int EARTHQUAKE_LOADER_ID = 1;
    QuakeDataAdapter quakeAdapter;
    private static final String USGS_DATA_QUERY = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fetch the progress view
        progressBar = (ProgressBar) findViewById(R.id.progress_view);
        ListView quakeList = (ListView) findViewById(R.id.quake_list_view);
        //Setting the empty view incase no result occurs
        emptyTextView = (TextView) findViewById(R.id.empty_case_textview);
        quakeList.setEmptyView(emptyTextView);
        quakeAdapter = new QuakeDataAdapter(MainActivity.this,new ArrayList<QuakeData>());
        quakeList.setAdapter(quakeAdapter);

        quakeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                QuakeData quakeTemp = quakeAdapter.getItem(i);
                //The method QueryFetcher.getQuakeURL() can be used also
                //String quakeURLFetched = quakeTemp.getQuakeURL();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(quakeTemp.getQuakeURL()));
                //intent.setData(Uri.parse(quakeTemp.getQuakeURL()));
                startActivity(intent);
            }
        });
        if(checkConnection()) {
            LoaderManager loaderManager = getLoaderManager();
            Log.i("Loader initialization->", "Successfull");
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
            //EarthquakeAsync earthquakeAsync = new EarthquakeAsync();
            //earthquakeAsync.execute(USGS_DATA_QUERY);
        }
        else
        {
            emptyTextView.setText(R.string.no_net);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public Loader<ArrayList<QuakeData>> onCreateLoader(int i,Bundle bundle)
    {
        Log.i("In onCreateLoader()","Successfull");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        Uri baseUri = Uri.parse(USGS_DATA_QUERY);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", "time");
        return new QuakeLoader(this, uriBuilder.toString());
    }
    @Override
    public void onLoadFinished(Loader<ArrayList<QuakeData>> loader,ArrayList<QuakeData> quakeData)
    {
        progressBar.setVisibility(View.GONE);
        Log.i("In onLoadFinished()","Successfully received Data");
        //Setting the empty textview
        emptyTextView.setText(R.string.no_quake);
        quakeAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (quakeData != null && !quakeData.isEmpty()) {
            quakeAdapter.addAll(quakeData);
        }
    }
    @Override
    public void onLoaderReset(Loader<ArrayList<QuakeData>> loader)
    {
        Log.i("In onLoaderReset()","Successfully resetted the loader");
        quakeAdapter.clear();
    }
    public boolean checkConnection()
    {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /*private class EarthquakeAsync extends AsyncTask<String,Void,ArrayList<QuakeData>>
    {

        @Override
        protected ArrayList<QuakeData> doInBackground(String... earthquakes) {
            if(earthquakes.length < 1 || earthquakes[0] == null)
            {
                return null;
            }
            final ArrayList<QuakeData> qdataResult = QueryFetcher.extractQuakes(earthquakes[0]);
            return qdataResult;
        }

        @Override
        protected void onPostExecute(final ArrayList<QuakeData> quakeData) {
            // Clear the adapter of previous earthquake data
            quakeAdapter.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (quakeData != null && !quakeData.isEmpty()) {
                quakeAdapter.addAll(quakeData);
            }
        }
    }*/
}
