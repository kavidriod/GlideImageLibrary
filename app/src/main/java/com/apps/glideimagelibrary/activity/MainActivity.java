package com.apps.glideimagelibrary.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.apps.glideimagelibrary.R;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.apps.glideimagelibrary.adapter.GalleryAdapter;
import com.apps.glideimagelibrary.app.AppController;
import com.apps.glideimagelibrary.model.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private static final String endpoint = "http://api.androidhive.info/json/glide.json";
    private ArrayList<Image> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_View);

        pDialog = new ProgressDialog(this);
        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(this,images);


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(),
                          recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Log.e(TAG,"addOnItemTouchListener Clicked : "+position);

                Bundle  bundle = new Bundle();
                bundle.putSerializable("IMAGE",images);;
                bundle.putSerializable("POSITION",position);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment slideshowDialogFragment = SlideshowDialogFragment.newInstance();
                slideshowDialogFragment.setArguments(bundle);
                slideshowDialogFragment.show(fragmentTransaction,"SlideShow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        fetchImagesFromWebService();

    }

    private void fetchImagesFromWebService() {
     pDialog.setMessage("Downloading Json ... ");
        pDialog.show();



        JsonArrayRequest jsonrequest = new JsonArrayRequest(endpoint,
                new Response.Listener<JSONArray>(){

                    @Override
                    public void onResponse(JSONArray response) {

                        Log.e(TAG,"Error : "+response.toString());
                        pDialog.hide();

                        images.clear();

                        for (int i=0;i<response.length();i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                Image eachImage = new Image();
                                eachImage.setName(jsonObject.getString("name"));
                                eachImage.setTimestamp(jsonObject.getString("timestamp"));

                                JSONObject urlJsonObject = jsonObject.getJSONObject("url");
                                eachImage.setLarge(urlJsonObject.getString("large"));
                                eachImage.setMedium(urlJsonObject.getString("medium"));
                                eachImage.setSmall(urlJsonObject.getString("small"));
                                  images.add(eachImage);


                            } catch (JSONException e) {
                                Log.e(TAG,"Error : "+e.getMessage());

                            }
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
             Log.e(TAG,"Error : "+error.getMessage());
                pDialog.hide();
            }
        });
// Adding request to request queue

        AppController.getmInstance().addToRequestQueue(jsonrequest);

    }
}
