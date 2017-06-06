package com.apps.glideimagelibrary.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Kavitha on 6/6/2017.
 */

public class AppController  extends Application{


    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }


    public static synchronized AppController getmInstance(){
        return  mInstance;
    }


    public RequestQueue getmRequestQueue(){
        if (mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void  addToRequestQueue(Request<T> request,String tag){
        // set the default tag if tag is empty
        request.setTag(TextUtils.isEmpty(tag)?TAG:tag);
        getmRequestQueue().add(request);
    }


    public <T> void addToRequestQueue(Request<T> request){
        request.setTag(TAG);
        getmRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag){
        if (mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }

}
