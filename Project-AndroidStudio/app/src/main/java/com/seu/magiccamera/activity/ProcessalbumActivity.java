package com.seu.magiccamera.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.seu.magiccamera.R;
import com.seu.magiccamera.adapter.FilterAdapter;
import com.seu.magicfilter.MagicEngine;
import com.seu.magicfilter.filter.helper.MagicFilterType;
import com.seu.magicfilter.widget.MagicImageView;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by luoyin on 2017/5/7.
 */



public class ProcessalbumActivity extends Activity {
    //    private ImageView imageView;
    private Bitmap bmp;                          //载入图片
    private Bitmap mbmp;                       //复制模版
    private RelativeLayout Imagelayout;
    private LinearLayout mFilterLayout;
    private RecyclerView mFilterListView;
    private LinearLayout mPoemLayout;
    private RecyclerView mPoemListView;
    private FilterAdapter mAdapter;
    private MagicEngine magicEngine;
    private final MagicFilterType[] types = new MagicFilterType[]{
            MagicFilterType.NONE,
            MagicFilterType.FAIRYTALE,
            MagicFilterType.SUNRISE,
            MagicFilterType.SUNSET,
            MagicFilterType.WHITECAT,
            MagicFilterType.BLACKCAT,
            MagicFilterType.SKINWHITEN,
            MagicFilterType.HEALTHY,
            MagicFilterType.SWEETS,
            MagicFilterType.ROMANCE,
            MagicFilterType.SAKURA,
            MagicFilterType.WARM,
            MagicFilterType.ANTIQUE,
            MagicFilterType.NOSTALGIA,
            MagicFilterType.CALM,
            MagicFilterType.LATTE,
            MagicFilterType.TENDER,
            MagicFilterType.COOL,
            MagicFilterType.EMERALD,
            MagicFilterType.EVERGREEN,
            MagicFilterType.CRAYON,
            MagicFilterType.SKETCH,
            MagicFilterType.AMARO,
            MagicFilterType.BRANNAN,
            MagicFilterType.BROOKLYN,
            MagicFilterType.EARLYBIRD,
            MagicFilterType.FREUD,
            MagicFilterType.HEFE,
            MagicFilterType.HUDSON,
            MagicFilterType.INKWELL,
            MagicFilterType.KEVIN,
            MagicFilterType.LOMO,
            MagicFilterType.N1977,
            MagicFilterType.NASHVILLE,
            MagicFilterType.PIXAR,
            MagicFilterType.RISE,
            MagicFilterType.SIERRA,
            MagicFilterType.SUTRO,
            MagicFilterType.TOASTER2,
            MagicFilterType.VALENCIA,
            MagicFilterType.WALDEN,
            MagicFilterType.XPROII
    };

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Intent intent = getIntent();
//        String path = intent.getStringExtra("path");
        ArrayList<Uri> path_album= intent.getParcelableArrayListExtra("path_album");
//        File img= new File(path_album);
//        System.out.println(img.length());

//        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
//        bmpFactoryOptions.inJustDecodeBounds = true;
//        bmp = BitmapFactory.decodeFile(path,bmpFactoryOptions);
            System.out.println(path_album);



//        Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
//
//        final ClarifaiClient client = new ClarifaiBuilder("-kPm0OiE1VRMGcYo6wPIjonTzkQqlS2Dq4fYmoKw", "fRLDegHHQBDRbJqzbbsaWyUwjr5BseJme3zXQjbC").buildSync();
//
//        final List<ClarifaiOutput<Concept>> predictionResults =
//                client.getDefaultModels().generalModel() // You can also do Clarifai.getModelByID("id") to get custom models
//                        .predict()
//                        .withInputs(
//                                ClarifaiInput.forImage(ClarifaiImage.of(img))
//                        )
//                        .executeSync() // optionally, pass a ClarifaiClient parameter to override the default client instance with another one
//                        .get();
//
//        for(int i=0; i<predictionResults.size(); i++){
//            System.out.println(predictionResults.get(i));
//        }



//        imageShow = (ImageView) findViewById(R.id.imageView1);
        Imagelayout = (RelativeLayout) findViewById(R.id.Content_Layout);
//        imageView = (ImageView) findViewById(R.id.imageView1);
        findViewById(R.id.btn_camera_filter).setOnClickListener(btn_listener);

