package com.seu.magiccamera.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.provider.MediaStore;

import com.seu.magiccamera.R;
import com.seu.magiccamera.adapter.App;
import com.seu.magiccamera.adapter.FilterAdapter;
import com.seu.magiccamera.adapter.PoemAdapter;
import com.seu.magiccamera.view.edit.Poemtextview;
import com.seu.magicfilter.MagicEngine;
import com.seu.magicfilter.filter.helper.MagicFilterType;
import com.seu.magicfilter.widget.MagicImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.ConceptModel;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import com.hankcs.hanlp.dictionary.CoreSynonymDictionary;

import static android.app.PendingIntent.getActivity;


/**
 * Created by luoyin on 2017/5/7.
 */
public class ProcessalbumActivity extends Activity implements PoemAdapter.ListItemClickListener{
    private Bitmap bmp;                          //载入图片
    private RelativeLayout Imagelayout;
    private LinearLayout mFilterLayout;
    private RecyclerView mFilterListView;
    private LinearLayout mPoemLayout;
    private RecyclerView mPoemListView;
    private FilterAdapter mAdapter;
    private File img;
    private Poemtextview poemtext;
    private MagicEngine magicEngine;
    private JSONObject testJson;
    private ArrayList<String> poem_list = new ArrayList<>();
    private JSONArray array = new JSONArray();
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
    @NonNull private final PoemAdapter adapter = new PoemAdapter();
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //将相册的图片传过来
        Intent intent = getIntent();
        String path_album= intent.getStringExtra("path_album");
        Uri pathalbum=Uri.parse(path_album);
        img=new File(pathalbum.getPath());
//        System.out.println(img.length());
        //设置图片预览的区域
        Imagelayout = (RelativeLayout) findViewById(R.id.Content_Layout);
        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        android.view.ViewGroup.LayoutParams pp = Imagelayout.getLayoutParams();
        pp.width=screenSize.x;
        pp.height=screenSize.x * 5 / 4;
        Imagelayout.setLayoutParams(pp);
        //显示图片
        MagicImageView imageView = (MagicImageView)findViewById(R.id.imageView1);
        MagicEngine.Builder builder = new MagicEngine.Builder();
        magicEngine = builder
                .build((MagicImageView)findViewById(R.id.imageView1));
//        bmpFactoryOptions.inJustDecodeBounds = false;
        bmp = getBitmapFromUri(pathalbum);
        android.view.ViewGroup.LayoutParams pp1 = imageView.getLayoutParams();
        int bmpwidth=bmp.getWidth();
        int bmpheight=bmp.getHeight();
        pp1.width=bmpwidth;
        pp1.height=bmpheight;
        imageView.setLayoutParams(pp1);
        System.out.println(bmpwidth);
        System.out.println(bmpheight);
        imageView.setImageBitmap(bmp); //显示照片


        //滤镜菜单（显示与监听）
        mFilterLayout = (LinearLayout)findViewById(R.id.layout_filter);
        mFilterListView = (RecyclerView) findViewById(R.id.filter_listView);//滤镜菜单view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mFilterListView.setLayoutManager(linearLayoutManager);
        mAdapter = new FilterAdapter(this, types);
        mFilterListView.setAdapter(mAdapter);
        mAdapter.setOnFilterChangeListener(onFilterChangeListener);

        //按钮的渲染
        findViewById(R.id.btn_camera_filter).setOnClickListener(btn_listener);
        findViewById(R.id.btn_camera_filter).setOnClickListener(btn_listener);
        findViewById(R.id.btn_camera_closefilter).setOnClickListener(btn_listener);
        findViewById(R.id.btn_camera_beauty).setOnClickListener(btn_listener);
        findViewById(R.id.btn_closepoems).setOnClickListener(btn_listener);

