package com.manuelpeinado.geocodingtask;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

public class ReverseGeocodingTask extends AsyncTask<Location, Void, Address> {

    protected static final String TAG = ReverseGeocodingTask.class.getSimpleName();
    private Context mContext;
    private ReverseGeocodingListener mListener;
    private Location mLocation;
    private boolean mockFailure;
    private boolean mockSlowProgress;

    public ReverseGeocodingTask(Context context) {
        this.mContext = context;
    }

    public void setListener(ReverseGeocodingListener listener) {
        this.mListener = listener;
    }

    public Location getLocation() {
        return mLocation;
    }

    @Override
    protected Address doInBackground(Location... params) {
        if (mockFailure) {
            GeocodingTask.sleep(2000);
            return null;
        }
        if (mockSlowProgress) {
            GeocodingTask.sleep(6000);
        }
        Geocoder geocoder = new Geocoder(mContext);
        List<Address> results;
        try {
            mLocation = params[0];
            results = geocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
            if (results.size() > 0) {
                return results.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Address result) {
        if (mListener != null) {
            mListener.onReverseGeocodingResultReady(this, result);
        }
    }

    public void setMockFailure(boolean mockFailure) {
        this.mockFailure = mockFailure;
    }

    public void cancel() {
        cancel(true);
    }
}