package com.apps.glideimagelibrary.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.glideimagelibrary.R;
import com.apps.glideimagelibrary.model.Image;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SlideshowDialogFragment extends DialogFragment implements  ViewPager.OnPageChangeListener{

    private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private ArrayList<Image> images;
    private int selectedPosition = 0;
    private TextView lblCount, lblTitle, lblDate;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;

    public SlideshowDialogFragment() {
        // Required empty public constructor
    }

    static SlideshowDialogFragment newInstance(){
        SlideshowDialogFragment fragment = new SlideshowDialogFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slideshow_dialog, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        lblCount = (TextView) view.findViewById(R.id.lbl_count);
        lblTitle = (TextView) view.findViewById(R.id.title);
        lblDate = (TextView) view.findViewById(R.id.date);


        images = (ArrayList<Image>) getArguments().getSerializable("IMAGE");
        selectedPosition = getArguments().getInt("POSITION");

        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "images size: " + images.size());

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(this);

        setCurrentItem(selectedPosition);

        return  view;
    }

    private void setCurrentItem(int selectedPosition) {
        viewPager.setCurrentItem(selectedPosition);
        displayMetaInfo(selectedPosition);
    }

    private void displayMetaInfo(int selectedPosition) {
        lblCount.setText((selectedPosition+1)+" of "+images.size());

        Image eachImage = images.get(selectedPosition);
        lblTitle.setText(eachImage.getName());
        lblDate.setText(eachImage.getTimestamp());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
       displayMetaInfo(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public class  MyViewPagerAdapter extends PagerAdapter{

        private LayoutInflater layoutInflater;

        @Override
        public int getCount() {
            return images.size();
        }


        public  MyViewPagerAdapter(){

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ( (View) object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView( (View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview,container,false);


            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

            Image image  = images.get(position);


            Glide.with(getActivity()).load(image.getLarge())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);

            container.addView(view);


            return view;
        }
    }
}