        //诗词文字
        poemtext=(Poemtextview) findViewById(R.id.poemtext);
        load_json();
        mPoemLayout=(LinearLayout) findViewById(R.id.resultsList);
        mPoemListView = (RecyclerView) findViewById(R.id.poem_listView);//古诗菜单
        mPoemListView.setLayoutManager(new LinearLayoutManager(this));
        mPoemListView.setAdapter(adapter);
        onImagePicked(img);

    }
    /* uri转化为bitmap */
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
// 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                    this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
//            Log.e("[Android]", e.getMessage());
//            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }
    //下载诗词
    private void load_json() {
        try {
            InputStreamReader isr = new InputStreamReader(getAssets().open("poem.json"), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            isr.close();
            testJson = new JSONObject(builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //匹配
    private void match_poem(List<ClarifaiOutput<Concept>> predictions) {
        poem_list = new ArrayList<>();
        try {
            List<Concept> results = predictions.get(0).data();
            //the list of the title of poems
            List title_list = new ArrayList();
            for (Iterator<String> iterator = testJson.keys(); iterator.hasNext(); ) {
                String key = iterator.next();
                title_list.add(key);
            }
            //使用hanlp计算语义距离
            double[] numarray = new double[title_list.size()];
            for (int i = 0; i < results.size(); i++) {
                for (int j = 0; j < title_list.size(); j++) {
                    numarray[j] += CoreSynonymDictionary.similarity(results.get(i).name().toString(), title_list.get(j).toString());
                }
            }
            //返回最符合的诗句title下标
            int[] index = SearchMaxWithIndex(numarray);
            //通过数组返回诗句
            poem_list = GetResuleFromJson(index, title_list);

        } catch(Exception e){
            e.printStackTrace();
        }

    }

    private static int[] SearchMaxWithIndex(double[] arr) {
        int[] pos = new int[arr.length];
        int position = 0;
        int j = 1;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > arr[position]) {
                position = i;
                j = 1;
            }
            else if (arr[i] == arr[position])
                pos[j++] = i;
        }
        pos[0] = position;

        if (j < arr.length) pos[j] = -1;
        return pos;
    }

    private ArrayList GetResuleFromJson(int[] pos, List titlelist) {
        ArrayList resultlist = new ArrayList();
        try {
            for (int i = 0; i < pos.length; i++) {
                if (pos[i] == -1) break;
                JSONArray jsonArray = new JSONArray();
                jsonArray = testJson.getJSONArray(titlelist.get(pos[i]).toString());
                for (int j = 0; j < jsonArray.length(); j++) {
                    resultlist.add(jsonArray.getString(j));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return resultlist;
    }
//监听
@Override
public void onListItemClick(int clickedItemIndex) {
    poemtext.setText(poem_list.get(clickedItemIndex));
}
//匹配的线程
    private void onImagePicked(@NonNull final File image) {
        // Now we will upload our image to the Clarifai API
//        setBusy(true);

        // Make sure we don't show a list of old concepts while the image is being uploaded
//        adapter.setData(Collections.<Concept>emptyList());

        new AsyncTask<Void, Void, ClarifaiResponse<List<ClarifaiOutput<Concept>>>>() {
            @Override protected ClarifaiResponse<List<ClarifaiOutput<Concept>>> doInBackground(Void... params) {
                // The default Clarifai model that identifies concepts in images
                final ConceptModel generalModel = App.get().clarifaiClient().getDefaultModels().generalModel();

                // Use this model to predict, with the image that the user just selected as the input
                return generalModel.predict()
                        .withInputs(ClarifaiInput.forImage(ClarifaiImage.of(image)))
                        .executeSync();
            }

            @Override protected void onPostExecute(ClarifaiResponse<List<ClarifaiOutput<Concept>>> response) {
//                setBusy(false);
//                if (!response.isSuccessful()) {
//                    showErrorSnackbar(R.string.error_while_contacting_api);
//                    return;
//                }
                final List<ClarifaiOutput<Concept>> predictions = response.get();
//                if (predictions.isEmpty()) {
//                    showErrorSnackbar(R.string.no_results_from_api);
//                    return;
//                }
                match_poem(predictions);
                adapter.setData(poem_list,ProcessalbumActivity.this);
//                imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
            }

//            private void showErrorSnackbar(@StringRes int errorString) {
//                Snackbar.make(
//                        root,
//                        errorString,
//                        Snackbar.LENGTH_INDEFINITE
//                ).show();
//            }
        }.execute();
    }

    private View.OnClickListener btn_listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_camera_beauty:
//                    matching();
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