        mFilterLayout = (LinearLayout)findViewById(R.id.layout_filter);
        mFilterListView = (RecyclerView) findViewById(R.id.filter_listView);//滤镜菜单
        mPoemLayout=(LinearLayout) findViewById(R.id.resultsList);
        mPoemListView = (RecyclerView) findViewById(R.id.poem_listView);//古诗菜单

        findViewById(R.id.btn_camera_filter).setOnClickListener(btn_listener);
        findViewById(R.id.btn_camera_closefilter).setOnClickListener(btn_listener);
        findViewById(R.id.btn_camera_beauty).setOnClickListener(btn_listener);
        findViewById(R.id.btn_closepoems).setOnClickListener(btn_listener);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mFilterListView.setLayoutManager(linearLayoutManager);

        mAdapter = new FilterAdapter(this, types);
        mFilterListView.setAdapter(mAdapter);
        mAdapter.setOnFilterChangeListener(onFilterChangeListener);

        MagicImageView imageView = (MagicImageView)findViewById(R.id.imageView1);

        MagicEngine.Builder builder = new MagicEngine.Builder();
        magicEngine = builder
                .build((MagicImageView)findViewById(R.id.imageView1));



        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        android.view.ViewGroup.LayoutParams pp = Imagelayout.getLayoutParams();
        pp.width=screenSize.x;
        pp.height=screenSize.x * 5 / 4;
        Imagelayout.setLayoutParams(pp);

//        bmpFactoryOptions.inJustDecodeBounds = false;
//        bmp = BitmapFactory.decodeFile(path,bmpFactoryOptions);
//        mbmp = bmp.copy(Bitmap.Config.ARGB_8888, true);
//        imageView.setImageBitmap(bmp); //显示照片

//        Glide.with(this).load(path).into(imageView);
//        Picasso.with(this)
//                .load(path)
//                .into(imageView);


//        Toast.makeText(this, path, Toast.LENGTH_SHORT).show();


    }
    private View.OnClickListener btn_listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_camera_beauty:
                    showPoems();

                    break;

                case R.id.btn_camera_filter:
                    showFilters();

                    break;

                case R.id.btn_camera_closefilter:
                    hideFilters();
                    break;
                case R.id.btn_closepoems:
                    hidePoems();
                    break;
            }
        }
    };
    private void showFilters(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFilterLayout, "translationY", mFilterLayout.getHeight(), 0);
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
//                findViewById(R.id.btn_camera_shutter).setClickable(false);
                mFilterLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        animator.start();
    }
    private void hideFilters(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFilterLayout, "translationY", 0 ,  mFilterLayout.getHeight());
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                mFilterLayout.setVisibility(View.INVISIBLE);
//                findViewById(R.id.btn_camera_shutter).setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub
                mFilterLayout.setVisibility(View.INVISIBLE);
//                findViewById(R.id.btn_camera_shutter).setClickable(true);
            }
        });
        animator.start();
    }
    private void showPoems(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(mPoemLayout, "translationY", mPoemLayout.getHeight(), 0);
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                findViewById(R.id.btn_camera_beauty).setClickable(false);
                findViewById(R.id.btn_camera_filter).setClickable(false);
                mPoemLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        animator.start();
    }
    private void hidePoems(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(mPoemLayout, "translationY", 0 , mPoemLayout.getHeight());
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                mPoemLayout.setVisibility(View.INVISIBLE);
                findViewById(R.id.btn_camera_beauty).setClickable(true);
                findViewById(R.id.btn_camera_filter).setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub
                mPoemLayout.setVisibility(View.INVISIBLE);
                findViewById(R.id.btn_camera_beauty).setClickable(true);
                findViewById(R.id.btn_camera_filter).setClickable(true);
            }
        });
        animator.start();
    }
    private FilterAdapter.onFilterChangeListener onFilterChangeListener = new FilterAdapter.onFilterChangeListener(){

        @Override
        public void onFilterChanged(MagicFilterType filterType) {
            magicEngine.setFilter(filterType);
        }
    };




}
